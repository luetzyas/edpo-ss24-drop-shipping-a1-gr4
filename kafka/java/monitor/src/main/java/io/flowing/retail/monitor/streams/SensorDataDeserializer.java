package io.flowing.retail.monitor.streams;

import com.google.gson.Gson;
import io.flowing.retail.monitor.domain.SensorData;
import org.apache.kafka.common.serialization.Deserializer;

public class SensorDataDeserializer implements Deserializer<SensorData> {
    private Gson gson = new Gson();

    @Override
    public SensorData deserialize(String topic, byte[] data) {
        if (data == null) return null;
        return gson.fromJson(new String(data), SensorData.class);
    }
}
