package io.drop.shipping.mailing.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.drop.shipping.mailing.messages.cloud.Message;
import io.drop.shipping.mailing.messages.payload.*;
import org.apache.avro.generic.GenericRecord;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MailingService {

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule()); // Register JavaTimeModule ISO-8601 compliant format

    private void sendMail(String subject, String body, String recipient) {
        System.out.println("Mail sent to " + recipient + " with subject '" + subject + "' and body '" + body + "'");
    }

    public void sendEmailForGoodsShippedEvent(String messageJson, String messageType) throws Exception {
        try {
            Message<GoodsShippedEventPayload> message = objectMapper.readValue(messageJson, new TypeReference<Message<GoodsShippedEventPayload>>() {});
            GoodsShippedEventPayload eventPayload = message.getData();

            String emailSubject = messageType;
            String emailContent = "The order with Ref ID: " + eventPayload.getRefId() + " has been shipped.";
            String recipient = "EDPO Gruppe 4"; // TODO: implement Customer on ShippedEvent

            sendMail(emailSubject, emailContent, recipient);

        } catch (Exception e) {
            System.out.println("Error " + Thread.currentThread().getStackTrace() + e);
        }
    }

    public void sendMailForOrderPlacedEvent(String messageJson, String messageType) throws Exception {
        try {
            Message<OrderPlacedEventPayload> message = objectMapper.readValue(messageJson, new TypeReference<Message<OrderPlacedEventPayload>>() {});
            OrderPlacedEventPayload eventPayload = message.getData();

            // Extracting article IDs from the order items
            List<String> articleIds = eventPayload.getItems().stream()
                    .map(item -> item.getArticleId()) // Assuming there is a getArticleId method in OrderItem
                    .collect(Collectors.toList());
            String itemsString = String.join(", ", articleIds); // Joining all IDs with comma separation

            String emailSubject = messageType;
            String emailContent = "The order with Order ID: " + eventPayload.getOrderId() + "\n with items: " + itemsString + " \n has been shipped.";
            String recipient = eventPayload.getCustomer().getName(); // This should be replaced with the actual recipient from the payload

            sendMail(emailSubject, emailContent, recipient);

        } catch (Exception e) {
            System.out.println("Error " + Thread.currentThread().getStackTrace() + e);
        }
    }

    public void sendMailForPaymentReceivedEvent(String messageJson, String messageType) throws Exception {
        try {
            Message<PaymentReceivedEventPayload> message = objectMapper.readValue(messageJson, new TypeReference<Message<PaymentReceivedEventPayload>>() {});
            PaymentReceivedEventPayload eventPayload = message.getData();

            String emailSubject = messageType;
            String emailContent = "The payment with Ref ID: " + eventPayload.getRefId() + "\n reason: " + eventPayload.getReason() + "\n amount: " + eventPayload.getAmount() + "\n has been payed.";
            String recipient = "EDPO Gruppe 4"; // TODO: implement Customer on PaymentEvent

            sendMail(emailSubject, emailContent, recipient);

        } catch (Exception e) {
            System.out.println("Error " + Thread.currentThread().getStackTrace() + e);
        }
    }

    public void sendMailForVgrFinishedEvent(String messageJson, String messageType) throws Exception {
        try {
            Message<VgrPayload> message = objectMapper.readValue(messageJson, new TypeReference<Message<VgrPayload>>() {});
            VgrPayload eventPayload = message.getData();

            String emailSubject = messageType;
            String emailContent = "The Vgr with Ref Id: " + eventPayload.getTraceId() + " has been finished.";
            String recipient = "EDPO Gruppe 4 VGR"; // TODO: implement Customer on PaymentEvent

            sendMail(emailSubject, emailContent, recipient);

        } catch (Exception e) {
            System.out.println("Error " + Thread.currentThread().getStackTrace() + e);
        }
    }
    public void sendMailForOrderCompetedEvent(String messageJson, String messageType) throws Exception {
        try {
            Message<OrderCompletedEventPayload> message = objectMapper.readValue(messageJson, new TypeReference<Message<OrderCompletedEventPayload>>() {});
            OrderCompletedEventPayload eventPayload = message.getData();

            String emailSubject = messageType;
            String emailContent = "The order with Ref ID: " + eventPayload.getOrderId() + " has been completed.";
            String recipient = "EDPO Gruppe 4"; // TODO: implement Customer on OrderCompletedEvent

            sendMail(emailSubject, emailContent, recipient);

        } catch (Exception e) {
            System.out.println("Error " + Thread.currentThread().getStackTrace() + e);
        }
    }

    public void sendMailForCustomerNotFoundEvent(GenericRecord enrichedOrder) {
        // Extracting the order ID from the enriched order
        String orderId = enrichedOrder.get("orderId").toString();
        String email = enrichedOrder.get("email").toString();

        String emailSubject = "Please register to complete your order";
        String emailContent = "The order with Order ID: " + orderId + " has been placed, but customer with email: "+email+" is not registered. Waiting for registration. Please sign up here: http://localhost:8098/";
        String recipient = email;

        sendMail(emailSubject, emailContent, recipient);

    }
}
