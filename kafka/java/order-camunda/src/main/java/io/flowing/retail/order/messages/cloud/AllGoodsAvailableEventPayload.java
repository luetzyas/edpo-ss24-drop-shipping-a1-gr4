package io.flowing.retail.order.messages.cloud;

import io.flowing.retail.order.domain.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllGoodsAvailableEventPayload {

    private String refId;
    private List<OrderItem> items;

}
