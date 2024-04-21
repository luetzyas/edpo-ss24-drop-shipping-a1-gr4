# Order Service

## Run

The service is orchestrating the `flowing-retail Camunda` application.

The start event is triggered by the `OrderPlacedEvent` from the Checkout-Service.

## Implementation

### Resources
[application.properties](src/main/resources/application.properties) contains the configuration for the Kafka server.

```properties

````
[order.bpmn](src/main/resources/order.bpmn) contains the BPMN model for the Order service.

<img src="vgr-bpmn.png" width="853" alt="VGR-BPMN-FLOW">

### Flow
1. The Order-flow orchestrates the subsequent services during order fulfillment.

2. The `Reserve Stock items` Send Task is implemented as Java Delegate [ReserveStockItemsAdapter](src/main/java/io/flowing/retail/order/flow/ReserveStockItemsAdapter.java) which sends a Command to the Inventory Service with the OrderId and the OrderItems.
3. The Items `reserved received` Catch Event waits until the `GoodsBlockedEvent` is received from the Inventory Service.

### Summary of the Flow

