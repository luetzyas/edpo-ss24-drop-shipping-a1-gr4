package io.drop.shipping.mailing.messages.payload;

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

  public String getRefId() {
    return refId;
  }

  public PaymentReceivedEventPayload setRefId(String refId) {
    this.refId = refId;
    return this;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }
}
