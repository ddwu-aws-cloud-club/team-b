#!/bin/bash

cd ..
./gradlew clean build
java -Djarmode=layertools -jar build/libs/class-registration-1.0-SNAPSHOT.jar extract
docker-compose up -d --build