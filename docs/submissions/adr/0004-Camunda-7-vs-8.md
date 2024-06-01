# 4. Camunda 7 vs 8

Date: 2024-05-09

## Status

Approved

## Context

When working with pbmn workflows, we have the choice between Camunda 7 and Camunda 8. Both have their advantages and
disadvantages.
In our EDPO-SS24 project, we have to decide which version we want to implement in our application.

- Camunda 7 is local
- Camunda 8 is cloud-native

## Decision

We want to test the Camunda 7 and 8 workflows. We wil test this by creating a mailing service double with zeebe.
The reason why we want to work with both version, is because we want to implement both versions and try them out.

## Granularity Disintegrators

### Camunda 7

- Service Functionality
    - Camunda 7 is designed primarily for workflow automation within monolithic architectures. This suggests it has a
      broader range of functionalities handling multiple tasks within one deployment.
- Service Volatility
    - Because it is more designed for monolithic architectures, changes in one part of the application might force
      redeplomyents on the entire system.
- Scalability and Throughput
    - Camunda 7 has some scalability limitations, because it operates in the monolith. It still can handle high loads,
      but is not as fluid or adaptable for microservice application.
- Fault Tolerance
    - Because of the monolithic architecture, a failure in one part of the service can cause the entire service to
      become nonoperational.
- Data Security
    - It requires more comprehensive management of security across system boundaries, because the deployment is not
      segmented into smaller services.
- Extensibility
    - Camunda 7 is not as easily extensible as Camunda 8, because it is not designed for cloud-native applications.

### Camunda 8

- Service Functionality
    - Camunda 8 is designed for cloud-native applications, which means it is more focused on a single task or purpose.
      This makes it more efficient and easier to maintain.
- Service Volatility
    - Changes are isolated to only one part of the service, which makes it easier to manage and maintain.
- Scalability and Throughput
    - Camunda 8 is designed for high throughput and scalability, which makes it more adaptable to changing loads and
      requirements.
- Fault Tolerance
    - Camunda 8 is designed for fault tolerance, which means that errors are isolated to one part of the service and do
      not affect the entire system.
- Data Security
    - Camunda 8 is designed for cloud-native applications, which means it has higher security levels than Camunda 7.
- Extensibility
    - Camunda 8 is more extensible than Camunda 7, which makes it easier to add new functionality and features.

## Consequences

### Camunda 7

The implementation of Camunda 7 will be done locally. This means that the deployment will be more complex and the
maintenance will be more difficult. The system will be less scalable and less adaptable to changing requirements. The
security will be more difficult to manage and the system will be less extensible. However, Camunda 7 is designed for
workflow automation within monolithic architectures, which means it has a broader range of functionalities handling
multiple tasks within one deployment. This can be beneficial for applications that require a wide range of features and
functionalities. 

### Camunda 8
The implementaiton of Camunda 8 will be done in the cloud. This means that the deployment will be easier and the 
maintenance will be more simple. The system will be more scalable and more adaptable to changing requirements. The 
security will be easier to manage and the system will be more extensible. Camunda 8 is designed for cloud-native
applications, which means it is more focused on a single task or purpose. This makes it more efficient and easier to
maintain. Camunda 8 is designed for high throughput and scalability, which makes it more adaptable to changing loads and
requirements. Camunda 8 is designed for fault tolerance, which means that errors are isolated to one part of the service
and do not affect the entire system.  Camunda 8 is designed for cloud-native applications, which means it has higher
security levels than Camunda 7. Camunda 8 is more extensible than Camunda 7, which makes it easier to add new functionality
and features. 


## Links

- [Camunda 7 Documentation](https://docs.camunda.org/manual/7.15/)
- [Camunda 8 Documentation](https://docs.camunda.io/)