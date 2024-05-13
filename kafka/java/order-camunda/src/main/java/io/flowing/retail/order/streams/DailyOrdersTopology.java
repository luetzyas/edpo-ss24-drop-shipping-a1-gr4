package io.flowing.retail.order.streams;

import io.flowing.retail.order.domain.Order;
import io.flowing.retail.order.messages.Message;
import io.flowing.retail.order.streams.serialization.json.OrderSerdes;
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

        // TODO : The message does not have a key, or if so it is still Null. Also, a custome Serde is required for Message<Order> see: GPT
        // Assuming the topic receives Message<Order> serialized as JSON
        KStream<String, Message<Order>> ordersStream = builder.stream("flowing-retail",
                Consumed.with(Serdes.String(), new OrderSerdes()));

        // Filter for only OrderPlacedEvent types
        KStream<String, Order> orderPlacedEvents = ordersStream
                .filter((key, value) -> {
                    try {
                        return "OrderPlacedEvent".equals(value.getType());
                    } catch (Exception e) {
                        System.out.println("Error processing message key=" + key + ", value=" + value + ", error=" + e.getMessage());
                        return false;
                    }
                })
                .map((key, value) -> {
                    try {
                        return new KeyValue<>(key, value.getData());
                    } catch (Exception e) {
                        System.out.println("Mapping failure for key=" + key + ", value=" + value + ", error=" + e.getMessage());
                        return null;
                    }
                })
                .filter((key, value) -> value != null);

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