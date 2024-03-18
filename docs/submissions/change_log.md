# Changelog Group 4
This log summarizes all notable changes made to the project from Week 6 onwards to facilitate easy reading and evaluation. The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

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

## Submission 19. March 2024
[Release 2.0]()

**@stefanmhsg**
- draft README.md file for the Mailing Service
- draft BPMN diagram
- enhance the experiments for E1
  - E01: Outage of Zookeeper (added: tried to modify topic while Zookeeper is down)
  - E06: Message Retention (new)
- add new service vgr-camunda
- adapt order-service to include vgr-flow

**@luetzyas** 
- create changelog for group project
- enhance the experiment for E1 and enhance E1 and E2 documentation with results
  - what happens if 1 consumer is shut down
  - implement linger.ms to see if the latency behaviour changes
  - enhance conclusion for the experiment: Read from 0
- work on commands file for own project shortcuts
- implement eventhandling in mailing service for OrderPlacedEvent, PaymentReceivedEvent, GoodsShippedEvent and OrderCompletedEvent
- rework whole project because of this error occuring in unchanged services: ()
```
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