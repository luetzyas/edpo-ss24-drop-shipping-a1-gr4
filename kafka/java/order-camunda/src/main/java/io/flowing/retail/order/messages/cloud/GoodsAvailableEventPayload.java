package io.flowing.retail.order.messages.cloud;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class GoodsAvailableEventPayload implements Serializable {
    private String refId; // Reference ID to correlate with the check stock event
    private boolean available; // Indicates if all requested items are available
    private List<ItemAvailability> availableItems; // List of available items with details
    private List<ItemAvailability> unavailableItems; // List of unavailable items with details

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemAvailability implements Serializable {
        private String itemId;
        private int requestedQuantity;
        private int availableQuantity; // This can be useful to indicate partial availability
    }
}
