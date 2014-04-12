Install hadoop 2.4.0 

[hadoop-common/BUILDING.txt at trunk · apache/hadoop-common](https://github.com/apache/hadoop-common/blob/trunk/BUILDING.txt)

## create droplets

Add SSH Keys/No root password 
boot 512MB Ram 20GB SSD Disk Singapore 1 Ubuntu 12.04.4 x64

## os setup

root@ssh key 
```
# apt-get update
# apt-get upgrade
# apt-get install linux-image-generic-lts-raring linux-headers-generic-lts-raring
# apt-get install build-essential
# apt-get install openjdk-7-jdk git maven
# apt-get install g++ autoconf automake libtool cmake zlib1g-dev pkg-config libssl-dev
# java -version
java version "1.7.0_51"
OpenJDK Runtime Environment (IcedTea 2.4.4) (7u51-2.4.4-0ubuntu0.12.04.2)
OpenJDK 64-Bit Server VM (build 24.45-b08, mixed mode)
# git -version
git version 1.7.9.5
# mvn -version
Apache Maven 3.0.4
Maven home: /usr/share/maven
Java version: 1.7.0_51, vendor: Oracle Corporation
Java home: /usr/lib/jvm/java-7-openjdk-amd64/jre
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "3.8.0-29-generic", arch: "amd64", family: "unix"
```

## sshd config

```
# cat /etc/ssh/sshd_config
...
PasswordAuthentication no
...
UsePAM no
# reload ssh
```
## proto buffer 2.5.0
```
# wget https://protobuf.googlecode.com/files/protobuf-2.5.0.tar.gz
# tar zxvr protobuf-2.5.0.tar.gz
# cd protobuf-2.5.0
# ./configure
# make
# make check
# make install
# ldconfig
# protoc --version
libprotoc 2.5.0

```
## user and group

```
# addgroup hadoop
# adduser --ingroup hadoop hduser
# adduser hduser sudo
```

## install hadoop

```
$ git clone https://github.com/apache/hadoop-common
$ cd hadoop-common
$ git tag -l '*2.4.*'
$ git checkout release-2.4.0
$ mvn package -Pdist,native -DskipTests -Dtar
```

## configure 

TODO

## test

TODO
