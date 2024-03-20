package io.flowing.retail.checkout.flow;

import io.flowing.retail.checkout.domain.Order;
import io.flowing.retail.checkout.messages.Message;
import io.flowing.retail.checkout.messages.MessageSender;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderPlacedAdapter implements JavaDelegate {
    @Autowired
    private MessageSender messageSender; // Autowired to use Spring's KafkaTemplate

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Order order = (Order) execution.getVariable("order");
        Message<Order> message = new Message<>("OrderPlacedEvent", order);
        messageSender.send(message); // Serialize and send the order as a Kafka message
    }
}
