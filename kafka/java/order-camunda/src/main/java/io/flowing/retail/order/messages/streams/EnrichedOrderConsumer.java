package io.flowing.retail.order.messages.streams;

import io.flowing.retail.order.domain.avro.EnrichedOrder;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EnrichedOrderConsumer {

    @KafkaListener(topics = "enriched-order", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(EnrichedOrder enrichedOrder) {
        // Process the EnrichedOrder
        System.out.println("Received EnrichedOrder: " + enrichedOrder);
        // Add your business logic here
    }
}
