package io.flowing.retail.inventory.connector;

import io.flowing.retail.inventory.messages.InMemoryOutbox;
import io.flowing.retail.inventory.messages.Message;
import io.flowing.retail.inventory.messages.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OutboxPoller {
    @Autowired
    private MessageSender messageSender;

    @Autowired
    private InMemoryOutbox outbox;

    @Scheduled(fixedDelay = 1000) // Poll every second
    public void pollOutbox() {
        Message<?> message;
        while ((message = outbox.pollMessage()) != null) {
            try {
                messageSender.send(message);
                System.out.println("Outbox Poller: Successfully sent message from outbox: " + message);
            } catch (Exception e) {
                System.err.println("Outbox Poller: Failed to send message, will retry: " + e.getMessage());
                // re-add to the queue
                outbox.addToOutbox(message);
            }
        }
    }
}

