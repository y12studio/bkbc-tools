#!/bin/bash
set -e
# ----
# override bitcoin.conf
# sudo docker run test/bitcoind cat /.bitcoin/bitcoin.conf > mybitcoin.conf
# nano mybitconf.conf
# sudo docker run -v `pwd`/mybitcoin.conf:/.bitcoin/bitcoin.conf test/bitcoind cat /.bitcoin/bitcoin.conf
# sudo docker run -d -v `pwd`/mybitcoin.conf:/.bitcoin/bitcoin.conf test/bitcoind
# Removing All Containers and Images (Spring Cleaning)
# docker rm -f $(docker ps -a -q) ; docker rmi $(docker images -q -a) 
if [ "$1" = "info" ]; then
  /bitcoind-git/src/bitcoin-cli -conf=/.bitcoin/bitcoin.conf  getinfo
elif [ "$1" == "readme" ]; then
  cat /root/README.md
elif [ "$1" == "ip" ]; then
  /sbin/ifconfig eth0 | grep "inet addr" | awk -F: '{print $2}' | awk '{print $1}'
else
  echo "unknown command"
fi
