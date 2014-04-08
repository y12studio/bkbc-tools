import 'dart:html';
import 'dart:convert';

void main() {
  querySelector("#sample_text_id")
      ..text = "Click me!"
      ..onClick.listen(reverseText);
  
  HttpRequest.getString("http://192.168.2.71:8080/t.json").then((str){
      Map rmap = JSON.decode(str);
      print(rmap);
  });
}

void reverseText(MouseEvent event) {
  var text = querySelector("#sample_text_id").text;
  var buffer = new StringBuffer();
  for (int i = text.length - 1; i >= 0; i--) {
    buffer.write(text[i]);
  }
  querySelector("#sample_text_id").text = buffer.toString();
}
