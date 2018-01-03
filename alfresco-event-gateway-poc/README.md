# Overview

Provides a service which routes (and redacts or enriches as needed) raw events from the internal Alfresco messaging infrastructure to other forms of public consumption such as Kafka streams.

# Setup

The services expects that the ActiveMQ broker of the Alfresco DBP is running and being fed raw events from the Alfresco repository.

Apache Kafka should also be running.

# Building

```bash
mvn clean package
```

Note that you'll need to add `-DskipTests` if you're not running ActiveMQ and Kafka.

TODO: First run fails, likely due to topic creation?

# Running

To manually run the service you must first have ActiveMQ and Kafka available.  You could do this via Docker:

```bash
docker run -p 5672:5672 -p 61616:61616 -p 8161:8161 \
--name activemq webcenter/activemq
```

```bash
docker run -p 2181:2181 -p 9092:9092 \
--env ADVERTISED_HOST=localhost \
--env ADVERTISED_PORT=9092 \
--name kafka spotify/kafka
```

Then run the service:

```bash
java -jar target/alfresco-event-gateway-poc-0.1-SNAPSHOT.jar
```

At that point you should be able to consume from Kafka.  For example, after [downloading the console tools](https://kafka.apache.org/downloads) you could run the consumer to display events received:

```bash
./kafka-console-consumer.sh --topic AlfrescoEvents --bootstrap-server localhost:9092
```
