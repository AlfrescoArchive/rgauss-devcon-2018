# Overview

We'd like to develop a custom application that uses image recognition to automatically tag images added to the Alfresco content repository, then start a process that requests verification of tags by a human before actually associating them with the content in the repository.

# Architecture

## [Backend Service](backend-service)

Monitors Alfresco events from the Event Gateway for addition of images, automatically tags them via image recognition, and starts an instance of the tag verification process.

It can also return a history of past tagging results via REST API.

## UI Application

End-users access the UI to verify tags and see the past history of auto tagging results. 

TODO: Architecture diagram

## PoC Alfresco Infrastructure

* Event Gateway
* API Gateway
* SSO / Identity

# Setup

## 1. Alfresco DBP Deployment

See [alfresco-dbp-deployment](https://github.com/Alfresco/alfresco-dbp-deployment).

## 2. Alfresco Event Gateway PoC

See [alfresco-event-gateway-poc](alfresco-event-gateway-poc/)

## 3. Build Tika / TensorFlow Docker Image



TODO: Helm chart

