# VGR Service

## Run

The service is integrated with the `flowing-retail Camunda` application.

In the docker-compose file, the service is defined as a separate service.

```yaml

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

````
[order.bpmn](src/main/resources/order.bpmn) contains the BPMN model for the Order service.

<img src="vgr-bpmn.png" width="853" alt="VGR-BPMN-FLOW">

### Flow
1. The Order-flow orchestrates the subsequent services during order fulfillment.

2. VGR
- `Trigger VGR` is implemented as java delegate `#{triggerVgrAdapter}` [TriggerVgrAdapter.java](kafka/java/order-camunda/src/main/java/io/flowing/retail/order/flow/TriggerVgrAdapter.java)
- The execute method invokes sending a message to the `flowing-retail` topic with type `TriggerVgrCommand`.
- The `VGR finished` event is then correlated with the `order.bpmn` models incoming-message activity `VGR Finished` which results in the continuation of the order-flow.

### Summary of the Flow

