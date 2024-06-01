# 3. Choreographed vs Orchestrated

Date: 2024-04-20

## Status

Implemented

## Context

In the EDPO-SS24 Assignment 1 project, we are presented with a variety of services that form part of an e-commerce
workflow: Checkout, Order, Mailing, Inventory, Payment, Factory, and Shipping.
These services are designed to interact with each other to process orders efficiently and reliably. Two distinct
patterns of microservices interaction are considered: Choreography and Orchestration. The former implies that each
service publishes events that other services listen to and react upon, creating a decentralized control system. The
latter means there is a central orchestrator service that directs the other services by sending explicit commands.

## Decision

After analyzing, we have decided to enhance and keep the hybrid approach where the
services: [Checkout](../../../kafka/java/checkout), [Mailing](../../../kafka/java/mailing), [Monitor](../../../kafka/java/monitor)
and [Order](../../../kafka/java/checkout) will employ a choreographed approach, subscribing to events and acting
independently, and the
services: [Inventory](../../../kafka/java/inventory), [Payment](../../../kafka/java/payment), [Factory](../../../kafka/java/vgr-camunda)
and [Shipping](../../../kafka/java/shipping) will be orchestrated due to their interdependencies and the critical
importance of their flow sequence.
<br></br>
![enhanved Microservice overview](../../docs/kafka-services/add-mailing-kafka-services.png)
Green = Choreography </br>
Blue = Orchestrated by the order service </br>
Grey = Monitor service listenes to events

## Granularity Disintegrators

- **Service functionality:**
  The Checkout and Mailing services are specialized and are better off with direct orchestration to ensure a consistent
  and controlled flow.
- **Service Volatility:**
  Orchestrated services are less likely to be affected by changes in other services, reducing the risk of cascading
  failures.
- **Scalability and Throughout:**
  The Payment, Factory, and Shipping services need to scale dynamically and independently, favoring a choreographed
  model.
- **Fault Tolerance:**
  A choreographed approach in Inventory, Factory, and Shipping allows for better fault isolation.
  <br></br>
- **Choreography:**
    - In choreographed interactions, each service reacts to *events* in the system independently. This requires a design
      where services are aware of the events but do not need direct knowledge of other services' internal workflows.
- **Orchestration:**
    - Orchestration inherently involves a central workflow logic. This central logic directs other services by telling
      them what to do (*commands*), which can simplify complex workflows and ensure that
      steps are followed in the correct order.

## Consequences

The workflow of orchestrated services will be steady and predictable, which is crucial for vital operations like mailing
and checkout. Contrarily, choreographed services will be able to function with greater independence and adaptability,
which will enable autonomous growth and development and benefit services such as shipping and inventory management.
Although this choice offers a customized solution to meet the diverse service requirements, it may increase the
complexity of managing various interaction patterns. 
