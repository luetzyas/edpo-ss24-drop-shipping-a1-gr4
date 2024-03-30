package io.flowing.retail.order.flow;

import io.flowing.retail.order.domain.OrderItem;

import java.util.List;
import lombok.Data;

@Data
public class CheckAvailableStockEventPayload {
    private String refId;
    private List<OrderItem> items;

}
