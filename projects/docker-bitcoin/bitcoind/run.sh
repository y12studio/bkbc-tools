#!/bin/bash
# override from host -v /host/path/data:/hconf
if [ -f /hconf/bitcoin.conf ]
then
    cp -u /hconf/bitcoin.conf /.bitcoin/bitcoin.conf
fi
/bitcoind-git/src/bitcoind -conf=/.bitcoin/bitcoin.conf >>/var/log/bitcoind.log 2>&1
