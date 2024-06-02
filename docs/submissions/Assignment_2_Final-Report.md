# University of St.Gallen - Exercise Submission

## Course Information

- **Course:** Event-driven and Process-oriented Architectures FS2024
- **Instructors:** B. Weber, R. Seiger, A. Abbad-Andaloussi

## Deadline

- **Submission Date:** 02.05.2024; 23:59 CET
- **[Work distribution](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/blob/master/docs/submissions/change_log.md)**

# Final Report

## Code

[Release]()

The [README.md](/kafka/java/mailing/README.md) file provides detailed description of implementation.

## General Project Description

What Services did we implement?
Here you can see an enhanced diagram of the flowing-retail application with two additional microservices: Mailing and
Factory

The mailing service is choreographed and is listening to all events happening in the flowing-retail process. Its primary
role is to keep our customers informed every step of the way by dispatching timely update emails in response to various
events triggered throughout the retail process.

Under the choreography section, we have the Checkout Service, which was enhanced to initiate the flowing retail
processing via a camunda form (which you will see in the next slide)

Transitioning to the orchestration aspect of our enhancements, we introduce the VGR, or smart factory service. With this
service we ensure that the order’s lifecycle is monitored from the factory onwoards. Furthermore, this service actively
reacts to order updates and inventory changes within the smart factory setting

The Services Inventory, Payment, Factory and Shipping are all orchestrated by the order service.

Additionally we enhanced the inventory and factory service with MQTT which is subscribed to the smart factory topics
“f/i/stock” for inventory and “f/i/order” for the factory service.

For the second part of the course, we implemented a new microservice, the CRM service, which manages customer data. 
This service has some data stored in a local database and is also connected to a Kafka topic to receive updates from the
other services.

We introduced Topology classes to track daily orders and item counts by product type. Additionally, 
a join-topology allows us to enrich orders with customer data from our new customer service.
If the customer exists in the database, it will be enriched; otherwise, if the customer cannot be found, we will use a placeholder.

> avro

> monitor enhancements

![kafka](../docs/kafka-services/add-crm-kafka-services.png)

### Where does the Choreography end and orchestration start

In our Flowing Retail application, we delve into the rich landscape of service coordination, articulating the nuanced
dance between Orchestration and Choreography. The Checkout Service, initiates the sequence of events to start processing
the order.

Once the order is placed, it simultaneously demonstrates the concept of Orchestration. Due to high semantic coupling it
acts with authoritative knowledge of the entire process flow, conducting each ensuring the order's lifecycle is followed
meticulously. (This approach is particularly useful for complex business transactions requiring coordinated steps and
when consistency is paramount.)

Complementing this is our Mailing Service, which adheres to the principles of Choreography, responding in a
decentralized manner to events as they occur.

## Workflow
![workflow](../docs/workflow.png) // TODO enhnace 

## Concepts

### State 
**Stateless and Stateful Event Processing:** The topologies within the Flowing Retail application include both 
stateless and stateful operations. Stateless operations, such as map and filter, do not maintain any state between events. 
Stateful operations, such as aggregate, join, and reduce, require maintaining state information across multiple events, 
which is stored in Kafka Streams state stores.


### Serialization and Deserialization
**JSON:** In most cases, we use Serde (Serializer/Deserializer) for JSON serialization and deserialization. 
JSON Serde is used for simple data types and structures where schema evolution is not a major concern.

**Avro:** The EnrichedOrder class is serialized and deserialized using Avro. 
Avro is a popular choice for serialization because it supports schema evolution, 
which is important for maintaining backward and forward compatibility. 
The schema is stored in the schema registry, which ensures consistency across producers and consumers.


### Time Semantics
**Event Time:** The time when the event actually occurred and was created by the source 

**Processing Time:** The time when the stream processing app processes the event

**Log Append Time:** The time when the event arrives in the Kafka topic 

**Time windows:** Tumbling Windows: These are fixed-size, non-overlapping windows. 
Tumbling windows are used to segment the data stream into distinct chunks for aggregation. 
In the Flowing Retail application, tumbling windows are used at various stages in the topologies 
to aggregate and process data over fixed intervals.


### Stream Table Duality
**KTable:** A KTable is a changelog stream where each data record represents an update (insert, update, delete) 
of a primary-keyed table. KTable is used for stateful processing and joins.

**GlobalKTable:** A GlobalKTable is similar to a KTable but is replicated on all instances of the Kafka Streams application.

**KStream:** A KStream is a record stream of key-value pairs, where each record is treated as an independent event. 
KStream is used for stateless transformations and simple processing tasks.


### Processing Guarantees
- At least once
- Exactly once
- At most once



## CRM Service
> new Microservice to manage customer data



## Topologies


## Reflections and lessons learned
> Kafka Streams

> Avro
 
> Others


**Team Collaboration and Workflow**
xxxx

