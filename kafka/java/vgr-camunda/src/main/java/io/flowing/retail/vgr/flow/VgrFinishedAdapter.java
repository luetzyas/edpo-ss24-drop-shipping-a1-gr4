package io.flowing.retail.vgr.flow;

import io.flowing.retail.vgr.messages.Message;
import io.flowing.retail.vgr.messages.MessageSender;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VgrFinishedAdapter implements JavaDelegate {
  
  @Autowired
  private MessageSender messageSender;  


  @Override
  public void execute(DelegateExecution context) throws Exception {
    String traceId = context.getProcessBusinessKey();
    System.out.println("VGR finished for order-fulfillment process instance " + traceId);
    String correlationId = (String) context.getVariable("correlationId");


    // create MovePartsEventPayload
    VgrFinishedEventPayload vgrFinishedEventPayload = new VgrFinishedEventPayload();
    vgrFinishedEventPayload.setTraceId(traceId);

    // publish
    messageSender.send(new Message<VgrFinishedEventPayload>("VgrFinishedEvent", traceId,
            vgrFinishedEventPayload).setCorrelationid(correlationId));
  }
  
}
