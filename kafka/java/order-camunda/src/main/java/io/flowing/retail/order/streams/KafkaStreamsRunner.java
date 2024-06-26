package io.flowing.retail.order.streams;

import io.flowing.retail.order.streams.serialization.json.MessageOrderSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import java.util.Properties;

@Configuration
public class KafkaStreamsRunner {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @PostConstruct
    public void startKafkaStreams() {
        startTopology(DailyOrdersTopology.build(), "daily-orders-app");
        startTopology(DailyItemsTopology.build(), "daily-color-count-app");
        startTopology(OrderEnrichmentTopology.build(), "order-enrichment-app");
    }

    private void startTopology(Topology topology, String applicationId) {
        try {
            Properties config = new Properties();
            config.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
            config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
            config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, MessageOrderSerde.class);
            config.put("schema.registry.url", "http://localhost:8081");

            KafkaStreams streams = new KafkaStreams(topology, config);
            Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

            System.out.println("Starting " + applicationId + " Streams");
            streams.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
