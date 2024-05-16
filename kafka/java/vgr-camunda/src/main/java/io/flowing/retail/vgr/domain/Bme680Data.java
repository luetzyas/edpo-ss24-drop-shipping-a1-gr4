package io.flowing.retail.vgr.domain;

import lombok.Data;

@Data
public class Bme680Data {
    private double aq;
    private double gr;
    private double h;
    private double iaq;
    private double p;
    private double rh;
    private double rt;
    private double t;
    private String ts;
}
