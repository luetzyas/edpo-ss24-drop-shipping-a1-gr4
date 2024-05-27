package io.flowing.retail.order.streams.serialization.json;

import io.flowing.retail.order.domain.Customer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class CustomerSerde implements Serde<Customer> {
    @Override
    public Serializer<Customer> serializer() {
        return new CustomerSerializer();
    }

    @Override
    public Deserializer<Customer> deserializer() {
        return new CustomerDeserializer();
    }
}
