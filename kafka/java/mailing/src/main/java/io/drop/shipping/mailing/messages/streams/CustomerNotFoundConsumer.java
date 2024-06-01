package io.drop.shipping.mailing.messages.streams;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CustomerNotFoundConsumer {

    @KafkaListener(topics = "customer-not-found", groupId = "mailing-service-group")
    public void listen(ConsumerRecord<String, GenericRecord> record) {
        GenericRecord enrichedOrder = record.value();
        // Process the GenericRecord object
        System.out.println("Received EnrichedOrder: " + enrichedOrder);
        // Add your mailing service logic here
    }

}
