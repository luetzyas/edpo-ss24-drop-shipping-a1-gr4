package io.flowing.retail.order.streams;

import io.flowing.retail.order.domain.Order;
import io.flowing.retail.order.domain.OrderItem;
import io.flowing.retail.order.messages.Message;
import io.flowing.retail.order.streams.serialization.json.MessageOrderSerde;
import io.flowing.retail.order.streams.serialization.json.OrderSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class DailyItemsTopology {


    public static Topology build() {
        StreamsBuilder builder = new StreamsBuilder();

        // Create new itemsStream and consume from the "flowing-retail" topic
        KStream<String, Message<Order>> ordersStream = builder.stream("flowing-retail",
                Consumed.with(Serdes.String(), new MessageOrderSerde()));

        // Filter for only "OrderPlacedEvent" and then map to set the new key to order ID
        KStream<String, Order> orderPlacedEvents = ordersStream
                .filter((key, value) -> value != null && "OrderPlacedEvent".equals(value.getType()))
                // Use flatMap to transform the list of items in each order to a stream of color key-values
                .flatMap((key, value) -> {
                    List<KeyValue<String, Order>> itemsColorList = new ArrayList<>();
                    for (OrderItem orderItem: value.getData().getItems()) {
                        itemsColorList.add(new KeyValue<>(orderItem.getArticleId(), value.getData()));
                    }
                    return itemsColorList;
                });

        // Print each valid message to the console
        orderPlacedEvents.foreach((key, order) -> {
            System.out.println("Streamed Message Key: " + key + ", Order ID: " + order.getId() + ", Items: " + order.getItems());
            order.getItems().forEach(item ->
                    System.out.println("Item: " + item.getArticleId() + ", Quantity: " + item.getAmount()));
        });

        // Group by key (order ID) and window by day, then count
        KTable<Windowed<String>, Long> dailyItemsCount = orderPlacedEvents
                .groupByKey(Grouped.with(Serdes.String(), new OrderSerde()))
                .windowedBy(TimeWindows.of(Duration.ofDays(1)))
                .count(Materialized.as("daily-items-count"));

        // Print the daily item counts
        dailyItemsCount.toStream().foreach((windowedKey, count) -> {
            System.out.println("**DailyItemsTopology** \n" +
                    "Window Start Time: " + windowedKey.window().startTime() +
                    ", Order ID: " + windowedKey.key() +
                    ", Total Orders Count: " + count);
        });

        return builder.build();
    }
}