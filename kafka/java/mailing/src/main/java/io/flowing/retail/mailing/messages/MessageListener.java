package io.flowing.retail.mailing.messages;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.flowing.retail.mailing.application.MailingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    @Autowired
    private MessageSender messageSender;
    @Autowired
    private MailingService mailingService;

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule()); // Register JavaTimeModule ISO-8601 compliant format


    @KafkaListener(id = "mailing", topics = "flowing-retail")
    public void orderCompletedEventReceived(String messageJson, @Header("type") String messageType) throws Exception {
        if ("OrderCompletedEvent".equals(messageType)) {
            System.out.println("Received new message");

            try {
                Message<JsonNode> message = objectMapper.readValue(messageJson, new TypeReference<Message<JsonNode>>(){});
                ObjectNode data = (ObjectNode) message.getData();

                //TODO we need a java object GoodsFetchedEventPayload or similar to map the data to it
                /**
                ObjectNode payload = (ObjectNode) message.getData();
                GoodsFetchedEventPayload payloadEvent = objectMapper
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                        .treeToValue(payload, GoodsFetchedEventPayload.class);
                **/


                System.out.println("Received new message: " + data);

                mailingService.sendMail(messageType, String.valueOf(data), "Franz Kafka");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    //TODO: Add more methods for other message types
    //TOdO: enhance readMe to explain the process and how it needs to be run
}
