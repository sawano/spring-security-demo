#!/bin/bash -e

pushd "${BASH_SOURCE%/*}/.."

IMAGE=sawano/ubuntu-openjdk8:latest
PORT=8081

docker run -it --rm -v $PWD:/opt/build -p 127.0.0.1:"${PORT}":"${PORT}" -e server_port=${PORT} "${IMAGE}" /bin/bash -c "cd /opt/build; ./mvnw clean spring-boot:run"

