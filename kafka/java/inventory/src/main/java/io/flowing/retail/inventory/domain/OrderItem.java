package io.flowing.retail.inventory.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItem {
    private String articleId;
    private int amount;

    public OrderItem(String articleId, int amount) {
        this.articleId = articleId;
        this.amount = amount;
    }
}