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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProcessOrderAdapter implements JavaDelegate {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ProcessOrderAdapter.class);
    @Autowired
    private CheckoutService checkoutService;


    @Override
    public void execute(DelegateExecution execution) throws Exception {

        // Create a new order
        logger.info("ProcessOrderAdapter: Creating order");
        Order order = new Order();

        // Set customer details
        Customer customer = new Customer();
        customer.setName((String) execution.getVariable("customerName"));
        customer.setAddress((String) execution.getVariable("customerAddress"));
        order.setCustomer(customer);
        logger.info("ProcessOrderAdapter: Customer details set to " + customer.getName() + " " + customer.getAddress());

        // Aggregate items by articleId
        Map<String, Integer> aggregatedItems = new HashMap<>();
        for (int i = 1; i <= 3; i++) { // 3 items Red/Blue/White
            String articleId = (String) execution.getVariable("articleId" + i);
            Integer amount = (Integer) execution.getVariable("amount" + i);

            if (articleId != null && amount != null) {
                aggregatedItems.merge(articleId, amount, Integer::sum);
            }
        }

        // Add aggregated items to the order
        aggregatedItems.forEach(order::addItem);
        logger.info("ProcessOrderAdapter: Items added to order " + order.getItems());

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
        execution.setVariable("order", order); // Store the order object for the next task
    }
}
