## bitcoinjs

```
$ sudo npm -g install bitcoinjs-lib browserify uglify-js
bitcoinjs-lib@1.0.2 /usr/lib/node_modules/bitcoinjs-lib
├── bs58@1.2.1
├── bs58check@1.0.1
├── bigi@1.1.0
├── ecurve@1.0.0
├── crypto-js@3.1.2-3
└── crypto-browserify@3.0.0 (ripemd160@0.2.0, sha.js@2.1.6)
$ browserify -r bitcoinjs-lib -s Bitcoin | uglifyjs > bitcoinjs-1.0.2.min.js
```