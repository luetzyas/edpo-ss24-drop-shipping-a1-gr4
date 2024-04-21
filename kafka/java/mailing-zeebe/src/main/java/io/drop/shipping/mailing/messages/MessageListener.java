package io.drop.shipping.mailing.messages;

import io.camunda.zeebe.client.ZeebeClient;
import io.drop.shipping.mailing.application.MailingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component("mailing-zeebe-messageListener")
public class MessageListener {

    @Autowired
    private MailingService mailingService;

    @KafkaListener(id = "mailing", topics = "flowing-retail")
    public void handleEvent(String messageJson, @Header("type") String messageType) throws Exception {
        try {
            switch (messageType) {
                case "OrderPlacedEvent":
                    mailingService.sendMailForOrderPlacedEvent(messageJson, messageType);
                    break;
                case "PaymentReceivedEvent":
                    mailingService.sendMailForPaymentReceivedEvent(messageJson, messageType);
                    break;
                case "GoodsShippedEvent":
                    mailingService.sendEmailForGoodsShippedEvent(messageJson, messageType);
                    break;
                case "OrderCompletegit log --all -- [file path]git log --all -- [file path]git dEvent":
                    mailingService.sendMailForOrderCompetedEvent(messageJson, messageType);
                    break;
                case "VgrFinishedEvent":
                    mailingService.sendMailForVgrFinishedEvent(messageJson, messageType);
                    break;
                default:
                    System.out.println("Eventtype: '" + messageType + "' is not handled");
            }
        } catch (Exception e) {
            System.out.println("Error processing message" + e);
        }
    }
}
