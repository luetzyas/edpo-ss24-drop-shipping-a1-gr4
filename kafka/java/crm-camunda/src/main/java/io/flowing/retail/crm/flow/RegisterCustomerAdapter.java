package io.flowing.retail.crm.flow;


import io.flowing.retail.crm.application.CrmService;
import io.flowing.retail.crm.domain.Customer;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RegisterCustomerAdapter implements JavaDelegate {

    @Autowired
    private CrmService crmService;


    @Override
    public void execute(DelegateExecution execution) throws Exception {

        try {
        // Create a new customer
        Customer customer = new Customer();

        customer.setName((String) execution.getVariable("name"));
        customer.setAddress((String) execution.getVariable("address"));
        customer.setEmail((String) execution.getVariable("email"));

        execution.setVariable("customer", customer); // Store the customer object for the next task

        crmService.processNewCustomer(customer);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
}
