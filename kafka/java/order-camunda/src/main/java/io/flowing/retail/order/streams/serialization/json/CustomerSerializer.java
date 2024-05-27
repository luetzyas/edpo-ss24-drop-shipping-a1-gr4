package io.flowing.retail.order.streams.serialization.json;

import com.google.gson.Gson;
import io.flowing.retail.order.domain.Customer;
import org.apache.kafka.common.serialization.Serializer;

public class CustomerSerializer implements Serializer<Customer> {

    private Gson gson = new Gson();

    @Override
    public byte[] serialize(String topic, Customer customer) {
        if (customer == null) return null;
        return gson.toJson(customer).getBytes();
    }

}
