# University of St.Gallen - Exercise Submission

## Course Information

- **Course:** Event-driven and Process-oriented Architectures FS2024
- **Instructors:** B. Weber, R. Seiger, A. Abbad-Andaloussi


# Drop Shipping Group 4 - Commands

This sample application demonstrates a simple order fulfillment system, decomposed into multiple independent components (like _microservices_).

## Maven
from the directory [kafka/java](../../kafka/java).

### Clean Project
```
  $ cd .\kafka\java\
```
```
  $ mvn clean install
```

### Run Project
```
  $ mvn spring-boot:run
```

## Docker
from the directory [runner/docker-compose](../../runner/docker-compose).

### Start docker (with container name)
```
  docker-compose -p docker-choreography -f docker-compose-kafka-java-choreography.yml up
```
```
  docker-compose -p docker-order-camunda -f docker-compose-kafka-java-order-camunda.yml up
```
```
  docker-compose -p docker-order-zeebe -f docker-compose-kafka-java-order-zeebe.yml up
```

### Topic auflisten
```
  docker-compose exec kafka bash -c "KAFKA_OPTS='' /opt/bitnami/kafka/bin/kafka-topics.sh --list --bootstrap-server localhost:9092"
```

### Topic löschen
from the docker [runner/docker-compose](../../runner/docker-compose).
```
  docker-compose exec kafka bash -c "KAFKA_OPTS='' /opt/bitnami/kafka/bin/kafka-topics.sh --delete --topic gaze-events --bootstrap-server localhost:9092"
```

### Docker Container auflisten
```
  docker ps
```

### Docker Logs
```
  docker-compose logs docker-kafka-1  
```