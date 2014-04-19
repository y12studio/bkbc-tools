jetty workshop project

```
$git clone https://github.com/y12studio/bkbc-tools/
$cd bkbc-tools/projects/jetty-workshop/
$/usr/share/maven/bin/mvn -version
Apache Maven 3.0.4
Maven home: /usr/share/maven
Java version: 1.7.0_25, vendor: Oracle Corporation
Java home: /usr/lib/jvm/java-7-openjdk-amd64/jre
Default locale: zh_TW, platform encoding: UTF-8
OS name: "linux", version: "3.11.0-12-generic", arch: "amd64", family: "unix"

$/user/share/maven/bin/mvn package
$cd target
$java -jar xxx.jar
```

bowery

```
$ git clone https://github.com/y12studio/bkbc-tools/
$ cd bkbc-tools/projects/jetty-workshop/
$ cat bowery.json
{
  "y12jetty": {
    "image": "java7",
    "path": "target",
    "build": "mvn package",
    "test": "",
    "start": "java -jar bkbc-tool-jetty-workshop-latest.jar"
  }
}
$ mvn package
$ java -jar target/bkbc-tool-jetty-workshop-latest.jar
...
2014-04-19 16:27:19.378:INFO:oejs.ServerConnector:main: 
  Started ServerConnector@237dc815{HTTP/1.1}{0.0.0.0:8080}
...
$ bowery connect
Hey there XXX. Connecting you to Bowery now...
Uploading file changes to y12jetty.
Service y12jetty is available at http://y12jetty.53523556bc9476670c000010.boweryapps.com.
Service y12jetty upload complete. Syncing file changes.
```