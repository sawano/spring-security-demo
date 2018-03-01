#!/bin/bash -e

pushd "${BASH_SOURCE%/*}/../.."

IMAGE=sawano/ubuntu-openjdk8:latest

docker run -it --rm -v $PWD:/opt/build "${IMAGE}" /bin/bash -c "cd /opt/build; ./mvnw clean verify"
