# 5. Add CRM (Customer Relation Management) Service

Date: 2024-05-16

## Status

Implemented

## Context

In the EDPO-SS24 Assignment 2 project, the current architecture encompasses services for: Checkout, Order, Inventory,
Payment, Shipping and Monitor. Each of these services plays a crucial role in the order fulfillment process.
This microservice listenes to events to store the customer data in the CRM service.

## Decision

The choice to incorporate the CRM Service, which uses Kafka to listen for events from the Order service, ensures that
customer data is stored in the CRM service.
The [CRM](../../../kafka/java/crm-camunda) service will subscribe to the topic: `flowing-retail`.
There will be a new camunda form to store or update customer data.
<br></br>
![enhanced Microservice overview](../../docs/kafka-services/add-crm-kafka-services.png)

### Granularity Disintegrators

- **Workflow and Choreography:**
  
- **Service Functionality:** 
- **Scalability Needs:** 
- **Fault Tolerance and Resilience:** 

## Consequences

xxx
