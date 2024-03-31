package io.flowing.retail.inventory.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class GoodsAvailableEventPayload {
    private String refId; // Reference ID to correlate with the check stock event
    private boolean available; // Indicates if all requested items are available
    private List<ItemAvailability> availableItems; // List of available items with details
    private List<ItemAvailability> unavailableItems; // List of unavailable items with details

    @Data
    @AllArgsConstructor
    public static class ItemAvailability {
        private String itemId;
        private int requestedQuantity;
        private int availableQuantity; // This can be useful to indicate partial availability
    }
}
