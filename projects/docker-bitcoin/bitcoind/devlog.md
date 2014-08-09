

move to phusion/baseimage

$ cd /git/bkbc-tools/projects/docker-bitcoin/bitcoind
$ sudo docker build test/bitcoind .
$ sudo docker run -i -t test/bitcoind /bin/bash
# my_init &
# top
# netstat -at
# /bitcoind-git/src/bitcoin-cli -conf=/.bitcoin/bitcoin.conf  getinfo
{
    "version" : 90201,
    "protocolversion" : 70002,
    "blocks" : 65892,
    "timeoffset" : 2,
    "connections" : 8,
    "proxy" : "",
    "difficulty" : 23.50125722,
    "testnet" : false,
    "relayfee" : 0.00001000,
    "errors" : ""
}

$ sudo docker run -d test/bitcoind
curl --fail -L -O https://github.com/phusion/baseimage-docker/archive/master.tar.gz && \
tar xzf master.tar.gz && \
$ sudo ./baseimage-docker-master/install-tools.sh
$sudo docker-ssh YOUR-CONTAINER-ID

# top
# netstat -at
# /bitcoind-git/src/bitcoin-cli -conf=/.bitcoin/bitcoin.conf  getinfo

# ls /.bitcoin/
bitcoin.conf  blocks  chainstate  debug.log
# exit

$ sudo docker-ssh $CID cat /root/cli
....
$ sudo docker-ssh $CID /root/cli info
$ sudo docker-ssh $CID /root/cli ip




--------------------------
lin@ubuntu73:~/git/bkbc-tools/projects/docker-bitcoin/bitcoind$ sudo docker build --rm -t test/bitcoind .

lin@ubuntu73:~/git/bkbc-tools/projects/docker-bitcoin/bitcoind$ sudo docker run -i -a stdout test/bitcoind cat env.sh
export HOST_DATA_DIR_PREFIX="/var/lib/bitcoind-docker"
export HOST_BITCOIND_DATA_DIR="$HOST_DATA_DIR_PREFIX/bitcoind"
export HOST_BITCOIND_P2P_PORT=8333
export HOST_BITCOIND_RPC_PORT=8332

export BITCOIND_RPC_USER=
export BITCOIND_RPC_PASSWORD=
export BITCOIND_CONFIG=

export BITCOIND_NODE_CONTAINER_NAME=bitcoind
export BITCOIND_RPC_CONTAINER_NAME=bitcoind-rpc


lin@ubuntu73:~/git/bkbc-tools/projects/docker-bitcoin/bitcoind$ sudo docker run -i -a stdout test/bitcoind cat bitcoindctl.sh | bash
+ echo sudo docker run -v :/bitcoind/data -e BITCOIND_EXTERNAL_IP=192.168.2.73 -e BITCOIND_RPC_USER= -e BITCOIND_RPC_PASSWORD= -e BITCOIND_CONFIG=
sudo docker run -v :/bitcoind/data -e BITCOIND_EXTERNAL_IP=192.168.2.73 -e BITCOIND_RPC_USER= -e BITCOIND_RPC_PASSWORD= -e BITCOIND_CONFIG=

lin@ubuntu73:~/git/bkbc-tools/projects/docker-bitcoin/bitcoind$ cat bitcoindctl.sh | bash
+ echo sudo docker run -v :/bitcoind/data -e BITCOIND_EXTERNAL_IP=192.168.2.73 -e BITCOIND_RPC_USER= -e BITCOIND_RPC_PASSWORD= -e BITCOIND_CONFIG=
sudo docker run -v :/bitcoind/data -e BITCOIND_EXTERNAL_IP=192.168.2.73 -e BITCOIND_RPC_USER= -e BITCOIND_RPC_PASSWORD= -e BITCOIND_CONFIG=

lin@ubuntu73:~/git/bkbc-tools/projects/docker-bitcoin/bitcoind$ sudo docker run -i -a stdout test/bitcoind cat bitcoindctl.sh serve | bash
+ echo sudo docker run -v :/bitcoind/data -e BITCOIND_EXTERNAL_IP=192.168.2.73 -e BITCOIND_RPC_USER= -e BITCOIND_RPC_PASSWORD= -e BITCOIND_CONFIG=
sudo docker run -v :/bitcoind/data -e BITCOIND_EXTERNAL_IP=192.168.2.73 -e BITCOIND_RPC_USER= -e BITCOIND_RPC_PASSWORD= -e BITCOIND_CONFIG=

