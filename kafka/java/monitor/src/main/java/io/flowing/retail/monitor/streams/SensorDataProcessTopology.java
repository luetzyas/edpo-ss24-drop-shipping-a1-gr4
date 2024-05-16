package io.flowing.retail.monitor.streams;

import io.flowing.retail.monitor.domain.SensorData;
import io.flowing.retail.monitor.streams.serealization.json.SensorDataSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

public class SensorDataProcessTopology {
    public static Topology build() {
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, SensorData> sensorDataStream = builder.stream("sensor-data",
                Consumed.with(Serdes.String(), new SensorDataSerde()));

        // Filter sensor readings based on poor air quality, very high humidity, and very high temperature
        KStream<String, SensorData> criticalSensorData = sensorDataStream
                .filter((key, sensorData) ->
                        sensorData.getIndexedAirQuality() > 100 || sensorData.getHumidity() > 65 || sensorData.getAirTemperature() > 40);

        // Process the non-critical sensor data
        KStream<String, SensorData> nonCriticalSensorData = sensorDataStream
                .filter((key, sensorData) ->
                        sensorData.getIndexedAirQuality() <= 100 &&
                                sensorData.getHumidity() <= 65 &&
                                sensorData.getAirTemperature() <= 40);

        // Map critical sensor data to have the key "critical"
        KStream<String, SensorData> criticalKeyedSensorData = criticalSensorData
                .map((key, sensorData) -> new KeyValue<>("critical", sensorData));

        // Map non-critical sensor data to have the key "normal"
        KStream<String, SensorData> normalKeyedSensorData = nonCriticalSensorData
                .map((key, sensorData) -> new KeyValue<>("normal", sensorData));


        // Merge the two streams back together
        KStream<String, SensorData> mergedStream = criticalKeyedSensorData.merge(normalKeyedSensorData);

        // Print the output for debugging purposes
        mergedStream.foreach((key, sensorData) -> {
            System.out.println("Key: " + key + ", SensorData: " + sensorData);
        });

        // Forward the processed stream to an output topic
        mergedStream.to("processed-sensor-data", Produced.with(Serdes.String(), new SensorDataSerde()));

        return builder.build();
    }

    }
