package io.flowing.retail.order.flow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.flowing.retail.order.domain.Order;
import io.flowing.retail.order.messages.cloud.Message;
import io.flowing.retail.order.messages.cloud.MessageSender;
import io.flowing.retail.order.persistence.OrderRepository;

@Component
public class ShipGoodsAdapter implements JavaDelegate {
  
  @Autowired
  private MessageSender messageSender;  

  @Autowired
  private OrderRepository orderRepository;  

  @Override
  public void execute(DelegateExecution context) throws Exception {
    Order order = orderRepository.findById( //
        (String)context.getVariable("orderId")).get(); 
    String pickId = (String)context.getVariable("pickId"); // TODO read from step before!
    String traceId = context.getProcessBusinessKey();

    messageSender.send(new Message<ShipGoodsCommandPayload>( //
            "ShipGoodsCommand", //
            traceId, //
            new ShipGoodsCommandPayload() //
              .setRefId(order.getId())
              .setPickId(pickId) //
              .setRecipientName("Recipient Name") //
              .setRecipientAddress("Recipient Address"))); //TODO read from object after enrichment is in place
  }  

}
