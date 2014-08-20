## bitcoinjs

```
$ sudo npm -g install bitcoinjs-lib browserify uglify-js
bitcoinjs-lib@1.0.2 /usr/lib/node_modules/bitcoinjs-lib
¢u¢w¢w bs58@1.2.1
¢u¢w¢w bs58check@1.0.1
¢u¢w¢w bigi@1.1.0
¢u¢w¢w ecurve@1.0.0
¢u¢w¢w crypto-js@3.1.2-3
¢|¢w¢w crypto-browserify@3.0.0 (ripemd160@0.2.0, sha.js@2.1.6)
$ browserify -r bitcoinjs-lib -s Bitcoin | uglifyjs > bitcoinjs-1.0.2.min.js
```