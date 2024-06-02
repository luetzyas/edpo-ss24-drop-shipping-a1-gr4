package io.flowing.retail.order.messages.cloud;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.flowing.retail.order.domain.avro.EnrichedOrder;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.spin.plugin.variable.SpinValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.flowing.retail.order.domain.Order;
import io.flowing.retail.order.persistence.OrderRepository;

@Component
public class MessageListener {
  
  @Autowired
  private OrderRepository repository;
  @Autowired
  private RuntimeService runtimeService;
  @Autowired
  private ObjectMapper objectMapper;

  @Transactional
  public void goodsAvailableReceived(String messagePayloadJson) throws IOException {

    try {
      Message<AllGoodsAvailableEventPayload> message = objectMapper.readValue(messagePayloadJson,
              new TypeReference<Message<AllGoodsAvailableEventPayload>>() {
              }
      );

      AllGoodsAvailableEventPayload payload = message.getData();

      runtimeService.createMessageCorrelation("AllGoodsAvailableEvent")
              .processInstanceBusinessKey(message.getTraceid())
              .setVariable("available", "true")
              .correlateWithResult();

      System.out.println("AllGoodsAvailableEvent received: " + payload + " for " + message.getTraceid());
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  @Transactional
  public void customerRegisteredEventReceived(String messagePayloadJson) throws IOException {
    try {
      Message<CustomerRegisteredEventPayload> message = objectMapper.readValue(messagePayloadJson,
              new TypeReference<Message<CustomerRegisteredEventPayload>>() {
              }
      );

      CustomerRegisteredEventPayload payload = message.getData();
      System.out.println("CustomerRegisteredEvent received: " + payload);

      runtimeService.createMessageCorrelation("CustomerRegisteredEvent")
              .processInstanceBusinessKey(payload.getRefId())
              .setVariable("customer", payload.getCustomer())
              .correlateWithResult();

    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

  }

  @Transactional
  public void paymentReceivedEventReceived(String messagePayloadJson) throws IOException {
    try {
      Message<PaymentReceivedEventPayload> message = objectMapper.readValue(messagePayloadJson,
              new TypeReference<Message<PaymentReceivedEventPayload>>() {
              }
      );

      PaymentReceivedEventPayload payload = message.getData();
      System.out.println("PaymentReceivedEvent received: " + payload);

      runtimeService.createMessageCorrelation("PaymentReceivedEvent")
              .processInstanceBusinessKey(payload.getRefId())
              .setVariable("refId", payload.getRefId())
              .correlateWithResult();

    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }
  /**
   * Very generic listener for simplicity. It takes all events and checks, if a 
   * flow instance is interested. If yes, they are correlated, 
   * otherwise they are just discarded.
   *  
   * It might make more sense to handle each and every message type individually.
   */
  @Transactional
  @KafkaListener(id = "order", topics = "flowing-retail", containerFactory = "messageListenerContainerFactory")
  public void messageReceived(String messagePayloadJson, @Header("type") String messageType) throws Exception{
    if ("AllGoodsAvailableEvent".equals(messageType)) {
      System.out.println("AllGoodsAvailableEvent received");
      goodsAvailableReceived(messagePayloadJson);
    }
    if ("CustomerRegisteredEvent".equals(messageType)) {
      System.out.println("CustomerRegisteredEvent received");
      customerRegisteredEventReceived(messagePayloadJson);
    }
    if ("PaymentReceivedEvent".equals(messageType)) {
      System.out.println("PaymentReceivedEvent received");
      paymentReceivedEventReceived(messagePayloadJson);
    }

    Message<JsonNode> message = objectMapper.readValue( //
            messagePayloadJson, //
            new TypeReference<Message<JsonNode>>() {});

    long correlatingInstances = runtimeService.createExecutionQuery() //
            .messageEventSubscriptionName(message.getType()) //
            .processInstanceBusinessKey(message.getTraceid()) //
            .count();

    if (correlatingInstances==1) {
      System.out.println("Correlating " + message + " to waiting flow instance");

      runtimeService.createMessageCorrelation(message.getType())
              .processInstanceBusinessKey(message.getTraceid())
              .setVariable(//
                      "PAYLOAD_" + message.getType(), //
                      SpinValues.jsonValue(message.getData().toString()).create())//
              .correlateWithResult();
    }
  }

}
