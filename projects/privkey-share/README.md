java -jar bkbc-privkey-share-0.0.1-SNAPSHOT.jar -g -s 5 -t 3

## Usage 

usage: java -jar app.jar
 -g,--generate          generate private key sharing
 -h,--help              Print help.
 -s,--shares <int>      number of shares to generate
 -t,--threshold <int>   number of requires shares


## Licence 

threshold-secret-sharing © antik10ud

Distributed under MIT license

https://github.com/antik10ud/threshold-secret-sharing


## ref 

[資訊人權貴ㄓ疑: NSA 要求微軟安置在 Windows 裡的後門... 保護智財， 無可奉告](http://ckhung0.blogspot.tw/2013/07/nsakey.html)


## 測試遊戲：咖啡五分之三 Coffee3of5

java -jar app.jar -newuser akb48
user_akb48.json
publish id and publickey to facebook

java -jar app.jar -user akb48 -game cof1

grap http://xxx/cof1_game.json
check sharing private
print 2/5 or 1/5
print NOT WIN.





java -jar app.jar -newgame cof1

cof1_priv.json
cof1_pub.json

btc 產出地址存入 0.00200855 BTC

產出5份私鑰sharing

訂出開始報名/截止/計算 block height

取得參與名單

根據名單與目標 block hash 比大小

abs(doublehash(id+ran+blcokhash)-doublehash(blockhash))
使用公鑰加密發布 cof1_game.json

最接近兩名 PM 給1/5私鑰

發放五次後結束，可用策略談好10分鐘結盟平分？