package io.flowing.retail.vgr.flow;

public class TriggerVgrCommandPayload {
  
  private String refId;
  private String reason;

  public String getRefId() {
    return refId;
  }
  public TriggerVgrCommandPayload setRefId(String refId) {
    this.refId = refId;
    return this;
  }
  public String getReason() {
    return reason;
  }

  public TriggerVgrCommandPayload setReason(String reason) {
    this.reason = reason;
    return this;
  }

}
