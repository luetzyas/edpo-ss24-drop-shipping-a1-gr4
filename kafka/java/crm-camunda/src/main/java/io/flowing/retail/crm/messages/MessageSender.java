package io.flowing.retail.crm.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowing.retail.crm.domain.Customer;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Helper to send messages, currently nailed to Kafka, but could also send via AMQP (e.g. RabbitMQ) or
 * any other transport easily
 */
@Component
public class MessageSender {

  public static final String TOPIC_NAME = "customer";

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Bean
  public NewTopic autoCreateTopicOnStartupIfNotExistant() {
    return TopicBuilder.name(TOPIC_NAME).partitions(1).replicas(1).build();
  }

  public void sendMessage(Message<?> m) {
    try {
      // avoid too much magic and transform ourselves
      String jsonMessage = objectMapper.writeValueAsString(m);

      // wrap into a proper message for Kafka including a header
      ProducerRecord<String, String> record = new ProducerRecord<String, String>("flowing-retail", jsonMessage);
      record.headers().add("type", m.getType().getBytes());

      System.out.println("CRM Sending message: " + jsonMessage + " with type " + m.getType());
      // and send it
      kafkaTemplate.send(record);
    } catch (Exception e) {
      throw new RuntimeException("Could not transform and send message: "+ e.getMessage(), e);
    }
  }


  public void send(Customer customer) {
    try {
      String jsonMessage = objectMapper.writeValueAsString(customer);

      ProducerRecord<String, String> record = new ProducerRecord<String, String>(TOPIC_NAME, customer.getEmail(), jsonMessage);
      record.headers().add("type", customer.getEmail().getBytes());

      // print for debugging
      System.out.println("CRM Sending message: " + record);

      // and send it
      kafkaTemplate.send(record);
    } catch (Exception e) {
      throw new RuntimeException("Could not transform and send message: "+ e.getMessage(), e);
    }
  }
}
