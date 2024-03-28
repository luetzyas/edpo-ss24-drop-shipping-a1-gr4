package io.drop.shipping.mailing.messages.payload;

import lombok.Data;

@Data
public class OrderCompletedEventPayload {

    /**
     * {
     *      "orderId":"checkout-generated-41b6a349-6020-486d-8eab-ccad029b9cde"
     * }
     */

    private String orderId;

}
