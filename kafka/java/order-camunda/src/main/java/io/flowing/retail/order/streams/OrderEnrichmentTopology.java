package io.flowing.retail.order.streams;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.flowing.retail.order.domain.Order;
import io.flowing.retail.order.messages.Message;
import io.flowing.retail.order.streams.serialization.json.MessageOrderSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import java.util.HashMap;
import java.util.Map;

public class OrderEnrichmentTopology {

    public static Topology build() {
        StreamsBuilder builder = new StreamsBuilder();

        // Configure the Avro Serde for Customer
        Map<String, String> serdeConfig = new HashMap<>();
        serdeConfig.put("schema.registry.url", "http://localhost:8081");
/*
        final SpecificAvroSerde<Customer> customerSerde = new SpecificAvroSerde<>();
        customerSerde.configure(serdeConfig, false); // `false` for record values

        // Define the Customer KTable
        KTable<String, Customer> customerTable = builder.table(
                "customer",
                Consumed.with(Serdes.String(), customerSerde)
        );

        // Define the Orders KStream
        KStream<String, Message<Order>> ordersStream = builder.stream(
                "flowing-retail",
                Consumed.with(Serdes.String(), new MessageOrderSerde())
        );

        // Filter for only "OrderPlacedEvent" and then map to set the new key to order ID
        KStream<String, Order> orderPlacedEvents = ordersStream
                .filter((key, value) -> value != null && "OrderPlacedEvent".equals(value.getType()))
                .map((key, value) -> KeyValue.pair(value.getData().getEmail(), value.getData())); // Use the email as the key

        // Perform a left join to enrich orders with customer information
        KStream<String, Order> enrichedOrders = orderPlacedEvents.leftJoin(
                customerTable,
                (order, customer) -> {
                    if (customer != null) {
                        order.setCustomerName(customer.getName().toString());
                        order.setCustomerAddress(customer.getAddress().toString());
                    }
                    return order;
                }
        );

        // Print the enriched orders to the console
        enrichedOrders.foreach((key, order) -> System.out.println("Enriched Order: " + order));

        // Optionally, you can write the enriched orders to a new topic
        enrichedOrders.to("enriched-orders", Produced.with(Serdes.String(), new OrderSerde()));
*/
        return builder.build();
    }
}
