#!/bin/sh
set -o errexit

. ./build.properties

mvn org.apache.maven.plugins:maven-dependency-plugin:2.8:copy \
    -Dartifact=$SERVICE_MAVEN_GROUP_ID:$SERVICE_MAVEN_ARTIFACT_ID:$SERVICE_MAVEN_VERSION \
    -DoutputDirectory=./
