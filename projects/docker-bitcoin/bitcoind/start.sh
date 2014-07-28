#!/bin/bash

set -e

if [ "$1" = "" ]; then
    cat <<EOF
No command specified. Valid commands include:

  * serve: Run the bitcoind node
  * rpc: Issue an RPC command

See https://github.com/binaryage/bitcoind-docker for details.
EOF
    exit 1
fi

cd /bitcoind

#
BITCOIND_PREFIX="bitcoind -conf=/bitcoind/bitcoind.conf -datadir=/bitcoind/data"

if [ "$1" == "rpc" ]; then
    set -x
    exec $BITCOIND_PREFIX -rpcconnect=$BITCOIND_PORT_8332_TCP_ADDR -rpcport=$BITCOIND_PORT_8332_TCP_PORT ${*:2}
elif [ "$1" == "serve" ]; then
    set -x
    exec $BITCOIND_PREFIX -printtoconsole
else
    echo $@ | exec bash
fi
