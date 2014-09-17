## HOW TO INSTALL ARCH LINUX and OpenBazaar

* download ARCH LINUX form [Downloads | Raspberry Pi](http://www.raspberrypi.org/downloads/)
* write the image to sd card 
* ssh user/pass = root/root

```
# pacman -Sy
# pacman -S sudo git curl wget
# git clone -v --depth 1 --branch master https://github.com/OpenBazaar/OpenBazaar
# cd OpenBazaar && ./configure.sh
...
# IP=$(/sbin/ifconfig eth0 | grep 'inet ' | awk '{print $2}')
# ./run.sh -j --disable-open-browser -k $IP -q 8888 -p 12345; tail -f logs/production.log
# 
```