#!/bin/sh
set -o errexit

. ./build.properties

## Do we already have the tar?  It may be outdated...let's remove it.
if [ -f $UI_MAVEN_ARTIFACT_ID-*.tar ]; then
    rm $UI_MAVEN_ARTIFACT_ID-*.tar
fi

 mvn org.apache.maven.plugins:maven-dependency-plugin:2.8:copy \
    -Dartifact=$UI_MAVEN_GROUP_ID:$UI_MAVEN_ARTIFACT_ID:$UI_MAVEN_VERSION:tar \
    -DoutputDirectory=./
