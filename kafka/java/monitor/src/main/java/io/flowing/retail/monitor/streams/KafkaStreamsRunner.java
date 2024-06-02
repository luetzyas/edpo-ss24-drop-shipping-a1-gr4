package io.flowing.retail.monitor.streams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Configuration
public class KafkaStreamsRunner {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Autowired
    private KafkaStreamsService kafkaStreamsService;

    @PostConstruct
    public void startKafkaStreams() {
        try {
            startTopology(SensorDataProcessTopology.build(), "sensor-data-process-app");
            startTopology(SensorDataMonitorTopology.build(kafkaStreamsService), "sensor-data-monitor-app");
        } catch (Exception e) {
            System.out.println("Error starting Kafka Streams topologies: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void startTopology(Topology topology, String applicationId) {
        try {
            Properties config = new Properties();
            config.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
            config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
            config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
            config.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE_V2);
            config.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 1);

            KafkaStreams streams = new KafkaStreams(topology, config);
            streams.setStateListener((newState, oldState) -> {
                System.out.println("State change from " + oldState + " to " + newState);
                if (newState == KafkaStreams.State.ERROR) {
                    System.err.println("Kafka Streams entered ERROR state. Exiting...");
                    System.exit(1);
                }
            });

            Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

            kafkaStreamsService.registerStreams(streams); // Register KafkaStreams instance

            System.out.println("Starting " + applicationId + " Streams");
            streams.start();
        } catch (Exception e) {
            System.out.println("Error starting Kafka Streams for " + applicationId + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
