package io.flowing.retail.vgr.messages;


import io.flowing.retail.vgr.flow.TriggerVgrCommandPayload;
import org.camunda.bpm.engine.RuntimeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowing.retail.vgr.persistence.VgrRepository;

@Component
public class MessageListener {
  
  @Autowired
  private VgrRepository repository;


  @Autowired
  private RuntimeService runtimeService;

  @Autowired
  private ObjectMapper objectMapper;
  
  /**
   * Listener for TriggerVgrCommand from Order-fulfillment-flow. Starts the VGR process.
   * @param messagePayloadJson
   * @param messageType
   * @throws Exception
   */
  @Transactional
  @KafkaListener(id = "vgr", topics = MessageSender.TOPIC_NAME)
  public void messageReceived(String messagePayloadJson, @Header("type") String messageType) throws Exception {
    try {

      if (!"TriggerVgrCommand".equals(messageType)) {
        System.out.println("Ignoring message of type " + messageType);
        return;
      }

    Message<TriggerVgrCommandPayload> message = objectMapper.readValue(messagePayloadJson, new TypeReference<Message<TriggerVgrCommandPayload>>() {
    });
    TriggerVgrCommandPayload triggerVgrCommandPayload = message.getData();
    System.out.println("Trigger VGR: " + triggerVgrCommandPayload.getRefId());

    runtimeService.createMessageCorrelation(message.getType()) //
            .processInstanceBusinessKey(message.getTraceid())
            .setVariable("refId", triggerVgrCommandPayload.getRefId()) //
            .setVariable("correlationId", message.getCorrelationid()) //
            .correlateWithResult();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
