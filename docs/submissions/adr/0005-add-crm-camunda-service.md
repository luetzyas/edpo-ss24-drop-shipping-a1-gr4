# 5. Add CRM (Customer Relation Management) Service

Date: 2024-05-16

## Status

Implemented

## Context

In the EDPO-SS24 Assignment 2 project, the current architecture encompasses services for: Checkout, Order, Inventory,
Payment, Shipping and Monitor. Each of these services plays a crucial role in the order fulfillment process.
This microservice listens to events to store the customer data in the CRM service.

## Decision

The choice to incorporate the CRM Service, which uses Kafka to listen for events from the Order service, ensures that
customer data is stored in the CRM service.
The [CRM](../../../kafka/java/crm-camunda) service will subscribe to the topic: `flowing-retail`.
There will be a new camunda form to store or update customer data.
To have some testdata, the CRM service will be filled with some test data from an XML-file during Springboot start
application.
<br></br>
![enhanced Microservice overview](../../docs/kafka-services/add-crm-kafka-services.png)

### Granularity Disintegrators

- **Workflow and Choreography:**
  This microservice is not orchestrated by any other service, which ensures that the customer data is stored in the CRM.
- **Service Functionality:**
  The CRM Service adheres to the single-responsibility principle, taking on all responsibilities related to the customer
  data, which streamlines the development and maintenance of this specific functionality. 
- **Scalability Needs:**
  The CRM Service can be scaled independently of other services, ensuring responsiveness during peak customer data 
  activity. The CRM service will be filled with some test data from an XML-file during Springboot start application.  
- **Fault Tolerance and Resilience:**
  The CRM Service must robustly handle message loss, duplication, or communication failures, which are inherent risks when 
  dealing with Kafka and customer data environments.

## Consequences

To centralize the CRM service makes it more accessible and usable across the application, as it has one source of truth for
customer data. This addition is expected to align with the overall system's performance objectives and contribute to a more
resilient and adaptable architecture. But the data that will be stored has to be validated and checked for correctness.

Everytime the application has new customer data it has to be checked if the customer is already stored in the CRM service.
If not, the customer has to be stored in the CRM service. If the customer is already stored in the CRM service, the data
has to be updated. This could be done in a camunda form. 

For the whole order process it also has to be checked that the data is not a duplicate across the services. This could be
done by a unique identifier for the customer, maybe an email could be used for this. 