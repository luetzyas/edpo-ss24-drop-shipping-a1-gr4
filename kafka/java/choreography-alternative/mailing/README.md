# Mailing Service

## Run

The service is integrated with the `flowing-retail choreography-alternative` application. 

In the docker-compose file, the service is defined as a separate service.

```yaml
mailing:
  build:
  context: ../../kafka/java/mailing
  dockerfile: Dockerfile
  networks:
    - flowing
  ports:
    - "8096:8096"
  depends_on:
    - kafka
  environment:
    - SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092`
```

It is started when the relevant projects have been built at least once:

```
  $ cd .\kafka\java\
  $ mvn clean install
```

And afterwards you run from the directory [runner/docker-compose](runner/docker-compose).

```
  $ docker-compose -f docker-compose-kafka-java-choreography.yml up --build
```

## Implementation

### Properties
[application.properties](src/main/resources/application.properties) contains the configuration for the Kafka consumer and the mail server.

### Message Listener
[MessageListener.java](src/main/java/io/drop/shipping/mailing/messages/MessageListener.java) 