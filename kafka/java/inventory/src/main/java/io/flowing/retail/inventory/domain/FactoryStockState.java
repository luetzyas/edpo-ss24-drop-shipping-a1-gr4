package io.flowing.retail.inventory.domain;

import lombok.Data;

import java.util.*;

@Data
public class FactoryStockState {

      private String articleId;
      private int amount;

    // Helper method to transform a list of Workpiece objects into a list of FactoryStockState objects
    public static List<FactoryStockState> fromWorkpieces(List<InventoryUpdateMessage.Workpiece> workpieces) {
        Map<String, FactoryStockState> stockStateMap = new HashMap<>();

        for (InventoryUpdateMessage.Workpiece workpiece : workpieces) {
            if (workpiece != null && workpiece.getType() != null) {
                stockStateMap.computeIfAbsent(workpiece.getType(), k -> new FactoryStockState(k, 0))
                        .incrementAmount();
            }
        }

        return new ArrayList<>(stockStateMap.values());
    }

    public FactoryStockState(String articleId, int amount) {
        this.articleId = articleId;
        this.amount = amount;
    }

    public void incrementAmount() {
        this.amount++;
    }
}
