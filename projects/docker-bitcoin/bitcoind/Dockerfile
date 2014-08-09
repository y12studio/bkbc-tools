FROM phusion/baseimage:0.9.12
MAINTAINER Y12STUDIO <y12studio@gmail.com>

ENV BITCOIND_VERSION 0.9.2
ENV HOME /root
ENV LANG zh_TW.UTF-8
ENV LC_ALL zh_TW.UTF-8
ENV DEBIAN_FRONTEND noninteractive
# Use UTF-8 locale inside the container
RUN locale-gen zh_TW.UTF-8 && echo 'LANG="zh_TW.UTF-8"' > /etc/default/locale
# Use the phusion baseimage's insecure key
RUN /usr/sbin/enable_insecure_key

RUN apt-get update && apt-get upgrade -y
RUN apt-get install -y git build-essential libtool autotools-dev autoconf pkg-config libssl-dev libboost-all-dev bsdmainutils

RUN git clone -b $BITCOIND_VERSION https://github.com/bitcoin/bitcoin.git /bitcoind-git
RUN cd /bitcoind-git && ./autogen.sh && ./configure --disable-wallet --without-gui && make -j4
RUN strip /bitcoind-git/src/bitcoind

EXPOSE 8333 8332

ADD bitcoin.conf /.bitcoin/bitcoin.conf
ADD run.sh /etc/service/bitcoind/run
ADD cli.sh /root/cli
ADD README.md /root/README.md
RUN chmod +x /etc/service/bitcoind/run && chmod +x /root/cli

CMD ["/sbin/my_init"]

# Clean up APT when done.
RUN apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

