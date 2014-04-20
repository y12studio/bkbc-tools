#
# Jetty App Dockerfile
#
# https://github.com/y12studio/bkbc-tools/projects/jetty-workshop/
# https://github.com/y12studio/bkbc-tools/issues/27
# git clone https://github.com/y12studio/bkbc-tools
# cd projects/jetty-workshop/
# sudo docker.io build -t="y13/jettyapp" .
#

FROM y12docker/ubuntu-java:oracle-java7
MAINTAINER Y12STUDIO

USER root

RUN apt-get install git maven

# Install Application
ENV PREFIX /usr/local/y13app
ADD . $PREFIX
RUN cd $PREFIX && mvn package
# Define default command.
# java -jar $PREFIX/target/latest/bkbc-tool-jetty-workshop-latest.jar
CMD ["java"]
