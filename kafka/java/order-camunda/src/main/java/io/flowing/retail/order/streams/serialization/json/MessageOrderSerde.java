package io.flowing.retail.order.streams.serialization.json;

import io.flowing.retail.order.domain.Order;
import io.flowing.retail.order.messages.cloud.Message;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class MessageOrderSerde implements Serde<Message<Order>> {

    @Override
    public Serializer<Message<Order>> serializer() {
        return new MessageOrderSerializer();
    }

    @Override
    public Deserializer<Message<Order>> deserializer() {
        return new MessageOrderDeserializer();
    }

}
