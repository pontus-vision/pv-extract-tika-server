#!/bin/bash

export TAG=${TAG:-1.13.2}
set -e
DIR="$( cd "$(dirname "$0")" ; pwd -P )"

export DOLLAR='$'
cat $DIR/Dockerfile.template | envsubst > $DIR/Dockerfile
cd $DIR/
#docker build --rm . -t pontusvisiongdpr/pontus-tika-server-lambda:${TAG}

if [[ -z $FORMITI_DEV_ACCOUNT ]]; then
  docker build  --rm . -t pontusvisiongdpr/pv-extract-tika-server-lambda:${TAG}
  docker push pontusvisiongdpr/pv-extract-tika-server-lambda:${TAG}
else
  if [[ $(aws --version 2>&1 ) == "aws-cli/1"* ]] ; then
    $(aws ecr get-login --no-include-email --region eu-west-2)
  else
    aws ecr get-login-password --region eu-west-2 | docker login --username AWS --password-stdin ${FORMITI_DEV_ACCOUNT}.dkr.ecr.eu-west-2.amazonaws.com
  fi

  docker build  --rm . -t pv-extract-tika-server-lambda:${TAG}
  TIMESTAMP=$(date +%y%m%d_%H%M%S)
  docker tag pv-extract-tika-server-lambda:${TAG} ${FORMITI_DEV_ACCOUNT}.dkr.ecr.eu-west-2.amazonaws.com/pv-extract-tika-server-lambda:${TAG}
  docker push ${FORMITI_DEV_ACCOUNT}.dkr.ecr.eu-west-2.amazonaws.com/pv-extract-tika-server-lambda:${TAG}
  IMAGE_SHA=$(aws ecr describe-images --repository-name pv-extract-tika-server-lambda --image-ids imageTag=${TAG} | jq -r '.imageDetails[0].imageDigest')
  aws lambda update-function-code --function-name pv-extract-tika-server-lambda  --image-uri ${FORMITI_DEV_ACCOUNT}.dkr.ecr.eu-west-2.amazonaws.com/pv-extract-tika-server-lambda@${IMAGE_SHA}

fi
