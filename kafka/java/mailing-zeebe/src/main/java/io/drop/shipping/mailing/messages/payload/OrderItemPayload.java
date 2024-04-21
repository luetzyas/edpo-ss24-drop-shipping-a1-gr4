package io.drop.shipping.mailing.messages.payload;

import lombok.Data;

@Data
public class OrderItemPayload {

    /**
     * {
     *      "articleId":"article1",
     *      "amount":5
     * }
     */

    private String articleId;
    private int amount;

}
