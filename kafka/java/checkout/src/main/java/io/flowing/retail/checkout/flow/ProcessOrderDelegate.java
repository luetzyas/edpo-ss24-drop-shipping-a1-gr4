package io.flowing.retail.checkout.flow;

import io.flowing.retail.checkout.domain.Customer;
import io.flowing.retail.checkout.domain.Order;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class ProcessOrderDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // Retrieve form data from process variables
        String customerName = (String) execution.getVariable("customerName");
        String customerAddress = (String) execution.getVariable("customerAddress");
        // Process the order details here
        // For simplicity, just setting an example order object as a process variable
        Order order = new Order();
        order.setCustomer(new Customer(customerName, customerAddress));
        // Add items to the order...
        execution.setVariable("order", order); // Store the order object for the next task
    }
}
