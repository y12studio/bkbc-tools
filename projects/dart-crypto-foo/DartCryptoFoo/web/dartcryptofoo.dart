// Copyright (c) 2014, y12studio.  Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// Apache license.
import 'dart:html';
import "package:crypto/crypto.dart";
import 'dart:typed_data';
import 'packages/cipher/impl/base.dart';
import 'packages/cipher/cipher.dart';
import 'cryptohelper.dart';
import 'dart:js' as js;
import 'dart:math';

// https://github.com/dart-lang/bleeding_edge/blob/master/dart/pkg/crypto/test/base64_test.dart

BlockCipher cipher;

void main() {
  initCipher();    
  mainForJsExport();  
  querySelector("#btnGenKey")..onClick.listen((e){
    genRsaKey();
  });
}

genRsaKey() {
  var now = new DateTime.now();
  var kp = CpHelper.rsaKeyGen(1024,'Time-${now.millisecondsSinceEpoch}-Ran-${new Random().nextInt(1000)}');
  querySelector('#taRsaJson')
  ..text = CpHelper.serializeRSAKeys(kp);
  
  RSAPublicKey rsaKey = kp.publicKey;
}



String y12AesFun(String plaintxt) {
  return CpHelper.encToHex(cipher,plaintxt);
}

mainForJsExport(){
 
   final List key = [0x00,0x11,0x22,0x33,0x44,0x55,0x66,0x77,0x88,0x99,0xAA,0xBB,0xCC,0xDD,0xEE,0xFF];
   cipher = CpHelper.createAesCipher(key);
   //print(CpHelper.encToHex(cipher,'Lorem ipsum dolor sit amet, consectetur adipiscing elit ........'));  
  js.context['y12AesFun'] = y12AesFun;
}

htmlLoad(){
  querySelector("#sample_text_id")
      ..text = "Click me!"
      ..onClick.listen(reverseText);  
}

//
// https://github.com/izaera/cipher/blob/master/test/test/src/helpers.dart#L56 
// = new Uint8List.fromList(s.codeUnits);
Uint8List createUint8ListFromString(String s) {
  var ret = new Uint8List(s.length);
  for( var i=0 ; i<s.length ; i++ ) {
    ret[i] = s.codeUnitAt(i);
  }
  return ret;
}


String formatBytesAsHexString(Uint8List bytes) {
  var result = new StringBuffer();
  for( var i=0 ; i<bytes.lengthInBytes ; i++ ) {
    var part = bytes[i];
    result.write('${part < 16 ? '0' : ''}${part.toRadixString(16)}');
  }
  return result.toString();
}

testAes(){
  initCipher();
  final List key = [0x00,0x11,0x22,0x33,0x44,0x55,0x66,0x77,0x88,0x99,0xAA,0xBB,0xCC,0xDD,0xEE,0xFF];
  BlockCipher cipher = CpHelper.createAesCipher(key);
  print(CpHelper.encToHex(cipher,'Lorem ipsum dolor sit amet, consectetur adipiscing elit ........'));  
  // 75020e0812adb36f32b1503e0de7a59691e0db8fd1c9efb920695a626cb633d6db0112c007d19d5ea66fe7ab36c766232b3bcb98fd35f06d27d5a2d475d92728
}


void reverseText(MouseEvent event) {
  String base64Target = 'hello y12 哈哈';
  String base64 = CryptoUtils.bytesToBase64(base64Target.codeUnits);
  querySelector("#result").text = base64;
  testStrintToUint8list(base64Target);
  testAes();
}

testStrintToUint8list(String target){
  print(target.codeUnits);
  print(new Uint8List.fromList(target.codeUnits));
  print(createUint8ListFromString(target));
}

