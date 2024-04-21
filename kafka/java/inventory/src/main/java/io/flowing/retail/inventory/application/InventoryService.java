package io.flowing.retail.inventory.application;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import io.flowing.retail.inventory.domain.*;
import io.flowing.retail.inventory.messages.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InventoryService {

  private final Map<String, FactoryStockState> stockStateMap = new ConcurrentHashMap<>();

  private final Map<String, List<InventoryBlockedGoodsState>> blockedGoods = new ConcurrentHashMap<>();


  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(InventoryService.class);


  public boolean reserveGoods(ReserveStockItemsCommandPayload reserveStockItemsCommand, String traceId, String correlationId) {
    // Reserve all ordered items with the inventory service
    updateBlockedGoods(reserveStockItemsCommand.getItems(), reserveStockItemsCommand.getRefId(), traceId, correlationId);

    // Check if additional items are needed
    boolean available = checkAvailability(reserveStockItemsCommand.getItems());
    System.out.println("InventoryService: Stock available: " + available);

    List<GoodsAvailableEventPayload.ItemAvailability> availableItems = getAvailableItems(reserveStockItemsCommand.getItems());
    System.out.println("InventoryService: Available items (requestedQuantity / availableQuantity): " + availableItems);

    List<GoodsAvailableEventPayload.ItemUnavailability> unavailableItems = getUnavailableItems(reserveStockItemsCommand.getItems());
    System.out.println("MessageListener: Unavailable items (requestedQuantity / unavailableQuantity): " + unavailableItems);

    // if unavailable items exist, order them
    if (!available) {
      System.out.println("InventoryService: Ordering additional items");
      // Order additional items
      List<OrderItem> orderItems = unavailableItems.stream()
              .map(item -> new OrderItem(item.getItemId(), item.getUnavailableQuantity()))
              .collect(Collectors.toList());
      available = orderAdditionalItems(reserveStockItemsCommand.getRefId(), orderItems);
    }

    return available;
  }

  private boolean orderAdditionalItems(String refId, List<OrderItem> orderItems) {
    blockedGoods.forEach((key, value) -> {
      if (key.equals(refId)) {
        System.out.println("InventoryService: Additional Goods arrived, order can be fulfilled for refId: " + refId);
      }
    });
    return true;
  }

  /**
   * Order to pick the given items in the warehouse. The inventory is decreased. 
   * Reservation fitting the reason/refId might be used to fulfill the order.
   * 
   * If no enough items are on stock - an exception is thrown.
   * Otherwise a unique pick id is returned, which can be used to 
   * reference the bunch of goods in the shipping area.
   * 
   * @param items to be picked
   * @param reason for which items are picked (e.g. "customer order")
   * @param refId Reference id fitting to the reason of the pick (e.g. "order id"). Used to determine which reservations can be used.
   * @return a unique pick ID 
   */
  public String pickItems(List<Item> items, String reason, String refId) {
    PickOrder pickOrder = new PickOrder().setItems(items);    
    System.out.println("# Items picked: " + pickOrder);      
    return pickOrder.getPickId();
  }

  /**
   * New goods are arrived and inventory is increased
   */
  public void topUpInventory(String articleId, int amount) {
    // TODO: Implement
  }

  // Check overall availability based on Workpiece types and their amounts
  public boolean checkAvailability(List<OrderItem> orderItems) {
    // Logic to aggregate requested amounts by type
    try {
      Map<String, Integer> requestedAmounts = new HashMap<>();
      for (OrderItem item : orderItems) {
        if (item != null && item.getArticleId() != null) {
          requestedAmounts.merge(item.getArticleId(), item.getAmount(), Integer::sum); // Aggregate amounts by type (already done in Order Service, but just in case)
        } else {
          System.out.println("OrderItem or type is null");
        }
      }
      // Check against current stock
      return requestedAmounts.entrySet().stream() // Converts the set of entries in the requestedAmounts map into a Stream
              .allMatch(entry -> // lambda expression where entry is each element in the stream
                      stockStateMap.containsKey(entry.getKey()) &&
                              stockStateMap.get(entry.getKey()).getAmount() >= entry.getValue());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

  }

  // Get available Workpieces
  public List<GoodsAvailableEventPayload.ItemAvailability> getAvailableItems(List<OrderItem> orderItems) {
    // Aggregate the required amounts by articleId
    Map<String, Integer> requestedAmounts = orderItems.stream()
            .collect(Collectors.groupingBy(OrderItem::getArticleId, Collectors.summingInt(OrderItem::getAmount)));
    // Filter and map to ItemAvailability, considering actual stock availability
    return requestedAmounts.entrySet().stream()
            .filter(entry -> stockStateMap.containsKey(entry.getKey()))
            .map(entry -> {
              int stockAvailable = stockStateMap.get(entry.getKey()).getAmount(); // Get the available stock amount
              int amountToReport = Math.min(entry.getValue(), stockAvailable); // Report the lesser of the requested and available amounts
              return new GoodsAvailableEventPayload.ItemAvailability(entry.getKey(), entry.getValue(), amountToReport); // E.g BLUE -> (we have 3, requested 5, so we report 3)
            })
            .collect(Collectors.toList());
  }

  // Get unavailable Workpieces
  public List<GoodsAvailableEventPayload.ItemUnavailability> getUnavailableItems(List<OrderItem> orderItems) {
    System.out.println("Checking if requested items are unavailable");
    // Aggregate the required amounts by articleId
    Map<String, Integer> requestedAmounts = orderItems.stream()
            .collect(Collectors.groupingBy(OrderItem::getArticleId, Collectors.summingInt(OrderItem::getAmount)));
    // Filter and map to ItemAvailability, highlighting shortfalls
    return requestedAmounts.entrySet().stream()
            .map(entry -> {
              int stockAvailable = stockStateMap.containsKey(entry.getKey()) ? stockStateMap.get(entry.getKey()).getAmount() : 0;
              int shortfall = entry.getValue() > stockAvailable ? entry.getValue() - stockAvailable : 0;
              return new GoodsAvailableEventPayload.ItemUnavailability(entry.getKey(), entry.getValue(), shortfall);
            })
            .collect(Collectors.toList());
  }

    public void updateStock(InventoryUpdateMessage updateMessage) {
  //  System.out.println("Updating Inventory with: " + updateMessage);
      Map<String, FactoryStockState> tempMap = new HashMap<>();

      for (InventoryUpdateMessage.StockItem stockItem : updateMessage.getStockItems()) {
        InventoryUpdateMessage.Workpiece workpiece = stockItem.getWorkpiece();
        if (workpiece != null) {
          tempMap.compute(workpiece.getType(), (key, existingVal) -> {
            if (existingVal == null) {
              return new FactoryStockState(key, 1);
            } else {
              existingVal.incrementAmount();
              return existingVal;
            }
          });
        }
      }

      // Update the main inventory state with the temporary map
      // Determine if the inventory actually needs updating
      if (!mapsAreEqual(stockStateMap, tempMap)) {
        stockStateMap.putAll(tempMap);
        System.out.println("InventoryService: Updated Inventory to: " + stockStateMap);

        // Write Event to Outbox
        InventoryUpdatedEventPayload payload = new InventoryUpdatedEventPayload(stockStateMap);
        Message<InventoryUpdatedEventPayload> messagePayload = new Message<>("InventoryUpdatedEvent", UUID.randomUUID().toString(), payload);
        System.out.println("InventoryService: Writing InventoryUpdatedEvent to Outbox");
        InMemoryOutbox.addToOutbox(messagePayload);
      }
  }
  // Utility method to compare two maps
  private boolean mapsAreEqual(Map<String, FactoryStockState> oldMap, Map<String, FactoryStockState> newMap) {
    if (oldMap.size() != newMap.size()) {
      return false;
    }
    return oldMap.entrySet().stream()
            .allMatch(e -> e.getValue().equals(newMap.get(e.getKey())));
  }

  public void updateBlockedGoods(List<OrderItem> items, String refId, String traceId, String correlationId) {
    // Obtain the current list of blocked goods for this refId, or create a new list if none exists.
    List<InventoryBlockedGoodsState> currentBlockedItems = blockedGoods.computeIfAbsent(refId, k -> new ArrayList<>());

    // Iterate over the items to be reserved and update the currentBlockedItems list with each ordered item
    for (OrderItem item : items) {
      currentBlockedItems.add(new InventoryBlockedGoodsState(item.getArticleId(), item.getAmount(), false));
    }
    // Update the blockedGoods map with the new list of blocked items
    blockedGoods.put(refId, currentBlockedItems);
    System.out.println("InventoryService: Updated blocked goods for refId: " + refId + " to: " + blockedGoods.get(refId));

    // Write GoodsBlockedEvent to Outbox
    GoodsBlockedEventPayload payload = new GoodsBlockedEventPayload();
    // Aggregate blocked goods by articleId
    Map<String, InventoryBlockedGoodsState> aggregatedBlockedItems = blockedGoods.values().stream()
            .flatMap(List::stream)
            .filter(item -> !item.getConsumed())
            .collect(Collectors.toMap(
                    InventoryBlockedGoodsState::getArticleId,
                    item -> item,  // Use the item as the value in the map
                    (existingItem, newItem) -> new InventoryBlockedGoodsState(existingItem.getArticleId(), existingItem.getAmount() + newItem.getAmount(), false)
            ));

    payload.setBlockedGoodsStateMap(aggregatedBlockedItems);

    Message<GoodsBlockedEventPayload> messagePayload = new Message<>();
    messagePayload.setType("GoodsBlockedEvent");
    messagePayload.setTraceid(traceId);
    messagePayload.setCorrelationid(correlationId);
    messagePayload.setData(payload);

    // Notifying the Checkout Service of the blocked goods
    InMemoryOutbox.addToOutbox(messagePayload);
    System.out.println("Outbox: GoodsBlockedEvent added for refId: " + refId);

  }
}
