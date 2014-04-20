#
# Jetty App Dockerfile (mvn package on host)
# host
# https://github.com/y12studio/bkbc-tools/projects/jetty-workshop/
# https://github.com/y12studio/bkbc-tools/issues/27
# git clone https://github.com/y12studio/bkbc-tools
# cd projects/jetty-workshop/ && mvn package
# sudo docker.io build -t="y13/jettyapp" .
#

#FROM y12docker/ubuntu-java:oracle-java7
FROM bowery/java
MAINTAINER Y12STUDIO

USER root

# Install Application
ENV PREFIX /usr/local/y13app
ADD target/latest/bkbc-tool-jetty-workshop-latest.jar $PREFIX/app.jar
# Define default command.
# java -jar $PREFIX/app.jar
CMD ["java"]
