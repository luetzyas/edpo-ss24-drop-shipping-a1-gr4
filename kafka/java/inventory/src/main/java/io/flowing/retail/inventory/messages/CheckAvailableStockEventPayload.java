package io.flowing.retail.inventory.messages;

import io.flowing.retail.inventory.domain.InventoryUpdateMessage;
import io.flowing.retail.inventory.domain.Item;
import io.flowing.retail.inventory.domain.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class CheckAvailableStockEventPayload {
    private String refId;
    private List<OrderItem> items;

}
