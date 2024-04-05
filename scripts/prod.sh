#!/bin/bash

cd ..
./gradlew clean build
java -Djarmode=layertools -jar build/libs/class-registration-1.0-SNAPSHOT.jar extract
docker build -f Dockerfile.prod -t team-b-server-prod .
docker run -it -p 8080:8080 --rm --name team-b-server-prod team-b-server-prod