## how to build image
```
git clone https://github.com/y12studio/bkbc-tools btools
cd btools/projects/docker-bitcoin/bitcoind/
sudo docker build -t test/bitcoind .
```
## run bitcoind
install docker-ssh from [phusion/baseimage-docker](https://github.com/phusion/baseimage-docker#login_ssh)
```
$ sudo docker run -d test/bitcoind
$ sudo docker-ssh $CID
# container inside
# top
# netstat -at
# /bitcoind-git/src/bitcoin-cli -conf=/.bitcoin/bitcoin.conf  getinfo
# ls /.bitcoin/
bitcoin.conf  blocks  chainstate  debug.log
# exit

$ sudo docker-ssh $CID cat /root/cli
...
$ sudo docker-ssh $CID /root/cli info
{
    "version" : 90201,
    "protocolversion" : 70002,
    "blocks" : 11015,
    "timeoffset" : 2,
    "connections" : 8,
    "proxy" : "",
    "difficulty" : 1.00000000,
    "testnet" : false,
    "relayfee" : 0.00001000,
    "errors" : ""
}
$ sudo docker-ssh $CID /root/cli ip
172.17.0.2
```