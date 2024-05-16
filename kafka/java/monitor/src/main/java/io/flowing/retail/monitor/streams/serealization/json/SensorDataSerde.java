package io.flowing.retail.monitor.streams.serealization.json;

import io.flowing.retail.monitor.domain.SensorData;
import org.apache.kafka.common.serialization.Serde;

public class SensorDataSerde implements Serde<SensorData> {

      @Override
      public SensorDataSerializer serializer() {
     return new SensorDataSerializer();
      }

      @Override
      public SensorDataDeserializer deserializer() {
     return new SensorDataDeserializer();
      }

}
