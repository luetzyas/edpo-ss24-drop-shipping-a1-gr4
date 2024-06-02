package io.flowing.retail.crm.flow;


import io.flowing.retail.crm.application.CrmService;
import io.flowing.retail.crm.domain.Customer;
import io.flowing.retail.crm.messages.CustomerRegisteredEventPayload;
import io.flowing.retail.crm.messages.Message;
import io.flowing.retail.crm.messages.MessageSender;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotifyOrderAdapter implements JavaDelegate {

    @Autowired
    private MessageSender messageSender;


    @Override
    public void execute(DelegateExecution execution) throws Exception {

        try {
        // Create a new customer
        Customer customer = (Customer) execution.getVariable("customer");

        // Create MessagePayload
        CustomerRegisteredEventPayload customerRegisteredEvent = new CustomerRegisteredEventPayload();
        customerRegisteredEvent.setCustomer(customer);
        customerRegisteredEvent.setRefId(execution.getVariable("refId").toString());

        messageSender.sendMessage(
                new Message<CustomerRegisteredEventPayload>(
                        "CustomerRegisteredEvent",
                        customerRegisteredEvent)
        );

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
