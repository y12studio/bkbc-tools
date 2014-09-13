## How to build lighthouse client-0.1-SNAPSHOT-bundled.jar from Docker

```
$ git clone https://github.com/y12studio/bkbc-tools
$ cd bkbc-tools/project/lighthouse-docker
$ sudo docker build -t test/lh .
$ sudo docker run -v /tmp/lh:/mnt test/lh cp /opt/lighthouse/client/target/client-0.1-SNAPSHOT-bundled.jar /mnt
$ ls /tmp/lh
client-0.1-SNAPSHOT-bundled.jar
$ cd /tmp/lh
$ java -jar client-0.1-SNAPSHOT-bundled.jar
```

