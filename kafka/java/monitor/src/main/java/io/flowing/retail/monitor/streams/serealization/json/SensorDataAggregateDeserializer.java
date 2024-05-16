package io.flowing.retail.monitor.streams.serealization.json;

import com.google.gson.Gson;
import io.flowing.retail.monitor.domain.SensorDataAggregate;
import org.apache.kafka.common.serialization.Deserializer;

public class SensorDataAggregateDeserializer implements Deserializer<SensorDataAggregate> {
    private Gson gson = new Gson();

    @Override
    public SensorDataAggregate deserialize(String topic, byte[] data) {
        if (data == null) return null;
        return gson.fromJson(new String(data), SensorDataAggregate.class);
    }
}
