# Overview

This project contains the artifacts to deploy the Alfresco Event Gateway via Docker and Kubernetes.

# Build Docker Image

To build the Docker image run the script which gets the service jar:

```bash
./build-prep.sh
```

then:

```bash
docker build -t alfresco/alfresco-event-gateway-poc:0.1-SNAPSHOT .
```

# Run via Docker

You first need the ActiveMQ and Kakfa hosts.  If you run them via Docker and have named the containers you can get the host from Docker, i.e.:

```bash
ACTIVEMQ_HOST=`docker inspect -f "{{ .NetworkSettings.IPAddress }}" activemq`
KAFKA_HOST=`docker inspect -f "{{ .NetworkSettings.IPAddress }}" kafka`
```

Run the service Docker image on port 8585:

```bash
docker run -p8585:8080 \
-e KAFKA_HOST=$KAFKA_HOST \
-e KAFKA_PORT=9092 \
-e MESSAGING_BROKER_URL=amqp://$ACTIVEMQ_HOST:5672 \
--name alfresco-event-gateway-poc alfresco-event-gateway-poc:latest
```

# Run via Helm

See [helm/alfresco-event-gateway-poc](helm/alfresco-event-gateway-poc)