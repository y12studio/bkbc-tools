FROM ubuntu:14.04
MAINTAINER Y12STUDIO <y12studio@gmail.com>

ENV BITCOIN_DOWNLOAD_VERSION 0.9.2.1

RUN apt-get update
RUN apt-get install -y wget build-essential make g++ python-leveldb libboost-all-dev libssl-dev libdb++-dev libtool autotools-dev autoconf libboost-all-dev bsdmainutils pkg-config

WORKDIR /tmp
RUN wget --no-check-certificate https://github.com/bitcoin/bitcoin/archive/v$BITCOIN_DOWNLOAD_VERSION.tar.gz

RUN tar xfz v$BITCOIN_DOWNLOAD_VERSION.tar.gz && mv bitcoin-$BITCOIN_DOWNLOAD_VERSION bitcoin

WORKDIR /tmp/bitcoin
RUN ./autogen.sh
RUN ./configure --disable-wallet
RUN make
RUN make install

ADD . /bitcoind
WORKDIR /bitcoind

EXPOSE 8333
EXPOSE 8332

ADD start.sh /start.sh
RUN chmod +x /start.sh

# just a check that bitcoind exists on path
RUN file `which bitcoind`

ENTRYPOINT ["/start.sh"]
