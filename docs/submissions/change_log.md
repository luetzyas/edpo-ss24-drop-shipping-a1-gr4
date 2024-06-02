# Changelog Group 4

This log summarizes all notable changes made to the project from Week 6 onwards to facilitate easy reading and
evaluation. The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

Here, you will find a brief overview of our progress, including major milestones and the submission dates of our work.

- **Submissions:** Our Submission documents can be found here [submissions](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/tree/master/docs/submissions).
  - All exercise papers can be found in the [exercise_tasks](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/tree/master/docs/submissions/exercise_tasks) folder 
  - The given feedback to improve our project in the [feedback](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/tree/master/docs/submissions/feedback) folder.

## Group Information

- **Yasmin Lützelschwab** ([luetzyas](https://github.com/luetzyas)) → [e-Mail](mailto:yasminesmeralda.luetzelschwab@student.unisg.ch)
- **Stefan Meier** ([stefanmhsg](https://github.com/stefanmhsg)) → [e-Mail](mailto:stefan.meier@student.unisg.ch)

## Exercise Overview
- [**Exercise 1**: Kafka - Getting Started](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/blob/master/docs/submissions/exercise_tasks/EDPO_SS24_E1.pdf)
- [**Exercise 2**: Kafka with Spring](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/blob/master/docs/submissions/exercise_tasks/EDPO_SS24_E2.pdf)
- [**Exercise 3**: Process Orchestration with Camunda](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/blob/master/docs/submissions/exercise_tasks/EDPO_SS24_E3.pdf)
- [**Exercise 4**: Orchestration vs Choreography in Flowing Retail](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/blob/master/docs/submissions/exercise_tasks/EDPO_SS24_E4.pdf)
- [**Exercise 5**: Zeebe.io](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/blob/master/docs/submissions/exercise_tasks/EDPO_SS24_E5.pdf)
- [**Exercise 6**: Sagas and Stateful Resilience Patterns](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/blob/master/docs/submissions/exercise_tasks/EDPO_SS24_E6.pdf)
- [**Exercise 7**: Stream Processing with Kafka Streams (Part I)](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/blob/master/docs/submissions/exercise_tasks/EDPO_SS24_E7.pdf)
- [**Exercise 8**: Stream Processing with Kafka Streams (Part II)](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/blob/master/docs/submissions/exercise_tasks/EDPO_SS24_E8.pdf)
- [**Exercise 9**: Stream Processing with Kafka Streams (Part III)](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/blob/master/docs/submissions/exercise_tasks/EDPO_SS24_E9.pdf)

## Final Submission Assignment 2 02. June 2024
[Release 2.0](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/releases/tag/EDPO_A2)

**@stefanmhsg**
- Order-Camunda Service
  - Stream Processing to get Daily Count of Orders
    - DailyOrderTopology
    - KafkaStreamsRunner
    - POM.xml with dependencies

- VGR-Service
  - MQTT Topic `i/bme680` forwarded to Kafka Topic `sensor-data`

- Checkout Service
  - BPMN Error Handling if Order is greater than 100 items leads to a User Task for Human Intervention, displaying the custom error message

- Monitor-Service
  - Stream Processing to monitor sensor data
    - SensorDataProcessTopology
    - SensorDataMonitorTopology
    - KafkaStreamsRunner
    - POM.xml with dependencies
  - Display sensor data on Monitor Dashboard by Windowed Aggregation
  - Display ciritcal sensor data on Monitor Dashboard


**@luetzyas**
- Documentation
  - Work on final presentation slides
  - ADR [0004 Camunda 7 vs 8](adr/0004-Camunda-7-vs-8.md)
  - ADR [0005 Add new CRM-Camunda Service](adr/0005-add-crm-camunda-service.md) 
  - implement feedback in [Assignment_1-revised-Final-Report](Assignment_1-revised-Final-Report.pdf) 
  - work on final report for [Assingment_2-Final_Report](Assignment_2-Final-Report.pdf) 
  - create diagrams of the topologies for Assignment 2 >> the graphics can be found [here](../docs/topologies)
  - prepare project for final submission on Git and Canvas
  
- Presentation
  - add [Midterm](Midterm-Presentation-EDPO-Gr4.pdf) presentation file to project
  - add [Final](Final-Presentation-EDPO-Gr4.pdf) presentation file to project
  
- Order-Camunda Service
  - new class [DailyItemsTopology](../../kafka/java/order-camunda/src/main/java/io/flowing/retail/order/streams/DailyItemsTopology.java) to get daily items count per day
  - restructure [KafkaStreamsRunner](../../kafka/java/order-camunda/src/main/java/io/flowing/retail/order/streams/KafkaStreamsRunner.java) for multiple topologies
  
- CRM-Camunda Service >> new microservice [crm-camunda](../../kafka/java/crm-camunda) to store and update customer data
  - Process to load XML file with customer sample data during Springboot startup
    - new classes: [Customers](../../kafka/java/crm-camunda/src/main/java/io/flowing/retail/crm/dbLoader/Customers.java), [DataLoader](../../kafka/java/crm-camunda/src/main/java/io/flowing/retail/crm/dbLoader/DataLoader.java)
    - new xmlfile: [customers.xml](../../kafka/java/crm-camunda/src/main/resources/customers.xml)
    - enhanced [CrmApplication](../../kafka/java/crm-camunda/src/main/java/io/flowing/retail/crm/CrmApplication.java) with demo bean

## Final Submission Assignment 1 21. April 2024
[Release 1.2](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/releases/tag/EDPO_A1_3.0)

**@stefanmhsg**
- Checkout Service 
  - as per the newly created [README.md](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/blob/master/kafka/java/checkout/README.md)
  - implemented with Camunda
  - replaced checkout with camunda form
    - assembling an order instance-variable from the form data
    - checkout-bpmn-flow which checks if ordered items are in stock, if not User Task will ask for a confirmation to proceed with the order and show which items are not in stock
  - Event-Carried State Transfer Pattern to keep a local copy of the inventory data
    - Message Listener for `InventoryUpdatedEvent` and `GoodsBlockedEvent`
    - `CheckoutService` to update the local inventory data

- VGR Service
  - MQTT Client for VGR Service capturing Order state
  - Updated existing [README.md](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/blob/master/kafka/java/vgr-camunda/README.md)

- Inventory Service
  - MQTT Client for Inventory Service capturing Inventory state
  - Event-Carried State Transfer Pattern notifying Checkout Service about Inventory changes
  - Idempotent Consumer Pattern
  - Outbox Pattern
  - as per the newly created [README.md](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/blob/master/kafka/java/inventory/README.md)

- Order Service
  - Extended Flow for parallel execution of payment and reserving required goods
  - as per the newly created [README.md](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/blob/master/kafka/java/order-camunda/README.md)

- Exercice 6
  - Report for E6
  - Outbox Pattern

**@luetzyas**
- Assignment 1 [Presentation](A1_Gr4_MidtermPresentation.pdf)
    - Presentation slides
    - Presentation script
- Documentation
    - ADR [0001 add Mailing service](adr/0001-add-mailing-service.md)
    - ADR [0002 add Factory service](adr/0002-add-factory-service.md)
    - ADR [0003 Choreographed vs. Orchestrated](adr/0003-Choreographed-vs-Orchestrated.md)
    - E5 Report
    - work on Assignment 1 final report
- Inventory Service
  - MQTT subscription >> topic `f/i/stock`
- Mailing Service
  - Duplicate Mailing Service to use Camunda 8 >> Zeebe.io
  - Connect with Zeebe.io Cloud Client [application.properties](../../kafka/java/mailing/src/main/resources/application.properties)
  - Standard Mailing Flow without Logic >> to test Zeeebe
  - Expand `MailingSerivce` with Zeebe.io to send mails
- Zeebe.io
  - implement new .yml file for mailing service with zeebe.io
  - test mailing bpmn
  - send mail with event logic bpmn

## Submission 19. March 2024
[Release 1.1](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/releases/tag/EDPO_A1_E3_4)

**@stefanmhsg**
- draft README.md file for the Mailing Service
- enhance the experiments for E1
  - E01: Outage of Zookeeper (added: tried to modify topic while Zookeeper is down)
  - E06: Message Retention (new)
- add new service vgr-camunda
  - As outlined in the README.md
- draft BPMN diagram for VGR-Camunda service
- adapt order-service to include vgr-flow
- work on report for E3 and E4

**@luetzyas** 
- create changelog for group project
- E01:
  - what happens if 1 consumer is shut down
  - implement linger.ms to see if the latency behaviour changes
  - enhance conclusion for the experiment: Read from 0
- work on commands file for own project shortcuts
- E02: 
  - enhance documentation according to the submission feedback [doc](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/blob/master/docs/submissions/A1_E2_Gr4_KaftaWithSpring.md)
  - write steps in the mailingservice [README.md](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/blob/master/kafka/java/mailing/README.md) 
  - implement eventhandling in mailing service for OrderPlacedEvent, PaymentReceivedEvent, GoodsShippedEvent and OrderCompletedEvent [mailing-service](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/tree/master/kafka/java/mailing/src/main/java/io/drop/shipping/mailing) 
  - implement new handlingEvents in the message listener and to "send" a mail with the according eventPayload [mailing-service](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/tree/master/kafka/java/mailing/src/main/java/io/drop/shipping/mailing)
- rework whole project because of this error occuring in unchanged services: ()
- create payloads for the events
  - OrderItemPayload
  - OrderPlacedEventPayload
```java
2024-03-18 21:11:29 SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
2024-03-18 21:11:29 SLF4J: Defaulting to no-operation (NOP) logger implementation
2024-03-18 21:11:29 SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
2024-03-18 21:11:30 Exception in thread "main" java.lang.reflect.InvocationTargetException
2024-03-18 21:11:30     at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
2024-03-18 21:11:30     at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:78)
2024-03-18 21:11:30     at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
2024-03-18 21:11:30     at java.base/java.lang.reflect.Method.invoke(Method.java:568)
2024-03-18 21:11:30     at org.springframework.boot.loader.launch.Launcher.launch(Launcher.java:91)
2024-03-18 21:11:30     at org.springframework.boot.loader.launch.Launcher.launch(Launcher.java:53)
2024-03-18 21:11:30     at org.springframework.boot.loader.launch.JarLauncher.main(JarLauncher.java:58)
2024-03-18 21:11:30 Caused by: java.lang.IllegalArgumentException: LoggerFactory is not a Logback LoggerContext but Logback is on the classpath. Either remove Logback or the competing implementation (class org.slf4j.helpers.NOPLoggerFactory loaded from jar:nested:/app.jar/!BOOT-INF/lib/slf4j-api-1.7.36.jar!/). If you are using WebLogic you will need to add 'org.slf4j' to prefer-application-packages in WEB-INF/weblogic.xml: org.slf4j.helpers.NOPLoggerFactory
2024-03-18 21:11:30     at org.springframework.util.Assert.instanceCheckFailed(Assert.java:592)
2024-03-18 21:11:30     at org.springframework.util.Assert.isInstanceOf(Assert.java:511)
2024-03-18 21:11:30     at org.springframework.boot.logging.logback.LogbackLoggingSystem.getLoggerContext(LogbackLoggingSystem.java:396)
2024-03-18 21:11:30     at org.springframework.boot.logging.logback.LogbackLoggingSystem.beforeInitialize(LogbackLoggingSystem.java:124)
2024-03-18 21:11:30     at org.springframework.boot.context.logging.LoggingApplicationListener.onApplicationStartingEvent(LoggingApplicationListener.java:238)
2024-03-18 21:11:30     at org.springframework.boot.context.logging.LoggingApplicationListener.onApplicationEvent(LoggingApplicationListener.java:220)
2024-03-18 21:11:30     at org.springframework.context.event.SimpleApplicationEventMulticaster.doInvokeListener(SimpleApplicationEventMulticaster.java:185)
2024-03-18 21:11:30     at org.springframework.context.event.SimpleApplicationEventMulticaster.invokeListener(SimpleApplicationEventMulticaster.java:178)
2024-03-18 21:11:30     at org.springframework.context.event.SimpleApplicationEventMulticaster.multicastEvent(SimpleApplicationEventMulticaster.java:156)
2024-03-18 21:11:30     at org.springframework.context.event.SimpleApplicationEventMulticaster.multicastEvent(SimpleApplicationEventMulticaster.java:138)
2024-03-18 21:11:30     at org.springframework.boot.context.event.EventPublishingRunListener.multicastInitialEvent(EventPublishingRunListener.java:136)
2024-03-18 21:11:30     at org.springframework.boot.context.event.EventPublishingRunListener.starting(EventPublishingRunListener.java:75)
2024-03-18 21:11:30     at org.springframework.boot.SpringApplicationRunListeners.lambda$starting$0(SpringApplicationRunListeners.java:54)
2024-03-18 21:11:30     at java.base/java.lang.Iterable.forEach(Iterable.java:75)
2024-03-18 21:11:30     at org.springframework.boot.SpringApplicationRunListeners.doWithListeners(SpringApplicationRunListeners.java:118)
2024-03-18 21:11:30     at org.springframework.boot.SpringApplicationRunListeners.starting(SpringApplicationRunListeners.java:54)
2024-03-18 21:11:30     at org.springframework.boot.SpringApplication.run(SpringApplication.java:326)
2024-03-18 21:11:30     at org.springframework.boot.SpringApplication.run(SpringApplication.java:1354)
2024-03-18 21:11:30     at org.springframework.boot.SpringApplication.run(SpringApplication.java:1343)
2024-03-18 21:11:30     at io.flowing.retail.inventory.InventoryApplication.main(InventoryApplication.java:10)
2024-03-18 21:11:30     ... 7 more
```

## Submission 05. March 2024
[Release 1.0](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/releases/tag/EDPO_A1_E1_2)

**@stefanmhsg**
- perform experiments in the lab repositories for E1
- write E1 submission documentation
- write E2 submssion documentation

**@luetzyas**
- create initial repository for project idea drop shipping
- implement new mailing service with basic functions