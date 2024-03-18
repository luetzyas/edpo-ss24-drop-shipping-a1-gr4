package io.flowing.retail.vgr.flow;

import io.flowing.retail.vgr.domain.Vgr;
import io.flowing.retail.vgr.messages.Message;
import io.flowing.retail.vgr.messages.MessageSender;
import io.flowing.retail.vgr.persistence.VgrRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MovePartsAdapter implements JavaDelegate {
  
  @Autowired
  private MessageSender messageSender;  

  @Autowired
  private VgrRepository vgrRepository;

  @Override
  public void execute(DelegateExecution context) throws Exception {
    String traceId = context.getProcessBusinessKey();
    System.out.println("Move parts for order-fulfillment process instance " + traceId);

    // Assuming some mechanism to determine these values based on the context or process state
    int active = 1; // Example value
    int code = 0; // Example value
    String description = ""; // Optional based on process state
    String target = "hbw"; // Example target
    Date now = new Date();

    // create MovePartsEventPayload
    MovePartsEventPayload movePartsEventPayload = new MovePartsEventPayload();
    movePartsEventPayload.setActive(active);
    movePartsEventPayload.setCode(code);
    movePartsEventPayload.setDescription(description);
    movePartsEventPayload.setTarget(target);
    movePartsEventPayload.setTs(now);

    // publish
    messageSender.send(new Message<MovePartsEventPayload>("f/i/state/vgr", traceId,
            movePartsEventPayload));
  }
  
}
