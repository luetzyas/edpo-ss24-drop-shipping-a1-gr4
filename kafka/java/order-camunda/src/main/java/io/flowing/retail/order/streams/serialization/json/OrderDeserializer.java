package io.flowing.retail.order.streams.serialization.json;

import com.google.gson.Gson;
import io.flowing.retail.order.domain.Order;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

public class OrderDeserializer implements Deserializer<Order> {
    private Gson gson = new Gson();

    @Override
    public Order deserialize(String topic, byte[] bytes) {
      if (bytes == null) return null;
      // Debugging output
      System.out.println("OrderDeserializer input: " + new String(bytes, StandardCharsets.UTF_8));
      return gson.fromJson(new String(bytes, StandardCharsets.UTF_8), Order.class);
    }
}
