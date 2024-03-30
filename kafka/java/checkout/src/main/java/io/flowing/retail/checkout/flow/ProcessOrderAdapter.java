package io.flowing.retail.checkout.flow;

import io.flowing.retail.checkout.domain.Customer;
import io.flowing.retail.checkout.domain.Order;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProcessOrderAdapter implements JavaDelegate {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ProcessOrderAdapter.class);


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

        execution.setVariable("order", order); // Store the order object for the next task
    }
}
