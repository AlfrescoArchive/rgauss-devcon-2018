# Overview

This project contains the artifacts to deploy the Alfresco Event Gateway PoC via Docker and Kubernetes.

# Build Docker Image

You must first build the jar artifact.  See [rgauss-devcon-2018-backend-service](../rgauss-devcon-2018-backend-service).

To build the Docker image run the script which gets the service jar:

```bash
./build-prep.sh
```

if building to deploy within Minikube:

```bash
eval $(minikube docker-env)
```

build the service Docker image:

```bash
docker build -t alfresco/rgauss-devcon-2018-backend-service:0.1-SNAPSHOT .
```

build the `inception-rest-tika` [Docker image](https://wiki.apache.org/tika/TikaAndVision#Step_1._Setup_REST_Server) (perhaps in a different directory):

```bash
git clone https://github.com/USCDataScience/tika-dockers.git && cd tika-dockers
docker build -f InceptionRestDockerfile -t uscdatascience/inception-rest-tika .
```

# Run via Helm

## Prerequisites

### Alfresco DBP Deployment

See [alfresco-dbp-deployment](https://github.com/Alfresco/alfresco-dbp-deployment).

It may be helpful to disable the components not needed for this example:

```bash
helm install alfresco-dbp \
--set alfresco-content-services.repository.environment.SYNC_SERVICE_URI="http://$ELBADDRESS:$INFRAPORT/syncservice" \
--set alfresco-activiti-cloud-gateway.enabled=false \
--set alfresco-activiti-cloud-registry.enabled=false \
--set alfresco-activiti-cloud-sso-idm.enabled=false
```

### Alfresco Event Gateway PoC

See [alfresco-event-gateway-poc](../alfresco-event-gateway-poc)

and be sure to set the `EVENTGATEWAYRELEASE` environment variable:

```bash
export EVENTGATEWAYRELEASE=smelly-cat
```

## Running

```bash
cd helm
helm depedency update rgauss-devcon-2018-backend-service
helm install rgauss-devcon-2018-backend-service --set eventgateway.kafka.host="$EVENTGATEWAYRELEASE-kafka" --set eventgateway.zookeeper.host="$EVENTGATEWAYRELEASE-zookeeper"
```
