package io.drop.shipping.mailing.messages.payload;

import lombok.Data;

@Data
public class PaymentReceivedEventPayload {

  /**
   * {
   *       "refId":"checkout-generated-882dbbea-8a06-4085-b451-1833e5e1809a",
   *       "reason":null,
   *       "amount":15
   *    }
   */

  private String refId;
  private String reason;
  private int amount;

}
