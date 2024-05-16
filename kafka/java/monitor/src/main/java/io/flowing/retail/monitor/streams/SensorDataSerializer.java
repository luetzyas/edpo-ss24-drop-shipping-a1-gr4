package io.flowing.retail.monitor.streams;

import com.google.gson.Gson;
import io.flowing.retail.monitor.domain.SensorData;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;

public class SensorDataSerializer implements Serializer<SensorData> {
    private Gson gson = new Gson();

    @Override
    public byte[] serialize(String topic, SensorData data) {
        if (data == null) return null;
        return gson.toJson(data).getBytes(StandardCharsets.UTF_8);
    }
}
