package io.drop.shipping.mailing.messages.payload;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderPlacedEventPayload {

    /**
     * {
     *      "orderId":"checkout-generated-83873085-daa7-4922-bcad-55c0e2437e75",
     *      "customer":{
     *                  "name":"Camunda",
     *                  "address":"Zossener Strasse 55\n10961 Berlin\nGermany"
     *                  },
     *       "items":[
     *          {
     *             "articleId":"article1",
     *             "amount":5
     *          },
     *          {
     *             "articleId":"article2",
     *             "amount":10
     *          }
     *       ]
     * }
     */

    private String orderId;
    private CustomerPayload customer;
    private List<OrderItemPayload> items = new ArrayList<>();
}
