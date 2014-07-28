#!/bin/bash
set -e
# IP address of current host
if [ "$(uname)" == "Darwin" ]; then
  export HOSTIP=$(/sbin/ifconfig | grep 'inet ' | grep -v '127.0.0.1' | head -n1 | awk '{print $2}')
else
  export HOSTIP=$(/sbin/ifconfig eth0 | grep "inet addr" | awk -F: '{print $2}' | awk '{print $1}')
fi
#SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
#cd "$SCRIPT_DIR/.."
#. etc/env

START="\
  sudo docker run 
    -v $HOST_BITCOIND_DATA_DIR:/bitcoind/data \
    -e BITCOIND_EXTERNAL_IP=$HOSTIP \
    -e BITCOIND_RPC_USER="$BITCOIND_RPC_USER" \
    -e BITCOIND_RPC_PASSWORD="$BITCOIND_RPC_PASSWORD" \
    -e BITCOIND_CONFIG="$BITCOIND_CONFIG" \
    $DOCKER_RUN_OPTS \
"

if [ "$1" = "serve" ]; then
  START="$START -d -p $HOST_BITCOIND_P2P_PORT:8333 -p $HOST_BITCOIND_RPC_PORT:8332 --name $BITCOIND_NODE_CONTAINER_NAME"
fi
if [ "$1" = "rpc" ]; then
  START="$START -d=false --rm --link $BITCOIND_NODE_CONTAINER_NAME:bitcoind --name $BITCOIND_RPC_CONTAINER_NAME"
fi

set -x
#exec $START binaryage/bitcoind $@
echo $START