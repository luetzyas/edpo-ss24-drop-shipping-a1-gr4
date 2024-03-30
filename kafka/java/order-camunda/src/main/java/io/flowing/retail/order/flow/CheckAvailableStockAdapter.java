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

    // Create the payload for the stock check event
    CheckAvailableStockEventPayload payload = new CheckAvailableStockEventPayload();
    payload.setRefId(order.getId());
    payload.setItems(new ArrayList<>(order.getItems())); // Create a new list from the order's items

    Message<CheckAvailableStockEventPayload> message = new Message<>(
            "CheckAvailableStockEvent",
            traceId,
            payload
    );

    messageSender.send(message);

  }

}
