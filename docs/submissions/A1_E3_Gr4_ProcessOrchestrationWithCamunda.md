# University of St.Gallen - Exercise Submission

## Course Information

- **Course:** Event-driven and Process-oriented Architectures FS2024
- **Instructors:** B. Weber, R. Seiger, A. Abbad-Andaloussi

## Deadline

- **Submission Date:** 19.03.2024; 23:59 CET
- **[Work distribution](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/blob/master/docs/submissions/change_log.md)**

# Exercise 03: Process Orchestration with Camunda


### Implementation of a process solution using Spring Boot and Camunda

#### Decision
We decided to extend the existing Flowing-Retail-Kafka-Camunda-Orchestrated project with a new service `VGR-Camunda` to implement an additional BPMN-based Process that extends the Order-Fulfillment process.


#### Rationale
As we aim to include the Smart-Factory highlevel dataset to facility stream processing, we start by implementing a Microservice that simulates the VGR (Vacuum-Gripper-Robot) in the Smart-Factory.


#### Design
Each service is configured to run its own instance of the Camunda BPMN engine.

#### Additional Considerations
The VGR process can be extended once we have decided on the states and actions of the VGR in the Smart-Factory Flow.
To make use of other BPMN elemnts, we have created a Camunda form for the Checkout service.

### Code
[Release](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/releases/tag/EDPO_A1_E3_4)

The [README.md](https://github.com/luetzyas/edpo-ss24-drop-shipping-a1-gr4/blob/master/kafka/java/vgr-camunda/README.md) file provides detailed description of VGR-Flow implementation.




