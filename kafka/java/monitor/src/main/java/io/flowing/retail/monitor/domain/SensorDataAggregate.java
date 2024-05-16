package io.flowing.retail.monitor.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class SensorDataAggregate {
    private double totalAirQuality;
    private double totalGasResistance;
    private double totalHumidity;
    private double totalIndexedAirQuality;
    private double totalAirPressure;
    private double totalRelativeHumidity;
    private double totalRoomTemperature;
    private double totalAirTemperature;
    private int count;

    public SensorDataAggregate() {
        this.count = 0;
    }

    public SensorDataAggregate add(SensorData data) {
        try {
            this.totalAirQuality += data.getAirQuality();
            this.totalGasResistance += data.getGasResistance();
            this.totalHumidity += data.getHumidity();
            this.totalIndexedAirQuality += data.getIndexedAirQuality();
            this.totalAirPressure += data.getAirPressure();
            this.totalRelativeHumidity += data.getRelativeHumidity();
            this.totalRoomTemperature += data.getRoomTemperature();
            this.totalAirTemperature += data.getAirTemperature();
            this.count++;
            return this;
        } catch (Exception e) {
            throw new RuntimeException("Error adding sensor data to aggregate", e);
        }

    }

    public double getAverageAirQuality() {
        return count == 0 ? 0 : totalAirQuality / count;
    }

    public double getAverageGasResistance() {
        return count == 0 ? 0 : totalGasResistance / count;
    }

    public double getAverageHumidity() {
        return count == 0 ? 0 : totalHumidity / count;
    }

    public double getAverageIndexedAirQuality() {
        return count == 0 ? 0 : totalIndexedAirQuality / count;
    }

    public double getAverageAirPressure() {
        return count == 0 ? 0 : totalAirPressure / count;
    }

    public double getAverageRelativeHumidity() {
        return count == 0 ? 0 : totalRelativeHumidity / count;
    }

    public double getAverageRoomTemperature() {
        return count == 0 ? 0 : totalRoomTemperature / count;
    }

    public double getAverageAirTemperature() {
        return count == 0 ? 0 : totalAirTemperature / count;
    }

    @Override
    public String toString() {
        return "SensorDataAggregate{" +
                "averageAirQuality=" + getAverageAirQuality() +
                ", averageGasResistance=" + getAverageGasResistance() +
                ", averageHumidity=" + getAverageHumidity() +
                ", averageIndexedAirQuality=" + getAverageIndexedAirQuality() +
                ", averageAirPressure=" + getAverageAirPressure() +
                ", averageRelativeHumidity=" + getAverageRelativeHumidity() +
                ", averageRoomTemperature=" + getAverageRoomTemperature() +
                ", averageAirTemperature=" + getAverageAirTemperature() +
                '}';
    }
}
