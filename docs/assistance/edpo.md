# Event-driven and Process-oriented Architecture

## Definitions

| Type     | Description                                                                                                                                                                                                      |
|----------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Producer | A producer publishes data to Kafka topics.                                                                                                                                                                       |
| Consumer | A consumer subscribes to topics and processes the feed of published messages.                                                                                                                                    |
| Broker   | A Kafka cluster consists of one or more servers, each of which is called a broker.                                                                                                                               |
| Topic    | A topic is a category or feed name to which records are published. Topics in Kafka are always multi-subscriber; that is, a topic can have zero, one, or many consumers that subscribe to the data written to it. |
| Partition| Topics may be divided into partitions to increase scalability. Each partition is an ordered, immutable sequence of records that is continually appended to.                                                      |
| Offset   | An offset is a unique identifier of a record within a partition. It denotes the position of a consumer in the partition.                                                                                         |
| Zookeeper| Zookeeper is used for managing and coordinating Kafka broker. It is a centralized service for maintaining configuration information, naming, providing distributed synchronization, and providing group services. |


## Group 4 Standard Constants
