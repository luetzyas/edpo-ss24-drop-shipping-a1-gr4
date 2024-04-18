package io.flowing.retail.checkout.messages;

import io.flowing.retail.checkout.domain.FactoryStockState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryUpdatedEventPayload {

    private Map<String, FactoryStockState> stockDetails;

}
