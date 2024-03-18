package io.drop.shipping.mailing.messages;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.drop.shipping.mailing.application.MailingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    @Autowired
    private MailingService mailingService;

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule()); // Register JavaTimeModule ISO-8601 compliant format

    //TODO: Add more methods for other message types
    //TOdO: enhance readMe to explain the process and how it needs to be run
    @KafkaListener(id = "mailing", topics = "flowing-retail")
    public void handleEvents(String messageJson, @Header("type") String messageType) throws Exception {
        System.out.println("Event: " + messageType);
        System.out.println("JSON: " + messageJson);

        try {
            switch (messageType) {
                case "OrderPlacedEvent": //step 1
                    //sendMailForOrderPlacedEvent(messageJson, messageType);
                    break;
                case "PaymentReceivedEvent": //step 2
                    //sendMailForPaymentReceivedEvent(messageJson, messageType);
                    break;
                case "OrderShippedEvent": //step 4 >> GoodsShippedEvent
                    //sendMailForOrderShippedEvent(messageJson, messageType);
                    break;
                case "OrderCompletedEvent": //step 5
                    //sendMailForOrderCompletedEvent(messageJson, messageType);
                    break;
                default:
                    System.out.println("Received unsupported event type: " + messageType);
            }
        } catch (Exception e) {
            System.out.println("Error processing message" + e);
        }
    }
}
