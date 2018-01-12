# Overview

Provides a service that:

* Monitors Alfresco events from the Event Gateway for addition of images
* Sends the image to [Apache Tika](https://tika.apache.org/) configured against [TensorFlow](https://www.tensorflow.org/) for auto tagging
* Starts an instance of the tag verification process
* Returns a history of past tagging results via REST API

# Tika / TensorFlow

Tika has a parser available which talks to a running [TensorFlow service via REST API](https://wiki.apache.org/tika/TikaAndVision#Step_1._Setup_REST_Server) which can be used via Docker.

If building to deploy within Minikube:

```bash
eval $(minikube docker-env)
```

then build the `inception-rest-tika` Docker image (perhaps in a different directory):

```bash
git clone https://github.com/USCDataScience/tika-dockers.git && cd tika-dockers
docker build -f InceptionRestDockerfile -t uscdatascience/inception-rest-tika .
```

# Configuration

The URI for the TensorFlow REST endpoint should be configured using the `apiBaseUri` param in `[tika-config-tflow-rest.xml](src/main/resources/tika/tika-config-tflow-rest.xml)`.

In a Kubernetes cluster that endpoint would be the name of the K8s service.

# Building

You must first install the `alfresco-event-gateway-poc-sdk` artifact.  See [alfresco-event-gateway-poc-sdk](../alfresco-event-gateway-poc-sdk).

Then build the service:

```bash
mvn clean package
```

Note that you'll need to add `-DskipTests` if you're not running the TensorFlow REST service above.

# Running

To manually run the service you must first have postgres available.  You could do this via Docker:

```bash
docker run -p 5432:5432 --name postgres postgres:9.4
```

Then run the service:

```bash
java -Dspring.datasource.username=postgres -Dkafka.host=localhost -jar target/rgauss-devcon-2018-backend-service-0.1-SNAPSHOT.jar
```

You can then access the REST API at `http://localhost:8080/recognition-results`.
