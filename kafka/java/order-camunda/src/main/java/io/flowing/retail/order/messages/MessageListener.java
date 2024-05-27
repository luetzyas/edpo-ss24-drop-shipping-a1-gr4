package io.flowing.retail.order.messages;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.spin.plugin.variable.SpinValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
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
  
  /**
   * Handles incoming OrderPlacedEvents. 
   *
   */
  @Transactional
  public void orderPlacedReceived(Message<Order> message) throws JsonParseException, JsonMappingException, IOException {
    Order order = message.getData();
    System.out.println("OrderPlacedEvent received full message: " + message);
    System.out.println("New order placed, start flow. Order object: " + order);
    
    // persist domain entity
    repository.save(order);    
    try {
      // and kick of a new flow instance
      runtimeService.createMessageCorrelation(message.getType())
              .processInstanceBusinessKey(message.getTraceid())
              .setVariable("orderId", order.getId())
              .setVariable("customer", SpinValues.jsonValue(objectMapper.writeValueAsString(order.getCustomer())).create())
              .setVariable("items", SpinValues.jsonValue(objectMapper.writeValueAsString(order.getItems())).create())
              .correlateWithResult();
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialize order items", e);
    }

  }

  @Transactional
  public void goodsAvailableReceived(String messagePayloadJson) throws IOException {

    Message<AllGoodsAvailableEventPayload> message = objectMapper.readValue(messagePayloadJson,
            new TypeReference<Message<AllGoodsAvailableEventPayload>>() {
            }
    );

    AllGoodsAvailableEventPayload payload = message.getData();
    boolean available = true;



    runtimeService.createMessageCorrelation("AllGoodsAvailableEvent")
            .processInstanceBusinessKey(message.getTraceid())
            .setVariable("available", available)
            .correlateWithResult();
  }


  /**
   * Very generic listener for simplicity. It takes all events and checks, if a 
   * flow instance is interested. If yes, they are correlated, 
   * otherwise they are just discarded.
   *  
   * It might make more sense to handle each and every message type individually.
   */
  @Transactional
  @KafkaListener(id = "order", topics = MessageSender.TOPIC_NAME)
  public void messageReceived(String messagePayloadJson, @Header("type") String messageType) throws Exception{
    if ("OrderPlacedEvent".equals(messageType)) {
        System.out.println("OrderPlacedEvent received");
      orderPlacedReceived(objectMapper.readValue(messagePayloadJson, new TypeReference<Message<Order>>() {}));
    }
    if ("AllGoodsAvailableEvent".equals(messageType)) {
      System.out.println("AllGoodsAvailableEvent received");
      goodsAvailableReceived(messagePayloadJson);
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
