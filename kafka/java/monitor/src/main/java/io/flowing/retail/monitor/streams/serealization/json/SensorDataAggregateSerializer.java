package io.flowing.retail.monitor.streams.serealization.json;

import com.google.gson.Gson;
import io.flowing.retail.monitor.domain.SensorDataAggregate;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;

public class SensorDataAggregateSerializer implements Serializer<SensorDataAggregate> {
    private Gson gson = new Gson();

    @Override
    public byte[] serialize(String topic, SensorDataAggregate data) {
        if (data == null) return null;
        return gson.toJson(data).getBytes(StandardCharsets.UTF_8);
    }
}
