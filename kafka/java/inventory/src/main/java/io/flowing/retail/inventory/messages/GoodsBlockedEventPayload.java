package io.flowing.retail.inventory.messages;

import io.flowing.retail.inventory.domain.InventoryBlockedGoodsState;
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
