package io.flowing.retail.inventory.messages;

import io.flowing.retail.inventory.domain.FactoryStockState;
import io.flowing.retail.inventory.domain.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryUpdatedEventPayload {

    private Map<String, FactoryStockState> stockDetails;

}
