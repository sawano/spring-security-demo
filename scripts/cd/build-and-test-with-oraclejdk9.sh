#!/bin/bash -e

pushd "${BASH_SOURCE%/*}/../.."

IMAGE=sawano/ubuntu-oraclejdk9:local

# Create image if necessary
$(docker image inspect "${IMAGE}" >/dev/null 2>&1) || IMAGE_EXISTS=$? && true
if [[ "${IMAGE_EXISTS}" -ne 0 ]]; then
    echo "No local image found. Creating  image..."
    curl -O https://raw.githubusercontent.com/sawano/docker-java/master/ubuntu-oraclejdk9/Dockerfile
    docker build -t "${IMAGE}" .
    rm Dockerfile
fi

docker run -it --rm -v $PWD:/opt/build "${IMAGE}" /bin/bash -c "cd /opt/build; ./mvnw clean verify"
