package io.flowing.retail.monitor.streams.serealization.json;

import io.flowing.retail.monitor.domain.SensorDataAggregate;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.Deserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class SensorDataAggregateSerde implements Serde<SensorDataAggregate> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Serializer<SensorDataAggregate> serializer() {
        return new Serializer<SensorDataAggregate>() {
            @Override
            public byte[] serialize(String topic, SensorDataAggregate data) {
                try {
                    return objectMapper.writeValueAsBytes(data);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to serialize SensorDataAggregate", e);
                }
            }
        };
    }

    @Override
    public Deserializer<SensorDataAggregate> deserializer() {
        return new Deserializer<SensorDataAggregate>() {
            @Override
            public SensorDataAggregate deserialize(String topic, byte[] data) {
                try {
                    return objectMapper.readValue(data, SensorDataAggregate.class);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to deserialize SensorDataAggregate", e);
                }
            }
        };
    }

}
