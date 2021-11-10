#!/usr/bin/env bash

SSH_NAME="wpiaws"
prod_PATH="/home/ec2-user/prod"
prod_NAME="prod"

staging_PATH="/home/ec2-user/staging"
staging_NAME="staging"

cd "${0%/*}"

ENV="$1"
case $ENV in
	"prod" | "staging")
		;;
	*)
		echo "Environment must be 'prod' or 'staging'"
		exit 1
		;;
esac


DESIRED_PATH="${ENV}_PATH"
DESIRED_PATH="${!DESIRED_PATH}"

DESIRED_NAME="${ENV}_NAME"
DESIRED_NAME="${!DESIRED_NAME}"

pushd ../ #navigate up to /frontend

#remove remote ...
ssh $SSH_NAME "rm -rf $DESIRED_PATH" # clean existing 
ssh $SSH_NAME "mkdir -p $DESIRED_PATH" # ensure installation path OK
rsync -av -e ssh --exclude='node_modules' . $SSH_NAME:$DESIRED_PATH #clone files
ssh $SSH_NAME "cd $DESIRED_PATH && cd .. && docker-compose -f managly-$DESIRED_NAME.yml up -d --build" #restart docker compose

popd
