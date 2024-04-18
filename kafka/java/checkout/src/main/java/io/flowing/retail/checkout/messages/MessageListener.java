package io.flowing.retail.checkout.messages;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowing.retail.checkout.application.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class MessageListener {

    @Autowired
    private CheckoutService checkoutService;
    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    @KafkaListener(id = "checkout", topics = "flowing-retail")
    public void messageReceived(String messagePayloadJson, @Header("type") String messageType) throws Exception{
        if ("InventoryUpdatedEvent".equals(messageType)) {
            System.out.println("Received InventoryUpdatedEvent");
            try {
                Message<InventoryUpdatedEventPayload> message = objectMapper.readValue(messagePayloadJson, new TypeReference<Message<InventoryUpdatedEventPayload>>() {
                });
                InventoryUpdatedEventPayload inventoryUpdatedEvent = message.getData();
                checkoutService.updateInventory(inventoryUpdatedEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
