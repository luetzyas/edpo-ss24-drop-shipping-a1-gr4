package io.flowing.retail.order.streams.serialization.json;

import com.google.gson.Gson;
import io.flowing.retail.order.domain.Order;
import io.flowing.retail.order.messages.Message;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;

public class OrderSerializer implements Serializer<Message<Order>> {
    private Gson gson = new Gson();

    @Override
    public byte[] serialize(String topic, Message<Order> order) {
        if (order == null) return null;
        return gson.toJson(order).getBytes(StandardCharsets.UTF_8);
    }
}
