package io.flowing.retail.vgr.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * Helper to send messages, currently nailed to Kafka, but could also send via AMQP (e.g. RabbitMQ) or
 * any other transport easily
 */
@Component
public class MessageSender {

  public static final String TOPIC_NAME = "flowing-retail";

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Bean
  public NewTopic autoCreateTopicOnStartupIfNotExistant() {
    return TopicBuilder.name(TOPIC_NAME).partitions(1).replicas(1).build();
  }

  public void send(Message<?> m) {
    try {
      // avoid too much magic and transform ourselves
      String jsonMessage = objectMapper.writeValueAsString(m);

      // wrap into a proper message for Kafka including a header
      ProducerRecord<String, String> record = new ProducerRecord<String, String>("flowing-retail", jsonMessage);
      record.headers().add("type", m.getType().getBytes());

      // and send it
      kafkaTemplate.send(record);
    } catch (Exception e) {
      throw new RuntimeException("Could not transform and send message: "+ e.getMessage(), e);
    }
  }
  public void sendToKafka(String topic, String payload, String type) {
    try {
      ProducerRecord<String, String> record = new ProducerRecord<>(topic, payload);
      if (type != null) {
        record.headers().add("type", type.getBytes(StandardCharsets.UTF_8));
      }
      kafkaTemplate.send(record);
      System.out.println("Sent message to Kafka topic: " + topic + ", Payload: " + payload);
    } catch (Exception e) {
      throw new RuntimeException("Could not send message to Kafka: " + e.getMessage(), e);
    }
  }

}
