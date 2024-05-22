package io.flowing.retail.crm.messages;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowing.retail.crm.application.CrmService;
import io.flowing.retail.crm.domain.db.Customer;
import io.flowing.retail.crm.persistence.CrmRepository;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MessageListener {

  @Autowired
  private CrmRepository repository;
  @Autowired
  private RuntimeService runtimeService;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private CrmService crmService;

  /**
   * Very generic listener for simplicity. It takes all events and checks, if a 
   * flow instance is interested. If yes, they are correlated, 
   * otherwise they are just discarded.
   *  
   * It might make more sense to handle each and every message type individually.
   */
  @Transactional
  @KafkaListener(id = "crm", topics = MessageSender.TOPIC_NAME)
  public void newCustomerCommand(String messagePayloadJson, @Header("type") String messageType) throws Exception{
    // TODO: add command when customer is updated form
    if ("notDoneYet".equals(messageType)) {
        crmService.processNewCustomer(objectMapper.readValue(messagePayloadJson, new TypeReference<Message<Customer>>() {}).getData());
    }

  }

}
