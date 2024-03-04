package com.examples;


import com.data.Gaze;
import com.google.common.io.Resources;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.Future;


public class EyeTrackersProducer {

    public static void main(String[] args) throws  Exception {

        // Specify Topic
        String topic = "gaze-events";


        // read Kafka properties file
        Properties properties;
        try (InputStream props = Resources.getResource("producer.properties").openStream()) {
            properties = new Properties();
            properties.load(props);
        }

        // create Kafka producer
        KafkaProducer<String, Gaze> producer = producer = new KafkaProducer<>(properties);

        /// delete existing topic with the same name
        deleteTopic(topic, properties);

        // create new topic with 2 partitions
        createTopic(topic, 2, properties);

        List<Long> latencyList = new ArrayList<>();

        try {

            // define an array with devices IDs
            Integer[] deviceIDs = {0,1};

            // define a counter which will be used as an eventID
            int counter = 0;

            while(true) {

                // sleep for 8 ms
                try {
                    Thread.sleep(8);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // select random device
                int deviceID =getRandomNumber(0, deviceIDs.length);

                // generate a random gaze event using constructor  Gaze(int eventID, long timestamp, int xPosition, int yPosition, int pupilSize)
                Gaze gazeEvent = new Gaze(counter,System.nanoTime(), getRandomNumber(0, 1920), getRandomNumber(0, 1080), getRandomNumber(3, 4));

                long sendTimeNano = System.nanoTime(); // Capture send time

                // send the gaze event
                Future<RecordMetadata> future = producer.send(new ProducerRecord<String, Gaze>(
                        topic, // topic
                        String.valueOf(deviceID), // key
                        gazeEvent  // value
                ));

                RecordMetadata metadata = future.get(); // Blocks until the record is acknowledged
                long ackTimeNano = System.nanoTime(); // Capture ack time
                // Calculate and print the latency in milliseconds
                long latencyInMillis = (ackTimeNano - sendTimeNano) / 1_000_000;
                System.out.println("Latency: " + latencyInMillis + " ms");
                latencyList.add(latencyInMillis);

                // print to console
                System.out.println("gazeEvent sent: "+gazeEvent.toString()+" from deviceID: "+deviceID);

                // increment counter i.e., eventID
                counter++;
                if(counter == 5000)
                    break;
            }

        } catch (Throwable throwable) {
            System.out.println(throwable.getStackTrace());
        } finally {
            producer.close();
            // Calculate the average latency
            double averageLatency = latencyList.stream().mapToLong(Long::longValue).average().orElse(0);
            System.out.println("Average latency: " + averageLatency + " ms");
        }


    }



    /*
    Generate a random nunber
    */
    private static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    /*
    Create topic
     */
    private static void createTopic(String topicName, int numPartitions, Properties properties) throws Exception {

        AdminClient admin = AdminClient.create(properties);

        //checking if topic already exists
        boolean alreadyExists = admin.listTopics().names().get().stream()
                .anyMatch(existingTopicName -> existingTopicName.equals(topicName));
        if (alreadyExists) {
            System.out.printf("topic already exits: %s%n", topicName);
        } else {
            //creating new topic
            System.out.printf("creating topic: %s%n", topicName);
            NewTopic newTopic = new NewTopic(topicName, numPartitions, (short) 1);
            admin.createTopics(Collections.singleton(newTopic)).all().get();
        }
    }

    /*
    Delete topic
     */
    private static void deleteTopic(String topicName, Properties properties) {

        try (AdminClient client = AdminClient.create(properties)) {
            DeleteTopicsResult deleteTopicsResult  = client.deleteTopics(Collections.singleton(topicName));
            while (!deleteTopicsResult.all().isDone()) {
                // Wait for future task to complete
            }
        }






    }

}
