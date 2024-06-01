package io.flowing.retail.order.streams;

import io.flowing.retail.order.domain.Order;
import io.flowing.retail.order.domain.OrderItem;
import io.flowing.retail.order.messages.cloud.Message;
import io.flowing.retail.order.streams.serialization.json.MessageOrderSerde;
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
        KStream<String, Long> itemsStream = ordersStream
                .filter((key, value) -> value != null && "OrderPlacedEvent".equals(value.getType()))
                // Use flatMap to transform the list of items in each order to a stream of color key-values
                .flatMap((key, value) -> {
                    List<KeyValue<String, Long>> itemsColorList = new ArrayList<>();
                    for (OrderItem orderItem: value.getData().getItems()) {
                        itemsColorList.add(new KeyValue<>(orderItem.getArticleId(), (long) orderItem.getAmount()));
                    }
                    return itemsColorList;
                });

        // Group by item ID, window by day, and aggregate the count of each item
        KTable<Windowed<String>, Long> dailyItemsCount = itemsStream
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Long()))
                .windowedBy(TimeWindows.of(Duration.ofDays(1)))
                .reduce(Long::sum)
                ;

        // Print the daily item counts
        dailyItemsCount.toStream().foreach((windowedKey, count) -> {
            System.out.println("**DailyItemsTopology** \n" +
                    "Window Start Time: " + windowedKey.window().startTime() +
                    ", Item ID: " + windowedKey.key() +
                    ", Total Items Count: " + count);
        });

        return builder.build();
    }
}