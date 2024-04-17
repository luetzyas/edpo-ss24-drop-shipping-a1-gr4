package io.flowing.retail.inventory.messages;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class IdempotentReceiver {
    private Set<String> processedIds = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public boolean isDuplicate(String refId) {
        return !processedIds.add(refId);
    }
}
