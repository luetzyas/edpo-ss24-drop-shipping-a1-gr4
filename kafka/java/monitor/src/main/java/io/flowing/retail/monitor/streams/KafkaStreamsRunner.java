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
        startTopology(SensorDataProcessTopology.build(), "sensor-data-process-app");
        startTopology(SensorDataMonitorTopology.build(), "sensor-data-monitor-app");
    }

    private void startTopology(Topology topology, String applicationId) {
        try {
            Properties config = new Properties();
            config.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
            config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
            config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

            KafkaStreams streams = new KafkaStreams(topology, config);
            Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

            kafkaStreamsService.registerStreams(streams); // Register KafkaStreams instance

            System.out.println("Starting " + applicationId + " Streams");
            streams.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
