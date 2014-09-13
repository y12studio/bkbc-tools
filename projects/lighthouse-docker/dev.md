```
$ sudo docker build -t test/lh .
$ sudo docker run test/lh mvn --version
Apache Maven 3.2.3 (33f8c3e1027c3ddde99d3cdebad2656a31e8fdf4; 2014-08-11T20:58:10+00:00)
Maven home: /opt/maven
Java version: 1.8.0_20, vendor: Oracle Corporation
Java home: /usr/lib/jvm/java-8-oracle/jre
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "3.13.0-24-generic", arch: "amd64", family: "unix"

$ sudo docker run -i -t test/lh /bin/bash

# ls -al /opt/lighthouse/client/target/
total 16488
drwxr-xr-x 8 root root     4096 Sep 13 10:57 .
drwxr-xr-x 5 root root     4096 Sep 13 10:57 ..
drwxr-xr-x 3 root root     4096 Sep 13 10:57 classes
-rw-r--r-- 1 root root 15468356 Sep 13 10:57 client-0.1-SNAPSHOT-bundled.jar
-rw-r--r-- 1 root root  1380274 Sep 13 10:57 client-0.1-SNAPSHOT.jar
drwxr-xr-x 3 root root     4096 Sep 13 10:57 generated-sources
drwxr-xr-x 3 root root     4096 Sep 13 10:57 generated-test-sources
drwxr-xr-x 2 root root     4096 Sep 13 10:57 maven-archiver
drwxr-xr-x 3 root root     4096 Sep 13 10:57 maven-status
drwxr-xr-x 3 root root     4096 Sep 13 10:57 test-classes
# ls -al /opt/lighthouse/server/target/
total 4240
drwxr-xr-x 6 root root    4096 Sep 13 10:57 .
drwxr-xr-x 4 root root    4096 Sep 13 10:57 ..
drwxr-xr-x 3 root root    4096 Sep 13 10:57 classes
drwxr-xr-x 3 root root    4096 Sep 13 10:57 generated-sources
drwxr-xr-x 2 root root    4096 Sep 13 10:57 maven-archiver
drwxr-xr-x 3 root root    4096 Sep 13 10:57 maven-status
-rw-r--r-- 1 root root   16251 Sep 13 10:57 original-server-0.1-SNAPSHOT.jar
-rw-r--r-- 1 root root 4298598 Sep 13 10:57 server-0.1-SNAPSHOT.jar

copy a file from a container
$ sudo docker run -v /tmp/lh:/mnt test/lh cp /opt/lighthouse/client/target/client-0.1-SNAPSHOT-bundled.jar /mnt
$ ls /tmp/lh
client-0.1-SNAPSHOT-bundled.jar




```