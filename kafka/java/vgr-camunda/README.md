# VGR Service

## Run

The service is integrated with the `flowing-retail Camunda` application. 

In the docker-compose file, the service is defined as a separate service.

```yaml
  vgr:
    build:
      context: ../../kafka/java/vgr-camunda
      dockerfile: Dockerfile
    networks:
      - flowing
    ports:
      - "8097:8097"
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
  $ docker-compose -f docker-compose-kafka-java-order-camunda.yml up --build
```

## Implementation

### Resources
[application.properties](src/main/resources/application.properties) contains the configuration for the Kafka server.

```properties
flowing-retail.topic-name=flowing-retail
spring.kafka.bootstrap-servers=kafka:9092
spring.kafka.consumer.auto-offset-reset=earliest
````
[vgr.bpmn](src/main/resources/vgr.bpmn) contains the BPMN model for the VGR service.

<img src="vgr-bpmn.png" width="853" alt="VGR-BPMN-FLOW">

### Flow
1. The Order-flow orchestrates the VGR during order fulfillment.

<img src="order-flow.png" width="50%" title="Order-BPMN-Flow"/>

2. The VGR service listens to the `flowing-retail` topic and processes the `TriggerVgrCommand` message [MessageListener.java](src/main/java/io/flowing/retail/vgr/messages/MessageListener.java)
- The `messageReceived` method reacts to the `TriggerVgrCommand` message constructs a message with the [TriggerVgrCommandPayload](src/main/java/io/flowing/retail/vgr/flow/TriggerVgrCommandPayload.java) and sends it to the `vgr` topic.
- The message is then correlated with the `vgr.bpmn` model which results in the start of a new process instance via `VGR started` start event.

3. The `vgr.bpmn` then starts the `Move Parts` Activity which is implemented as a Java Delegate `#{movePartsAdapter}` [MovePartsAdapter.java](src/main/java/io/flowing/retail/vgr/flow/MovePartsAdapter.java)
- The `execute` method ... TODO

4. The `vgr.bpmn` then continues with the `VGR finished` end event.
- It's implemented as a Java Delegate `#{vgrFinishedAdapter}` [VgrFinishedAdapter.java](src/main/java/io/flowing/retail/vgr/flow/VgrFinishedAdapter.java) which constructs a message with the [VgrFinishedEventPayload](src/main/java/io/flowing/retail/vgr/flow/VgrFinishedEventPayload.java) and sends it to the `flowing-retail` topic with type `VgrFinishedEvent`.
- The message is then correlated with the `order.bpmn` models incoming-message activity `VGR Finished` which results in the continuation of the order-flow.

### Summary of the Flow
<img src="flow.png" width="1328" alt="Screenshot of event and command monitor">

### Smart Factory Integration
This Services is also configured to listen to the Smart Factory MQTT-topic `f/i/order`.

1. The [MqttConfig.java](src/main/java/io/flowing/retail/vgr/mqtt/MqttConfig.java) configures the MQTT client to listen to the `f/i/order` topic with following properties.

````Properties
# MQTT
mqtt.host=tcp://ftsim.weber.ics.unisg.ch
mqtt.username=ftsim
mqtt.password=unisg
mqtt.port=1883
mqtt.topic=f/i/order
````

- It uses the spring-integration-mqtt library to create an `mqttInputChannel`.

````maven
<dependency>
	<groupId>org.springframework.integration</groupId>
	<artifactId>spring-integration-mqtt</artifactId>
	<version>6.2.3</version>
</dependency>
````
2. The [OrderStateUpdater.java](src/main/java/io/flowing/retail/vgr/mqtt/OrderStateUpdater.java) listens to the `mqttInputChannel` and processes the incoming messages.

- It currently only prints the incoming message to the console.

````Java
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void updateOrderState(Message<String> message) {
        try {
            lastStateJson = message.getPayload();
            System.out.println("******** Updated order state ******** -->  : " + lastStateJson);

            FactoryOrderState orderState = objectMapper.readValue(lastStateJson, FactoryOrderState.class);
            
        } catch (Exception e){
            // Handle JSON parsing or other exceptions
            e.printStackTrace();
        }

    }

    public String getLastStateJson() {
        return lastStateJson;
    }
}
````