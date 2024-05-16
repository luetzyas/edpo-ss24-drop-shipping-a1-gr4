package io.flowing.retail.monitor.streams.serealization.json;

import io.flowing.retail.monitor.domain.SensorDataAggregate;
import org.apache.kafka.common.serialization.Serde;

import java.util.Map;

public class SensorDataAggregateSerde implements Serde<SensorDataAggregate> {

    @Override
    public SensorDataAggregateSerializer serializer() {
        return new SensorDataAggregateSerializer();
    }

    @Override
    public SensorDataAggregateDeserializer deserializer() {
        return new SensorDataAggregateDeserializer();
    }

}
