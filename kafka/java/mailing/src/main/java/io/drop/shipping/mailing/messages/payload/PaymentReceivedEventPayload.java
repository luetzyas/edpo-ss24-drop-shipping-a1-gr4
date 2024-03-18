package io.drop.shipping.mailing.messages.payload;

public class PaymentReceivedEventPayload {
  private String refId;

  public String getRefId() {
    return refId;
  }

  public PaymentReceivedEventPayload setRefId(String refId) {
    this.refId = refId;
    return this;
  }
}
