# University of St.Gallen - Exercise Submission

## Course Information

- **Course:** Event-driven and Process-oriented Architectures FS2024
- **Instructors:** B. Weber, R. Seiger, A. Abbad-Andaloussi


# Flowing Retail >> Drop Shipping Group 4

This sample application demonstrates a simple order fulfillment system, decomposed into multiple independent components (like _microservices_).

## Lab04 

To run the **Choreography-based** version of the Flowing Retail project for lab04 you first need to be sure that all
the relevant projects have been built at least once:

```
  $ cd .\kafka\java\
  $ mvn clean install
```

Then you can execute:

```
  $ docker-compose -p docker-choreography -f docker-compose-kafka-java-choreography.yml up
```
from the directory [runner/docker-compose](runner/docker-compose).

## Lab07

To run the **Camunda-based** version of the Flowing Retail project for lab07 you need to execute:

```
  $ docker-compose -p docker-order-camunda -f docker-compose-kafka-java-order-camunda.yml up
```
from the directory [runner/docker-compose](runner/docker-compose).

## Lab11

To run the **Zeebe-based** version of the Flowing retail project for lab11 you need to execute:
```
  $ docker-compose -p docker-order-zeebe -f docker-compose-kafka-java-order-zeebe.yml up
```
from the directory [runner/docker-compose](runner/docker-compose). This uses the [order-zeebe](kafka/java/order-zeebe) project for handling the order workflow.
Hint: If you want to use the local Zeebe services you need to have the ''Self-managed local Zeebe'' configuration
active in your [application.properties](/kafka/java/order-zeebe/src/main/resources/application.properties) file. If you want
to use the hosted Camunda Cloud configuration you need to configure your Camunda Cloud credentials here.

---
The rest of this README file is the original one provided by Bernd Rücker.

The repository contains code for multiple implementation alternatives to allow a broad audience to understand the code and to compare alternatives. The [table below](#alternatives) lists these alternatives.

The example respects learnings from **Domain Driven Design (DDD)**, Event Driven Architecture (EDA) and **Microservices (µS)** and is designed to give you hands-on access to these topics.

**Note:** The code was written in order to be explained. Hence, I favored simplified code or copy & paste over production-ready code with generic solutions. **Don't consider the coding style best practice! It is purpose-written to be easily explainable code**.

You can find more information on the concepts in the [Practical Process Automation](https://processautomationbook.com/) book with O'Reilly.

Flowing retail simulates a very easy order fulfillment system:

![Events and Commands](docs/docs/workflow-in-service.png)

<a name = "alternatives"></a>

## Architecture and implementation alternatives

The most fundamental choice is to select the **communication mechanism**:

* **[Apache Kafka](kafka/)** as event bus (could be easily changed to messaging, e.g. RabbitMQ): [](docs/architecture.png)
* **[REST](rest/)** communication between Services.
  * This example also shows how to do **stateful resilience patterns** like **stateful retries** leveraging a workflow engine.
* **[Zeebe](zeebe/)** broker doing work distribution.

After the communication mechanism, the next choice is the **workflow engine**:

* **Camunda Platform**
* **Zeebe as managed service on Camunda Cloud**

and the **programming language**:

* **Java**
* **Go**
* **JavaScript / TypeScript**

## Storyline

Flowing retail simulates a very easy order fulfillment system. The business logic is separated into the services shown above (shown as a [context map](https://www.infoq.com/articles/ddd-contextmapping)).

### Long running services and orchestration

Some services are **long running** in nature - for example: the payment service asks customers to update expired credit cards. A workflow engine is used to persist and control these long running interactions.

### Workflows live within service boundaries

Note that the state machine (_or workflow engine in this case_) is a library used **within** one service. If different services need a workflow engine they can  run whatever engine they want. This way it is an autonomous team decision if they want to use a framework, and which one:

![Events and Commands](docs/docs/workflow-in-service.png)



## Links and background reading

* [Practical Process Automation](https://processautomationbook.com/) book
* Introduction blog post: https://blog.bernd-ruecker.com/flowing-retail-demonstrating-aspects-of-microservices-events-and-their-flow-with-concrete-source-7f3abdd40e53
* InfoQ-Writeup "Events, Flows and Long-Running Services: A Modern Approach to Workflow Automation": https://www.infoq.com/articles/events-workflow-automation

