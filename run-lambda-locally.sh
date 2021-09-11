#!/bin/bash

if [[ ! -d ~/.aws-lambda-rie ]]; then
  mkdir -p ~/.aws-lambda-rie && \
  curl -Lo ~/.aws-lambda-rie/aws-lambda-rie \
    https://github.com/aws/aws-lambda-runtime-interface-emulator/releases/latest/download/aws-lambda-rie && \
  chmod +x ~/.aws-lambda-rie/aws-lambda-rie               

fi

DOCKER_ID=$(docker run -p 8888:8888 -d -v ~/.aws-lambda-rie:/aws-lambda -p 9000:8080 \
  --entrypoint /aws-lambda/aws-lambda-rie pv-extract-tika-server-lambda:1.13.2  /lambda-entrypoint.sh com.pontusvision.tika.LambdaHandler::handleRequest)

echo docker logs -f ${DOCKER_ID}
echo docker rm -f ${DOCKER_ID}
