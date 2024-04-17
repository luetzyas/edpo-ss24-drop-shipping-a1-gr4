package io.flowing.retail.inventory.messages;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class InMemoryOutbox {
    private static final ConcurrentLinkedQueue<Message<?>> outbox = new ConcurrentLinkedQueue<>();

    public static void addToOutbox(Message<?> message) {
        outbox.add(message);
    }

    public static Message<?> pollMessage() {
        return outbox.poll(); // This retrieves and removes the head of the queue, or returns null if empty
    }
}

