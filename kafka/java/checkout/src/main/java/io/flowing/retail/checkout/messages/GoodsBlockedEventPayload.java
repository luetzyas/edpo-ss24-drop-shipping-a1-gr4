package io.flowing.retail.checkout.messages;

import io.flowing.retail.checkout.domain.InventoryBlockedGoodsState;
import io.flowing.retail.checkout.domain.InventoryStockState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsBlockedEventPayload {

    private Map<String, InventoryBlockedGoodsState> blockedGoodsStateMap;

}
