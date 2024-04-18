package io.flowing.retail.checkout.application;

import io.flowing.retail.checkout.domain.FactoryStockState;
import io.flowing.retail.checkout.messages.InventoryUpdatedEventPayload;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CheckoutService {
    private final Map<String, FactoryStockState> stockStateMap = new ConcurrentHashMap<>();

    public void updateInventory(InventoryUpdatedEventPayload payload) {
        Map<String, FactoryStockState> receivedStockState = payload.getStockDetails();
        stockStateMap.clear();
        // Populate the map with the new states
        stockStateMap.putAll(receivedStockState);
        System.out.println("CheckoutService: Updated Inventory replica");
    }

    public Map<String, FactoryStockState> getCurrentStockState() {
        return new ConcurrentHashMap<>(stockStateMap);
    }
}
