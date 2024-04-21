# 0. Title

Date: 2022-10-05

## Status

Submitted to decision due 30.09.2023 / Approved / Suspended / Rejected / Deprecated 

## Context

Forces at play

## Decision

Response to the forces with justification

### Granularity Disintegrators

**Service functionality:**
Is service doing too many unrelated things? Single-responsibility / single purpose principle.

**Service Volatility:**
Are changes isolated to only one part of the service?

**Scalability and Throughout:**
Do parts of the service need to scale differently?

**Fault Tolerance:**
Are there errors that cause critical functions to fall within the service? 

**Data security:**
Do some parts of the service need higher security levels than others?

**Extensibility:** Is the service always expanding to add new context?

### Granularity Integrators

Database transactions: 	Is an ACID transaction required between two distinct services? Note: ACID is atomicity, consistency, isolation, and durability
Workflow and Choreography:
-	Do Services need to talk to each other?
-	Fault tolerance: will a failure in service cause all services to become nonoperational?
-	Responsiveness and performance: what is the latency per request?
-	Reliability and data integrity: Would data need to be rolled back through a compensating transaction?
Shared code: Do services need to share code among one another?
Data relationships: Although a service can be broken apart, can the data it uses be broken apart as well?


## Consequences

Context after the decision is applied. These trade-offs could be cost-based or trade-offs against other architecture characteristics (“-ilities”) (P.289, Richards)
