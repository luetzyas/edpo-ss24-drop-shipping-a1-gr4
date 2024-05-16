package io.flowing.retail.monitor.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SensorData {
    @SerializedName("aq")
    private double airQuality;
    @SerializedName("gr")
    private double gasResistance;
    @SerializedName("h")
    private double humidity;
    @SerializedName("iaq")
    private double indexedAirQuality;
    @SerializedName("p")
    private double airPressure;
    @SerializedName("rh")
    private double relativeHumidity;
    @SerializedName("rt")
    private double roomTemperature;
    @SerializedName("t")
    private double airTemperature;
    @SerializedName("ts")
    private String timestamp;
}
