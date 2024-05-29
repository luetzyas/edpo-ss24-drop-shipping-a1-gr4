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
import java.util.HashMap;
import java.util.Map;

public class OrderEnrichmentTopology {

    public static Topology build() {
        StreamsBuilder builder = new StreamsBuilder();

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


 /*
        // Perform a left join to enrich orders with customer information
        KStream<String, Order> enrichedOrders = orderPlacedEvents.leftJoin(
                customerTable,
                (order, customer) -> {
                    if (customer != null) {
                        order.setCustomer(
                                new Customer(customer.getId(), customer.getName(), customer.getAddress(), customer.getEmail())
                        );
                    }
                    return order;
                }
        );
   /*
        // Print the enriched orders to the console
        enrichedOrders.foreach((key, order) -> System.out.println("Enriched Order: " + order));

        // Optionally, you can write the enriched orders to a new topic
        enrichedOrders.to("enriched-orders", Produced.with(Serdes.String(), new OrderSerde()));
*/


        return builder.build();
    }
}
