#
# Jetty App Dockerfile
#
# https://github.com/y12studio/bkbc-tools/projects/jetty-workshop/
#

# Pull base image.
FROM y12docker/ubuntu-java:oracle-java7
MAINTAINER Y12STUDIO

USER root

# Install Application
RUN git clone https://github.com/y12studio/bkbc-tools
RUN cd projects/jetty-workshop/
RUN mvn package
# Define default command.
CMD ["java"]
