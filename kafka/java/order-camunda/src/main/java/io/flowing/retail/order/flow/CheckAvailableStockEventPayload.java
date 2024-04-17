package io.flowing.retail.order.flow;

import io.flowing.retail.order.domain.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

import io.flowing.retail.order.domain.Workpiece;
import lombok.Data;

@Data
public class CheckAvailableStockEventPayload {
    private String refId;
    private List<OrderItem> items;

    // Method to convert OrderItems to Workpieces
    /*
    public static List<Workpiece> convertToWorkpieces(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> new Workpiece(null, null, item.getArticleId()))  // Assuming ID and state are not required
                .collect(Collectors.toList());
    }

     */

}
