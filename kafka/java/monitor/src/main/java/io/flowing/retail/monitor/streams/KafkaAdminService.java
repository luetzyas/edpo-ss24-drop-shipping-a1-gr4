package io.flowing.retail.monitor.streams;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.errors.TopicExistsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaAdminService {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @PostConstruct
    public void createTopics() {
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        try (AdminClient adminClient = AdminClient.create(config)) {
            createTopic(adminClient, "sensor-data", 1, (short) 1);
            createTopic(adminClient, "processed-sensor-data", 1, (short) 1);
        } catch (Exception e) {
            System.out.println("Error creating Kafka topics: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createTopic(AdminClient adminClient, String topicName, int numPartitions, short replicationFactor) throws ExecutionException, InterruptedException {
        try {
            if (!topicExists(adminClient, topicName)) {
                NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);
                adminClient.createTopics(Collections.singleton(newTopic)).all().get();
                System.out.println("Topic created: " + topicName);
            } else {
                System.out.println("Topic already exists: " + topicName);
            }
        } catch (TopicExistsException e) {
            System.out.println("Topic already exists (handled in catch): " + topicName);
        }
    }

    private boolean topicExists(AdminClient adminClient, String topicName) throws ExecutionException, InterruptedException {
        return adminClient.listTopics().names().get().contains(topicName);
    }
}

