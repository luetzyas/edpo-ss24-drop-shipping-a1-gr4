package io.flowing.retail.order.streams.serialization.avro;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.flowing.retail.order.domain.avro.EnrichedOrder;
import org.apache.kafka.common.serialization.Serde;

import java.util.Collections;
import java.util.Map;

public class AvroSerdes {

    public static Serde<EnrichedOrder> enrichedOrderSerde() {
        Map<String, String> serdeConfig = Collections.singletonMap("schema.registry.url", "http://localhost:8081");
        Serde<EnrichedOrder> serde = new SpecificAvroSerde<>();
        serde.configure(serdeConfig, false);
        return serde;
    }

}
