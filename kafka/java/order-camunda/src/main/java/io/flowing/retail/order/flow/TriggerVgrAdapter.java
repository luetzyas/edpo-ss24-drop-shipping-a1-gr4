package io.flowing.retail.order.flow;

import io.flowing.retail.order.domain.Order;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.flowing.retail.order.messages.cloud.Message;
import io.flowing.retail.order.messages.cloud.MessageSender;
import io.flowing.retail.order.persistence.OrderRepository;

@Component
public class TriggerVgrAdapter implements JavaDelegate {
  
  @Autowired
  private MessageSender messageSender;  

  @Autowired
  private OrderRepository orderRepository;

  @Override
  public void execute(DelegateExecution context) throws Exception {
    Order order = orderRepository.findById( //
        (String)context.getVariable("orderId")).get();
    String traceId = context.getProcessBusinessKey();

    // publish
    messageSender.send(new Message<TriggerVgrCommandPayload>( //
            "TriggerVgrCommand", //
            traceId, //
            new TriggerVgrCommandPayload() //
              .setRefId(order.getId())));
  }
  
}
