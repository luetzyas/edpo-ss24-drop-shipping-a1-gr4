package io.flowing.retail.order.streams.serialization.json;

import com.google.gson.Gson;
import io.flowing.retail.order.domain.Customer;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

public class CustomerDeserializer implements Deserializer<Customer> {

    private Gson gson = new Gson();

    @Override
    public Customer deserialize(String topic, byte[] bytes) {
        if (bytes == null) return null;
        return gson.fromJson(new String(bytes, StandardCharsets.UTF_8), Customer.class);
    }
}
