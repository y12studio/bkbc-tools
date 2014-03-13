import 'dart:html';
import 'dart:async';
import 'dart:convert';

WebSocket ws;
SpeechSynthesisUtterance speechSynthesisUtterance;

void main() {
  speechSynthesisUtterance = new SpeechSynthesisUtterance();
  speechSynthesisUtterance.lang = 'en-US';
  speechSynthesisUtterance.rate = 1;
  querySelector('#output').onClick.listen(speakBitcoin);
  initWebSocket(30);
}

outputMsg(String msg) {
  var output = querySelector('#output');
  var text = msg;
  if (!output.text.isEmpty) {
    text = "${text}\n${output.text}";
  }
  output.text = text;
}

speakBitcoin(MouseEvent event) {
  speechSynthesisUtterance.text = "Wait";
  window.speechSynthesis.speak(speechSynthesisUtterance);
}

void initWebSocket([int retrySeconds = 2]) {
  var reconnectScheduled = false;

  outputMsg("Connecting to ws://ws.blockchain.info/inv");
  ws = new WebSocket('ws://ws.blockchain.info/inv');

  void scheduleReconnect() {
    if (!reconnectScheduled) {
      new Timer(new Duration(milliseconds: 1000 * retrySeconds), () => initWebSocket(retrySeconds * 2));
    }
    reconnectScheduled = true;
  }

  ws.onOpen.listen((e) {
    outputMsg('Connected and wait..');
    ws.send('{"op":"unconfirmed_sub"}');
    //ws.send('{"op":"ping_block"}');
  });

  ws.onClose.listen((e) {
    outputMsg('Websocket closed, retrying in $retrySeconds seconds');
    scheduleReconnect();
  });

  ws.onError.listen((e) {
    outputMsg("Error connecting to ws");
    scheduleReconnect();
  });

  ws.onMessage.listen((MessageEvent e) {
    Map data = JSON.decode(e.data);
    Map x = data['x'];
    //int blockHeight = x['height'];
    //outputMsg('Received block height: ${blockHeight}');
    var inputs = x['inputs'];
    int btc = (inputs[0]['prev_out']['value']/100000/1000).toInt();
    if(btc>=1){      
        outputMsg('Tx Input BTC: ${btc}');
        speechSynthesisUtterance.text = "${btc} B T C";
        window.speechSynthesis.speak(speechSynthesisUtterance);
    }
  });
}
