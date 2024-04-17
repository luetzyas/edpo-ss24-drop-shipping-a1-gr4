package io.flowing.retail.inventory.application;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import io.flowing.retail.inventory.domain.*;
import io.flowing.retail.inventory.messages.GoodsAvailableEventPayload;
import io.flowing.retail.inventory.mqtt.StockStateUpdater;
import org.springframework.stereotype.Component;

@Component
public class InventoryService {

  private final Map<String, FactoryStockState> stockStateMap = new ConcurrentHashMap<>();

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(InventoryService.class);


  /**
   * reserve goods on stock for a defined period of time
   * 
   * @param reason A reason why the goods are reserved (e.g. "customer order")
   * @param refId A reference id fitting to the reason of reservation (e.g. the order id), needed to find reservation again later
   * @param expirationDate Date until when the goods are reserved, afterwards the reservation is removed
   * @return if reservation could be done successfully
   */
  public boolean reserveGoods(List<Item> items, String reason, String refId, LocalDateTime expirationDate) {
    // TODO: Implement
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
    System.out.println("InventoryService: checkAvailability");
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
    System.out.println("Checking if requested items available");
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
  public List<GoodsAvailableEventPayload.ItemAvailability> getUnavailableItems(List<OrderItem> orderItems) {
    System.out.println("Checking if requested items are unavailable");
    // Aggregate the required amounts by articleId
    Map<String, Integer> requestedAmounts = orderItems.stream()
            .collect(Collectors.groupingBy(OrderItem::getArticleId, Collectors.summingInt(OrderItem::getAmount)));
    // Filter and map to ItemAvailability, highlighting shortfalls
    return requestedAmounts.entrySet().stream()
            .map(entry -> {
              int stockAvailable = stockStateMap.containsKey(entry.getKey()) ? stockStateMap.get(entry.getKey()).getAmount() : 0;
              int shortfall = entry.getValue() > stockAvailable ? entry.getValue() - stockAvailable : 0;
              return new GoodsAvailableEventPayload.ItemAvailability(entry.getKey(), entry.getValue(), shortfall);
            })
            .collect(Collectors.toList());
  }

    public void updateStock(InventoryUpdateMessage updateMessage) {
    System.out.println("Updating Inventory with: " + updateMessage);
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
      stockStateMap.putAll(tempMap);
      System.out.println("Updated Inventory to: " + stockStateMap);
    }
}
