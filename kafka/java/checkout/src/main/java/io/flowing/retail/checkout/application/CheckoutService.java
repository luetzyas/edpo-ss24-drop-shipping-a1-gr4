package io.flowing.retail.checkout.application;

import io.flowing.retail.checkout.domain.InventoryBlockedGoodsState;
import io.flowing.retail.checkout.domain.InventoryStockState;
import io.flowing.retail.checkout.messages.GoodsBlockedEventPayload;
import io.flowing.retail.checkout.messages.InventoryUpdatedEventPayload;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CheckoutService {
    private final Map<String, InventoryStockState> stockStateMap = new ConcurrentHashMap<>();

    private final Map<String, InventoryBlockedGoodsState> blockedGoodsMap  = new ConcurrentHashMap<>();


    public void updateInventory(InventoryUpdatedEventPayload payload) {
        Map<String, InventoryStockState> receivedStockState = payload.getStockDetails();
        stockStateMap.clear();
        // Populate the map with the new states
        stockStateMap.putAll(receivedStockState);
        System.out.println("CheckoutService: Updated Inventory replica");
    }

    public Map<String, InventoryStockState> getCurrentStockState() {
        return new ConcurrentHashMap<>(stockStateMap);
    }

    public void updateBlockedGoods(GoodsBlockedEventPayload goodsBlockedEvent) {
        Map<String, InventoryBlockedGoodsState> receivedBlockedGoods = goodsBlockedEvent.getBlockedGoodsStateMap();
        blockedGoodsMap.clear();
        // Populate the map with the new states
        blockedGoodsMap.putAll(receivedBlockedGoods);
        System.out.println("CheckoutService: Updated Blocked Goods replica");
    }

    public Map<String, InventoryBlockedGoodsState> getCurrentBlockedGoods() {
        return new ConcurrentHashMap<>(blockedGoodsMap);
    }
}
