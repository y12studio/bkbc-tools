#!/bin/bash
set -e
# ----
# Removing All Containers and Images (Spring Cleaning)
# docker rm -f $(docker ps -a -q) ; docker rmi $(docker images -q -a) 
if [ "$1" = "info" ]; then
  /bitcoind-git/src/bitcoin-cli -conf=/.bitcoin/bitcoin.conf  getinfo
elif [ "$1" == "ip" ]; then
  /sbin/ifconfig eth0 | grep "inet addr" | awk -F: '{print $2}' | awk '{print $1}'
else
  echo "unknown command"
fi
