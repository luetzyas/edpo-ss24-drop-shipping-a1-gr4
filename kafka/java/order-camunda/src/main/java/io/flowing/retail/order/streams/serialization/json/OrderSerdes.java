package io.flowing.retail.order.streams.serialization.json;

import io.flowing.retail.order.domain.Order;
import io.flowing.retail.order.messages.Message;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class OrderSerdes implements Serde<Message<Order>> {

    @Override
    public Serializer<Message<Order>> serializer() {
        return new OrderSerializer();
    }

    @Override
    public Deserializer<Message<Order>> deserializer() {
        return new OrderDeserializer();
    }

}
