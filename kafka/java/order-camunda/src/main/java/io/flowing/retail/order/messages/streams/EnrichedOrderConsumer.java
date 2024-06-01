package io.flowing.retail.order.messages.streams;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowing.retail.order.domain.OrderDataToAvroMapper;
import io.flowing.retail.order.domain.avro.EnrichedOrder;
import io.flowing.retail.order.persistence.OrderRepository;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.spin.plugin.variable.SpinValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class EnrichedOrderConsumer {

    @Autowired
    private OrderRepository repository;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "enriched-order", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(EnrichedOrder enrichedOrder) {
        // Process the EnrichedOrder
        System.out.println("Received EnrichedOrder: " + enrichedOrder);

        // Extract the order from the EnrichedOrder
        io.flowing.retail.order.domain.Order order = OrderDataToAvroMapper.convertToOrder(enrichedOrder);

        // Check if the order is already processed
        if (!repository.existsById(order.getId())) {
            // Persist the order if it's new
            repository.save(order);
            System.out.println("New order placed, start flow. Order object: " + order);

            try {
                // Kick off a new business process
                runtimeService.createMessageCorrelation("OrderPlacedEvent")
                        .processInstanceBusinessKey(order.getId())
                        .setVariable("orderId", order.getId())
                        .setVariable("items", SpinValues.jsonValue(objectMapper.writeValueAsString(order.getItems())).create())
                        .correlateWithResult();
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize order items", e);
            }
        } else {
            System.out.println("Order already processed: " + order.getId());
        }
    }
}
