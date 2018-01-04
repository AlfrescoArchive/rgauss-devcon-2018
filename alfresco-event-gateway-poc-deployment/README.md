# Overview

This project contains the artifacts to deploy the Alfresco Event Gateway PoC via Docker and Kubernetes.

# Build Docker Image

To build the Docker image run the script which gets the service jar:

```bash
./build-prep.sh
```

if building using to deploy within Minikube:

```bash
eval $(minikube docker-env)
```

build the Docker image:

```bash
docker build -t alfresco/alfresco-event-gateway-poc:0.1-SNAPSHOT .
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

and be sure to set the `DBPRELEASE` environment variable:

```bash
export DBPRELEASE=icy-dog
```

## Running

```bash
cd helm
helm repo add incubator http://storage.googleapis.com/kubernetes-charts-incubator
helm dependency update alfresco-event-gateway-poc
helm install alfresco-event-gateway-poc --set messaging.broker.url="amqp://$DBPRELEASE-alfresco-sync-service-activemq:5672"
```

Note that you may see a few warnings in Kubernetes regarding the persistent volume, but that typically resolves itself after a minute or two.  You may also see warnings in the gateway logs during creation of new topics as Kafka / Zookeeper sort out the leader.

## Troubleshooting

### Kafka

To debug Kafka inside the cluster, first get the release name of the event gateway:

```bash
export EVENTGATEWAYRELEASE=looping-giraffe
```

Then deploy a test Kafka client:

```bash
kubectl apply -f debug/kafka-client.yaml
```

You can then run various tools from inside the cluster, i.e.:

```bash
kubectl exec -ti kafkatestclient -- ./bin/kafka-topics.sh --zookeeper $EVENTGATEWAYRELEASE-zookeeper:2181 --list

kubectl exec -ti kafkatestclient -- ./bin/kafka-console-consumer.sh --zookeeper $EVENTGATEWAYRELEASE-zookeeper:2181 --topic alfresco.repo.events.nodes

kubectl exec -ti kafkatestclient -- ./bin/kafka-console-producer.sh --broker-list $EVENTGATEWAYRELEASE-kafka:9092 --topic alfresco.repo.events.nodes
```

### ActiveMQ

To view the admin console of ActiveMQ:

```bash
export ELBADDRESS=$(minikube ip);
export ACTIVEMQADMINPORT=$(kubectl get service $DBPRELEASE-alfresco-sync-service-activemq-admin -o jsonpath={.spec.ports[0].nodePort});
open http://$ELBADDRESS:$ACTIVEMQADMINPORT/admin
```