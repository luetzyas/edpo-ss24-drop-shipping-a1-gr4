package io.flowing.retail.order.flow;

import io.flowing.retail.order.domain.Order;
import io.flowing.retail.order.domain.OrderItem;
import io.flowing.retail.order.messages.Message;
import io.flowing.retail.order.messages.MessageSender;
import io.flowing.retail.order.persistence.OrderRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CheckAvailableStockAdapter implements JavaDelegate {
  
  @Autowired
  private MessageSender messageSender;  

  @Autowired
  private OrderRepository orderRepository;  

  @Override
  public void execute(DelegateExecution context) throws Exception {
    Order order = orderRepository.findById( //
        (String)context.getVariable("orderId")).get(); 
    String traceId = context.getProcessBusinessKey();

    System.out.println("CheckAvailableStockAdapter retrieved order from orderRepository: " + order);

    // Create the payload for the stock check event
    CheckAvailableStockEventPayload payload = new CheckAvailableStockEventPayload();
    payload.setRefId(order.getId());
    payload.setItems(order.getItems());
    System.out.println("CheckAvailableStockAdapter created payload: " + payload);

    Message<CheckAvailableStockEventPayload> message = new Message<>(
            "CheckAvailableStockEvent",
            traceId,
            payload
    );
    System.out.println("Sending CheckAvailableStockEvent: " + message);
    messageSender.send(message);

  }

}
