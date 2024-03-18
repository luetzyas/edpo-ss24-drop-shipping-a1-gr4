package io.drop.shipping.mailing.messages;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.drop.shipping.mailing.application.MailingService;
import io.drop.shipping.mailing.messages.payload.GoodsShippedEventPayload;
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


    @KafkaListener(id = "mailing", topics = "flowing-retail")
    public void handleEvent(String messageJson, @Header("type") String messageType) throws Exception {
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

    private void sendMailForOrderShippedEvent(String messageJson, String messageType) throws Exception {
        try {
            Message<GoodsShippedEventPayload> message = objectMapper.readValue(messageJson, new TypeReference<Message<GoodsShippedEventPayload>>() {});
            GoodsShippedEventPayload eventPayload = message.getData();

            String emailSubject = "Order Status Update";
            String emailContent = "The order with ID " + eventPayload.getShipmentId() + " has been shipped.";
            //String recipient = eventPayload.getOrder().getCustomer(); // This should be replaced with the actual recipient from the payload

            mailingService.sendMail(emailSubject, emailContent);
            System.out.println("Email sent for " + messageType + ": " + emailContent);
        } catch (Exception e) {
            System.out.println("Error " + Thread.currentThread().getStackTrace() + e);
        }
    }

}
