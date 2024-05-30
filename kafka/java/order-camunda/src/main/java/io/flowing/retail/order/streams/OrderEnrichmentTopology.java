package io.flowing.retail.order.streams;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.flowing.retail.order.domain.Customer;
import io.flowing.retail.order.domain.Order;
import io.flowing.retail.order.domain.OrderDataToAvroMapper;
import io.flowing.retail.order.domain.avro.EnrichedOrder;
import io.flowing.retail.order.messages.Message;
import io.flowing.retail.order.streams.serialization.avro.AvroSerdes;
import io.flowing.retail.order.streams.serialization.json.CustomerSerde;
import io.flowing.retail.order.streams.serialization.json.MessageOrderSerde;
import io.flowing.retail.order.streams.serialization.json.OrderSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.HashMap;
import java.util.Map;

public class OrderEnrichmentTopology {

    public static Topology build() {
        StreamsBuilder builder = new StreamsBuilder();
      //  String schemaRegistryUrl = "http://localhost:8081"; // Local
        String schemaRegistryUrl = "http://schema-registry:8081"; // Docker


        // Define the Customer KTable
        KTable<String, Customer> customerTable = builder.table(
                "customer",
                Consumed.with(Serdes.String(), new CustomerSerde())
        );

        // Define the Orders KStream
        KStream<String, Message<Order>> ordersStream = builder.stream(
                "flowing-retail",
                Consumed.with(Serdes.String(), new MessageOrderSerde())
        );

        // Filter for only "OrderPlacedEvent" and then map to set the new key to order ID
        KStream<String, Order> orderPlacedEvents = ordersStream
                .filter((key, value) -> value != null && "OrderPlacedEvent".equals(value.getType()))
                .map((key, value) -> KeyValue.pair(value.getData().getEmail(), value.getData())); // Use the order-email as the key

        // print each valid message to the console
        customerTable.toStream().foreach((key, customer) -> {
            System.out.println("*OrderEnrichmentTopology* Customer ID: " + customer.getId() + ", Name: " + customer.getName() + ", Email: " + customer.getEmail() + ", Address: " + customer.getAddress());
        });

       orderPlacedEvents.foreach((key, order) -> {
            System.out.println("*OrderEnrichmentTopology* Streamed Message Key: " + key + ", Order ID: " + order.getId() + ", Items: " + order.getItems());
        });

        // Join orders with customer data
        KStream<String, EnrichedOrder> enrichedOrders = orderPlacedEvents
                .leftJoin(customerTable,
                        (order, customer) -> OrderDataToAvroMapper.convertToAvroEnrichedOrder(order, customer),
                        Joined.with(Serdes.String(), new OrderSerde(), new CustomerSerde()))
                .mapValues(value -> value);  // Ensure value is of type EnrichedOrder

        // print each enriched order to the console
        enrichedOrders.foreach((key, order) -> {
            System.out.println("*OrderEnrichmentTopology* Enriched Order ID: " + order.getOrderId() + ", Items: " + order.getItems() + ", Customer: " + order.getCustomer());
        });

        // Branch the stream into successful matches and no matches
        KStream<String, EnrichedOrder>[] branches = enrichedOrders.branch(
                (key, value) -> value.getCustomer().getCustomerId().equals("noMatch"),
                (key, value) -> !value.getCustomer().getCustomerId().equals("noMatch")
        );

        KStream<String, EnrichedOrder> noMatches = branches[0];
        KStream<String, EnrichedOrder> successfulMatches = branches[1];

        // Define how to serialize and deserialize the EnrichedOrder to the topic
        Produced<String, EnrichedOrder> producedEnrichedOrder = Produced.with(Serdes.String(), AvroSerdes.enrichedOrderSerde(schemaRegistryUrl));

        // Process no matches (e.g., send them to a topic to handle registration)
        noMatches.to("customer-not-found", producedEnrichedOrder);

        // Process successful matches (e.g., continue processing or store them)
        successfulMatches.to("enriched-order", producedEnrichedOrder);






        return builder.build();
    }
}
