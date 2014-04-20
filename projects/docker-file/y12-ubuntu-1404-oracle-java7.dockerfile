# ubuntu 14.04 Java 
# Make base image first
# sudo apt-get install bootstrap
# sudo /usr/share/docker.io/contrib/mkimage-debootstrap.sh y12/ubuntu trusty
# 
FROM y12/ubuntu:trusty
MAINTAINER Y12STUDIO
RUN locale-gen en_US.UTF-8
RUN apt-get install -y software-properties-common
RUN add-apt-repository -y ppa:webupd8team/java
RUN apt-get update
RUN echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections
RUN echo debconf shared/accepted-oracle-license-v1-1 seen true | debconf-set-selections
RUN apt-get install -y oracle-java7-installer
RUN apt-get install -y git curl
