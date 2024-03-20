# Checkout-Camunda

The provided checkout services has been extended to make use of the Camunda Forms for the checkout instead of the previous implementation.

## Notes 
- Form is defined in [checkout-form.form](src/main/resources/checkout-form.form)
  - Form gets autodeployed by setting property `camunda.bpm.auto-deployment-enabled=true` and creating dir `src/main/resources/META-INF` with file `processes.xml` which can remain empty. 
- Process is defined in [checkout-flow.bpmn](src/main/resources/checkout-flow.bpmn)
- Camunda is enabled in [application.properties](src/main/resources/application.properties) and [pom.xml](pom.xml)
- http://localhost:8091/ now shows the Camunda Webapp instead of html checkout page