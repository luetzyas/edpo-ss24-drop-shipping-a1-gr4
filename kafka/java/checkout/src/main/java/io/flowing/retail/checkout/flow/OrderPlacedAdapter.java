package io.flowing.retail.checkout.flow;

import io.flowing.retail.checkout.domain.Customer;
import io.flowing.retail.checkout.domain.Order;
import io.flowing.retail.checkout.messages.Message;
import io.flowing.retail.checkout.messages.MessageSender;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderPlacedAdapter implements JavaDelegate {
    @Autowired
    private MessageSender messageSender; // Autowired to use Spring's KafkaTemplate

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(OrderPlacedAdapter.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        try {
            // Read order from process variables
            Order order = (Order) execution.getVariable("order");

            Message<Order> message = new Message<>("OrderPlacedEvent", order);
            message.setTraceid(order.getOrderId());
            message.setCorrelationid(order.getOrderId());
            messageSender.send(message); // Serialize and send the order as a Kafka message
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
