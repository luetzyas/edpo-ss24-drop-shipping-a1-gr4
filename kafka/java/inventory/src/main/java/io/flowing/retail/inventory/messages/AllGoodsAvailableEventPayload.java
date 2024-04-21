package io.flowing.retail.inventory.messages;

import io.flowing.retail.inventory.domain.InventoryBlockedGoodsState;
import io.flowing.retail.inventory.domain.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllGoodsAvailableEventPayload {

    private String refId;
    private List<OrderItem> items;

}
