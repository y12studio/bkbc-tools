// Copyright (c) 2014, y12studio.  Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// Apache license.
library y12cryptohelper;

import "package:cipher/cipher.dart";
import "package:cipher/impl/base.dart";
import 'dart:typed_data';
import 'packages/crypto/crypto.dart';

class CpHelper {
  
  static BlockCipher createAesCipher(List keyBytes){
    final Uint8List key = new Uint8List.fromList(keyBytes);
    final params = new KeyParameter(key);
    BlockCipher cipher = new BlockCipher("AES")..init(true, params);
    return cipher;
  }
  
  static String encToHex(BlockCipher cipher, String plainText){
    Uint8List cipherTextList = processAllBlocks(cipher, strToUint8List(plainText));  
    return CryptoUtils.bytesToHex(cipherTextList);
  }
  
 static Uint8List strToUint8List(String s){
    return new Uint8List.fromList(s.codeUnits);
  }
  
 static Uint8List processAllBlocks( BlockCipher cipher, Uint8List inp ) {
    var out = new Uint8List(inp.lengthInBytes);
    for( var offset=0 ; offset<inp.lengthInBytes ; ) {
      var len = cipher.processBlock( inp, offset, out, offset );
      offset += len;
    }
    return out;
  }
  
}

