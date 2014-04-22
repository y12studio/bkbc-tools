// Copyright (c) 2014, y12studio.  Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// Apache license.
library y12cryptohelper;

import "package:cipher/cipher.dart";
import 'dart:typed_data';
import 'packages/crypto/crypto.dart';
import "package:bignum/bignum.dart";
import 'dart:convert';

class CpHelper {

  // https://github.com/dart-lang/spark/blob/master/ide/app/lib/mobile/android_rsa_cipher.dart
  static AsymmetricKeyPair rsaKeyGen(int bits, String seedStr) {
    Digest digest = new Digest("SHA-3/256");
    Uint8List seed256 = digest.process(strToUint8List(seedStr));
    final rnd = new SecureRandom("AES/CTR/PRNG");
    var rsapars = new RSAKeyGeneratorParameters(new BigInteger("257"), bits, 90);
    // Using the random token from the device as the seed.
    ParametersWithIV rndParams = new ParametersWithIV(new KeyParameter(new Uint8List.view(seed256.buffer, 3, 16)), new Uint8List(16));
    rnd.seed(rndParams);
    var params = new ParametersWithRandom(rsapars, rnd);
    var kg = new KeyGenerator("RSA");
    kg.init(params);
    return kg.generateKeyPair();
  }

  static BlockCipher createAesCipher(List keyBytes) {
    final Uint8List key = new Uint8List.fromList(keyBytes);
    final params = new KeyParameter(key);
    BlockCipher cipher = new BlockCipher("AES")..init(true, params);
    return cipher;
  }

  static Uint8List hexStringToUint8List(String hex) {
    var result = new Uint8List(hex.length~/2);
    for( var i=0 ; i<hex.length ; i+=2 ) {
      var num = hex.substring(i, i+2);
      var byte = int.parse( num, radix: 16 );
      result[i~/2] = byte;
    }
    return result;
  }
  
  static Uint8List bigIntegerToBytes(BigInteger bi){
    return new Uint8List.fromList(bi.toByteArray());
  }


  static String encToHex(BlockCipher cipher, String plainText) {
    Uint8List cipherTextList = processAllBlocks(cipher, strToUint8List(plainText));
    return CryptoUtils.bytesToHex(cipherTextList);
  }

  static Uint8List strToUint8List(String s) {
    return new Uint8List.fromList(s.codeUnits);
  }

  static Uint8List processAllBlocks(BlockCipher cipher, Uint8List inp) {
    var out = new Uint8List(inp.lengthInBytes);
    for (var offset = 0; offset < inp.lengthInBytes; ) {
      var len = cipher.processBlock(inp, offset, out, offset);
      offset += len;
    }
    return out;
  }
  // copy from https://github.com/dart-lang/spark/blob/master/ide/app/lib/mobile/android_rsa_cipher.dart
  // Serializes the RSA private key and public key.
  static String serializeRSAKeys(AsymmetricKeyPair keys) {
    Map<String, String> cereal = new Map<String, String>();
    cereal['p'] = keys.privateKey.p.toString(16);
    cereal['q'] = keys.privateKey.q.toString(16);
    cereal['modulus'] = keys.privateKey.modulus.toString(16);
    cereal['privateexponent'] = keys.privateKey.exponent.toString(16);
    cereal['publicexponent'] = keys.publicKey.exponent.toString(16);
    return JSON.encode(cereal);
  }

  // copy from https://github.com/dart-lang/spark/blob/master/ide/app/lib/mobile/android_rsa_cipher.dart
  // Unserializes the RSA private key and public key.
  static AsymmetricKeyPair deserializeRSAKeys(String json) {
    Map<String, List<int>> cereal = JSON.decode(json);
    BigInteger p = new BigInteger(cereal['p'], 16);
    BigInteger q = new BigInteger(cereal['q'], 16);
    BigInteger modulus = new BigInteger(cereal['modulus'], 16);
    BigInteger publicExponent = new BigInteger(cereal['publicexponent'], 16);
    BigInteger privateExponent = new BigInteger(cereal['privateexponent'], 16);

    RSAPublicKey public = new RSAPublicKey(modulus, publicExponent);
    RSAPrivateKey private = new RSAPrivateKey(modulus, privateExponent, p, q);
    return new AsymmetricKeyPair(public, private);
  }
  
  // copy from https://github.com/dart-lang/spark/blob/master/ide/app/lib/mobile/android_rsa_cipher.dart
  // Returns a signature given a data buffer and a key.
   static ByteData sign(AsymmetricKeyPair keys, ByteData data) {
     // First we create a SecureRandom, using the latter 16 bytes of
     // the token as the seed. This is to keep it from being the same
     // as the key generator seed, which would have been the first 16.
     SecureRandom rnd = new SecureRandom('AES/CTR/PRNG');
     ParametersWithIV rndParams = new ParametersWithIV(
         new KeyParameter(new Uint8List.view(data.buffer, 4, 16)),
         new Uint8List(16));
     rnd.seed(rndParams);

     // Next we prepare the Signer.
     PrivateKeyParameter<RSAPrivateKey> pkParam =
         new PrivateKeyParameter<RSAPrivateKey>(keys.privateKey);
     ParametersWithRandom params = new ParametersWithRandom(pkParam,
         rnd);
     Signer signer = new Signer("SHA-1/RSA");
     signer.init(true, params);

     // And finally create the signature.
     RSASignature sig = signer.generateSignature(new Uint8List.view(data.buffer));
     return new ByteData.view(sig.bytes.buffer);
   }


}
