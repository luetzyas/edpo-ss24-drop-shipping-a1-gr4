package io.drop.shipping.mailing.messages.payload;

public class VgrPayload {

    /**
     * {
     *      "traceId":"8bf98fee-7375-4ca4-b8e4-b4ee526b050c"
     * }
     */

    private String traceId;

    public String getTraceId() {
        return traceId;
    }

    public VgrPayload setTraceId(String refId) {
        this.traceId = refId;
        return this;
    }

}
