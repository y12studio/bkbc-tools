## beta version

test@do

```
$ git clone https://github.com/OpenBazaar/OpenBazaar
$ cd OpenBazaar
# docker build -t=test/ob .
# docker run -i -t test/ob /bin/bash
root@12cb00b6e3d7:/bazaar# bash run.sh
/usr/bin/python2
Production Mode
File ob.db does not exist. Running setup script.
DB_PATH db/ob.db
Traceback (most recent call last):
  File "node/tornadoloop.py", line 8, in <module>
    from crypto2crypto import CryptoTransportLayer
  File "/bazaar/node/crypto2crypto.py", line 21, in <module>
    from pybitcointools import *
ImportError: No module named pybitcointools

```
test new dockerfile
```
$ sudo docker run -i -t test/ob /bin/bash
root@790a5adde288:/bazaar# bash run.sh && tail -f /bazaar/logs/production.log
/usr/bin/python2
Production Mode
File ob.db does not exist. Running setup script.
DB_PATH db/ob.db
tail: cannot open '/bazaar/logs/production.log' for reading: No such file or directory
root@790a5adde288:/bazaar# SELECT * FROM settings WHERE market_id = '1' ORDER BY id ASC
PRIVATE KEY:  e49fefe26db335dffa558f4f2e3f791c76d98dfd001743b293480f8024eb62d5
PUBLIC KEY:  041fe62a6e26fb4a71a1635a127c4c90daca5445520a569fca50890b56688684f56ba30d0f828d99216d6b2f75bfcb68866d48f544f3c748ff7bb72bfd8bd659e1
SELECT * FROM settings WHERE market_id = '1' ORDER BY id ASC
SELECT order_id FROM orders WHERE market_id = '1' ORDER BY updated DESC LIMIT 0 , 10
SELECT * FROM peers WHERE market_id = '1' ORDER BY id ASC
known_peers ['tcp://seed.openbazaar.org:12345', 'tcp://seed2.openbazaar.org:12345']
Searching for myself
Setting up UPnP Port Map Entry...
Exception : No UPnP device discovered
UPnP TCP P2P Port Map configuration done (12345 -> 12345) => False
UPnP UDP P2P Port Map configuration done (12345 -> 12345) => False
Started OpenBazaar Web App at http://127.0.0.1:8888

root@790a5adde288:/bazaar#
root@790a5adde288:/bazaar# netstat -at
Active Internet connections (servers and established)
Proto Recv-Q Send-Q Local Address           Foreign Address         State
tcp        0      0 localhost:8888          *:*                     LISTEN
tcp        0      0 *:12345                 *:*                     LISTEN
tcp        0      0 790a5adde288:59255      kcvn-ps7c.accessd:12345 TIME_WAIT
tcp        0      0 790a5adde288:59259      kcvn-ps7c.accessd:12345 TIME_WAIT
tcp        0      0 790a5adde288:42623      bmrp-l7vz.accessd:12345 TIME_WAIT
tcp        0      0 790a5adde288:59257      kcvn-ps7c.accessd:12345 TIME_WAIT
tcp        0      0 790a5adde288:42619      bmrp-l7vz.accessd:12345 TIME_WAIT

```

test d140901

```
$ git clone https://github.com/OpenBazaar/OpenBazaar
$ cd OpenBazaar && sudo docker build -t=test/ob .
$ nano Dockerfile
# install the missing libssl-dev package.
RUN apt-get install -y python-dev python-pip g++ libjpeg-dev wget git
RUN apt-get install -y libssl-dev


```