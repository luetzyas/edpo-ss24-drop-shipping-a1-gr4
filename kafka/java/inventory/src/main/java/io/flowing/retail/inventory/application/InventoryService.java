package io.flowing.retail.inventory.application;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import io.flowing.retail.inventory.domain.FactoryStockState;
import io.flowing.retail.inventory.domain.InventoryUpdateMessage;
import io.flowing.retail.inventory.messages.GoodsAvailableEventPayload;
import org.springframework.stereotype.Component;

import io.flowing.retail.inventory.domain.Item;
import io.flowing.retail.inventory.domain.PickOrder;

@Component
public class InventoryService {

  private final Map<String, FactoryStockState> stockStateMap = new ConcurrentHashMap<>();


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
  public boolean checkAvailability(List<InventoryUpdateMessage.Workpiece> workpieces) {
    // Logic to aggregate requested amounts by type
    Map<String, Integer> requestedAmounts = new HashMap<>();
    workpieces.forEach(workpiece -> requestedAmounts.merge(workpiece.getType(), 1, Integer::sum));

    // Check against current stock
    return requestedAmounts.entrySet().stream()
            .allMatch(entry ->
                    stockStateMap.containsKey(entry.getKey()) &&
                            stockStateMap.get(entry.getKey()).getAmount() >= entry.getValue());
  }

  // Get available Workpieces
  public List<GoodsAvailableEventPayload.ItemAvailability> getAvailableItems(List<InventoryUpdateMessage.Workpiece> workpieces) {
    return workpieces.stream()
            .filter(workpiece ->
                    stockStateMap.containsKey(workpiece.getType()) &&
                            stockStateMap.get(workpiece.getType()).getAmount() > 0)
            .map(workpiece -> new GoodsAvailableEventPayload.ItemAvailability(workpiece.getType(), 1,
                    stockStateMap.get(workpiece.getType()).getAmount()))
            .collect(Collectors.toList());
  }

  // Get unavailable Workpieces
  public List<GoodsAvailableEventPayload.ItemAvailability> getUnavailableItems(List<InventoryUpdateMessage.Workpiece> workpieces) {
    Map<String, Long> counts = workpieces.stream().collect(Collectors.groupingBy(InventoryUpdateMessage.Workpiece::getType, Collectors.counting()));

    return counts.entrySet().stream()
            .filter(entry ->
                    !stockStateMap.containsKey(entry.getKey()) ||
                            stockStateMap.get(entry.getKey()).getAmount() < entry.getValue())
            .map(entry -> new GoodsAvailableEventPayload.ItemAvailability(entry.getKey(), entry.getValue().intValue(),
                    stockStateMap.containsKey(entry.getKey()) ? stockStateMap.get(entry.getKey()).getAmount() : 0))
            .collect(Collectors.toList());
  }

    public void updateStock(InventoryUpdateMessage updateMessage) {
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
      tempMap.forEach((articleId, factoryStockState) -> stockStateMap.put(articleId, factoryStockState));
    }
}
