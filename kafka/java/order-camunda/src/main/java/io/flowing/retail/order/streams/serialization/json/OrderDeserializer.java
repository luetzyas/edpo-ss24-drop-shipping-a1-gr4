package io.flowing.retail.order.streams.serialization.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.flowing.retail.order.domain.Order;
import io.flowing.retail.order.messages.Message;
import org.apache.kafka.common.serialization.Deserializer;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class OrderDeserializer implements Deserializer<Message<Order>> {
    private Gson gson = new Gson();
    private final Type type = new TypeToken<Message<Order>>(){}.getType();  // Preserve generic type


    @Override
    public Message<Order> deserialize(String topic, byte[] bytes) {
      if (bytes == null) return null;
      return gson.fromJson(new String(bytes, StandardCharsets.UTF_8), type);
    }
}
