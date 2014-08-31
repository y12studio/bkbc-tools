build issue

```
$ sudo docker build -t=test/ob .
...
amalgamation/sqlite3.c:13654:26: fatal error: openssl/rand.h: No such file or directory

 #include <openssl/rand.h>

                          ^

compilation terminated.

error: command 'x86_64-linux-gnu-gcc' failed with exit status 1
```

fixing

```
$ sed -i 's/wget git/wget git libssl-dev/g' Dockerfile
$ sudo docker build -t=test/ob .
...
Successfully built ...
```