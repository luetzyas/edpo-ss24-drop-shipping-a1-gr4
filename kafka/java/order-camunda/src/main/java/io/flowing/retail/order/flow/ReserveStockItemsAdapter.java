package io.flowing.retail.order.flow;

import io.flowing.retail.order.domain.Order;
import io.flowing.retail.order.messages.cloud.Message;
import io.flowing.retail.order.messages.cloud.MessageSender;
import io.flowing.retail.order.persistence.OrderRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReserveStockItemsAdapter implements JavaDelegate {
  
  @Autowired
  private MessageSender messageSender;  

  @Autowired
  private OrderRepository orderRepository;  

  @Override
  public void execute(DelegateExecution context) throws Exception {
    Order order = orderRepository.findById( //
        (String)context.getVariable("orderId")).get(); 
    String traceId = context.getProcessBusinessKey();

    System.out.println("ReserveStockItemsAdapter retrieved order from orderRepository: " + order);

    // Create the payload for the stock check event
    ReserveStockItemsCommandPayload payload = new ReserveStockItemsCommandPayload();
    payload.setRefId(order.getId());
    payload.setItems(order.getItems());
    System.out.println("ReserveStockItemsAdapter created payload: " + payload);

    Message<ReserveStockItemsCommandPayload> message = new Message<>(
            "ReserveGoodsCommand", // old: CheckAvailableStockEvent
            traceId,
            payload
    );
    System.out.println("Sending ReserveGoodsCommand: " + message);
    messageSender.send(message);

  }

}
