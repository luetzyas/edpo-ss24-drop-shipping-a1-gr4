package io.flowing.retail.monitor.streams;

import io.flowing.retail.monitor.domain.SensorData;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.WindowStore;

import java.time.Duration;

public class SensorDataTopology {
    public static Topology build() {
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, SensorData> sensorDataStream = builder.stream("sensor-data",
                Consumed.with(Serdes.String(), new SensorDataSerde()));

        // Print each valid message to the console
        sensorDataStream.foreach((key, sensorData) -> {
            System.out.println("Streamed Message Key: " + key + ", Sensordata: " + sensorData);
        });




        return builder.build();
    }

    }
