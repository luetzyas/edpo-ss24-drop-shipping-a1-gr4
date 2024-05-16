package io.flowing.retail.monitor.streams;

import io.flowing.retail.monitor.domain.SensorData;
import io.flowing.retail.monitor.domain.SensorDataAggregate;
import io.flowing.retail.monitor.streams.serealization.json.SensorDataAggregateSerde;
import io.flowing.retail.monitor.streams.serealization.json.SensorDataSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.WindowStore;

import java.time.Duration;

public class SensorDataMonitorTopology {
    public static Topology build() {
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, SensorData> processedSensorDataStream = builder.stream(
                "processed-sensor-data",
                Consumed.with(Serdes.String(), new SensorDataSerde())
        );

        // Filter for normal sensor data
        KStream<String, SensorData> normalSensorDataStream = processedSensorDataStream
                .filter((key, value) -> "normal".equals(key));

        // Calculate average values per hour for normal data
        KTable<Windowed<String>, SensorDataAggregate> hourlyAverage = normalSensorDataStream
                .groupByKey(Grouped.with(Serdes.String(), new SensorDataSerde()))
                .windowedBy(TimeWindows.of(Duration.ofHours(1)))
                .aggregate(
                        SensorDataAggregate::new, // Initializer
                        (key, newValue, aggregate) -> aggregate.add(newValue), // Aggregator defines how to add a new record (newValue) to the existing aggregation
                        Materialized.<String, SensorDataAggregate, WindowStore<Bytes, byte[]>>as("hourly-average-store")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(new SensorDataAggregateSerde())
                );


        hourlyAverage.toStream().foreach((windowedKey, aggregate) -> {
            System.out.println("Average values from " + windowedKey.window().startTime() + " to " + windowedKey.window().endTime() + ": " + aggregate);
        });

        // Filter for critical sensor data
        KStream<String, SensorData> criticalSensorDataStream = processedSensorDataStream
                .filter((key, value) -> "critical".equals(key));

        // Handle critical sensor data
        criticalSensorDataStream.foreach((key, value) -> {
            System.out.println("ALERT! Critical sensor data received: " + value);
            // You can add more alert handling logic here
        });

        return builder.build();
    }
}
