## beta version

```
$ git clone https://github.com/OpenBazaar/OpenBazaar
$ cd OpenBazaar && sudo docker build -t=test/ob .
$ nano Dockerfile
# install the missing libssl-dev package.
RUN apt-get install -y python-dev python-pip g++ libjpeg-dev wget git
RUN apt-get install -y libssl-dev


```