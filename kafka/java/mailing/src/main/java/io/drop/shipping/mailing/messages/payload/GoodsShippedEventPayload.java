package io.drop.shipping.mailing.messages.payload;

public class GoodsShippedEventPayload {

    /**
     * {
     *      "refId":"checkout-generated-83873085-daa7-4922-bcad-55c0e2437e75",
     *      "shipmentId":"5b4ff7e7-c029-467c-9c93-3dd63a97830a"
     * }
     */



    private String refId;
    private String shipmentId;

    public String getRefId() {
        return refId;
    }

    public GoodsShippedEventPayload setRefId(String refId) {
        this.refId = refId;
        return this;
    }


    public String getShipmentId() {
        return shipmentId;
    }

    public GoodsShippedEventPayload setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
        return this;
    }


}
