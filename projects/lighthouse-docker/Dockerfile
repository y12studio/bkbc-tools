FROM ubuntu:14.04
MAINTAINER Y12STUDIO <y12studio@gmail.com>

RUN locale-gen --no-purge en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US.UTF-8
ENV LC_ALL en_US.UTF-8
RUN update-locale

ENV DEBIAN_FRONTEND noninteractive
ENV HOME /root

RUN apt-get -y update
RUN apt-get -y upgrade
RUN apt-get install -yq curl wget git software-properties-common

# Install Java.
RUN \
  echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
  add-apt-repository -y ppa:webupd8team/java && \
  apt-get update && \
  apt-get install -y oracle-java8-installer

ENV JAVA_HOME /usr/lib/jvm/java-8-oracle/

# install maven
ENV MAVEN_VERSION 3.2.3
ENV M2_HOME /opt/maven
ENV M2 $M2_HOME/bin

RUN wget -qO- http://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz  | tar xvz -C /tmp && \
    mv /tmp/apache-maven-* $M2_HOME && \
    chmod +x $M2/mvn

ENV PATH $PATH:$JAVA_HOME/bin:$M2

# install bitcoinj/updatefx/lighthouse

RUN cd /opt && git clone https://github.com/bitcoinj/bitcoinj && cd bitcoinj && mvn install -DskipTests
RUN cd /opt && git clone https://github.com/vinumeris/updatefx && cd updatefx && mvn install -DskipTests
RUN cd /opt && git clone https://github.com/vinumeris/lighthouse && cd lighthouse && mvn package -DskipTests

RUN apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*