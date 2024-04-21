package io.flowing.retail.inventory.messages;

import io.flowing.retail.inventory.domain.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class ReserveStockItemsCommandPayload {
    private String refId;
    private List<OrderItem> items;

}
