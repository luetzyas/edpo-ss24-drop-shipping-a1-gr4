package io.flowing.retail.checkout.flow;

import io.flowing.retail.checkout.application.CheckoutService;
import io.flowing.retail.checkout.domain.Customer;
import io.flowing.retail.checkout.domain.InventoryBlockedGoodsState;
import io.flowing.retail.checkout.domain.InventoryStockState;
import io.flowing.retail.checkout.domain.Order;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CheckAvailableStockAdapter implements JavaDelegate {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CheckAvailableStockAdapter.class);
    @Autowired
    private CheckoutService checkoutService;


    @Override
    public void execute(DelegateExecution execution) throws Exception {

        // Read order details from the process variables
        Order order = (Order) execution.getVariable("order");

        // Aggregate items by articleId (workaround to fit previous implementation)
        Map<String, Integer> aggregatedItems = new HashMap<>();
        order.getItems().forEach(item -> aggregatedItems.merge(item.getArticleId(), item.getAmount(), Integer::sum));

        Map<String, Integer> unavailableItems = new HashMap<>();

        for (Map.Entry<String, Integer> entry : aggregatedItems.entrySet()) {
            String articleId = entry.getKey();
            Integer requiredAmount = entry.getValue();
            // All items from inventory stock
            InventoryStockState stockState = checkoutService.getCurrentStockState().get(articleId);
            // All blocked items from inventory reserved goods
            InventoryBlockedGoodsState blockedState = checkoutService.getCurrentBlockedGoods().get(articleId);
            // Net available items (stock - reserved goods)
            int availableAmount = (stockState != null ? stockState.getAmount() : 0) - (blockedState != null ? blockedState.getAmount() : 0);
            if (availableAmount < 0) {availableAmount = 0;} // Prevent negative stock for calculation

            if (availableAmount < requiredAmount) {
                // Item is not available in sufficient quantity
                int shortAmount = requiredAmount - availableAmount;
                unavailableItems.put(articleId, shortAmount);
                System.out.println("Insufficient stock for item: " + articleId + ", short by: " + shortAmount);
            }
        }

        // Check overall availability based on Workpiece types and their amounts
        boolean allItemsAvailable = unavailableItems.isEmpty();
        if (!allItemsAvailable) {
            execution.setVariable("allItemsAvailable", false);
            // Create a string representation of unavailable items for display in Generated Task Form
            String unavailableItemsDesc = unavailableItems.entrySet().stream()
                    .map(e -> e.getKey() + " short by " + e.getValue())
                    .collect(Collectors.joining(", "));
            execution.setVariable("unavailableItems", unavailableItemsDesc);
        } else {
            execution.setVariable("allItemsAvailable", true);
        }
    }
}
