package io.flowing.retail.vgr.domain;

import lombok.Data;

@Data
public class FactoryOrderState {
    private String state;
    private String ts; // timestamp
    private String type;
    private String topic;
}
