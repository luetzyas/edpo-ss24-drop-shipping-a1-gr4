package io.flowing.retail.order.streams;

import io.flowing.retail.order.domain.Order;
import io.flowing.retail.order.messages.Message;
import io.flowing.retail.order.streams.serialization.json.MessageOrderSerde;
import io.flowing.retail.order.streams.serialization.json.OrderSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.WindowStore;

import java.time.Duration;

public class DailyOrdersTopology {


    public static Topology build() {
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, Message<Order>> ordersStream = builder.stream("flowing-retail",
                Consumed.with(Serdes.String(), new MessageOrderSerde()));

        // Filter for only "OrderPlacedEvent" and then map to set the new key to order ID
        KStream<String, Order> orderPlacedEvents = ordersStream
                .filter((key, value) -> value != null && "OrderPlacedEvent".equals(value.getType()))
                .map((key, value) -> KeyValue.pair("all_orders", value.getData())); // Use a constant key for all orders


        // Print each valid message to the console
        orderPlacedEvents.foreach((key, order) -> {
            System.out.println("*DailyOrdersTopology* Streamed Message Key: " + key + ", Order ID: " + order.getId() + ", Items: " + order.getItems());
            order.getItems().forEach(item ->
                    System.out.println("Item: " + item.getArticleId() + ", Quantity: " + item.getAmount()));
        });

        // Group by key (order ID) and window by day, then count
        KTable<Windowed<String>, Long> dailyOrderCounts = orderPlacedEvents
                .groupByKey(Grouped.with(Serdes.String(), new OrderSerde())) // Ensure to use proper Serde for the Order class
                .windowedBy(TimeWindows.of(Duration.ofDays(1)))
                .count(Materialized.<String, Long, WindowStore<Bytes, byte[]>>as("daily-order-counts")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(Serdes.Long()));

        // Print the daily counts
        dailyOrderCounts.toStream().foreach((windowedKey, count) -> {
            System.out.println("**DailyOrdersTopology** \n" +
                    "Window Start Time: " + windowedKey.window().startTime() +
                    ", Order ID: " + windowedKey.key() +
                    ", Total Orders Count: " + count);
        });

        return builder.build();
    }
}