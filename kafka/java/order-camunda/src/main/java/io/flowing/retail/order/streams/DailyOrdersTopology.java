package io.flowing.retail.order.streams;

import io.flowing.retail.order.domain.Order;
import io.flowing.retail.order.messages.Message;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;

public class DailyOrdersTopology {


    public static Topology build() {
        StreamsBuilder builder = new StreamsBuilder();

        // Assuming the topic receives Message<Order> serialized as JSON
        KStream<String, Message<Order>> ordersStream = builder.stream("flowing-retail",
                Consumed.with(Serdes.String(), new JsonSerde<>(Message.class)));

        // Filter for only OrderPlacedEvent types
        KStream<String, Order> orderPlacedEvents = ordersStream
                .filter((key, value) -> "OrderPlacedEvent".equals(value.getType()))
                .map((key, value) -> new KeyValue<>(key, value.getData()));

        // Daily counting of orders
        KTable<Windowed<String>, Long> dailyOrderCounts = orderPlacedEvents
                .groupByKey()
                .windowedBy(TimeWindows.of(Duration.ofDays(1)))
                .count();

        dailyOrderCounts.toStream().foreach((windowedKey, count) -> {
            System.out.println("Order count on " + windowedKey.window().startTime() + ": " + count);
        });

        return builder.build();
    }
}