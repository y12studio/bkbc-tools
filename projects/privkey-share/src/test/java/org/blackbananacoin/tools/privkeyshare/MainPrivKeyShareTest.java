/*
 * Copyright 2014 Y12STUDIO
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.blackbananacoin.tools.privkeyshare;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.util.Date;

import org.bitcoinj.wallet.Protos;
import org.bitcoinj.wallet.Protos.ScryptParameters;
import org.junit.Before;
import org.junit.Test;
import org.spongycastle.bcpg.HashAlgorithmTags;
import org.spongycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.spongycastle.bcpg.sig.Features;
import org.spongycastle.bcpg.sig.KeyFlags;
import org.spongycastle.crypto.generators.RSAKeyPairGenerator;
import org.spongycastle.crypto.params.KeyParameter;
import org.spongycastle.crypto.params.RSAKeyGenerationParameters;
import org.spongycastle.openpgp.PGPEncryptedData;
import org.spongycastle.openpgp.PGPException;
import org.spongycastle.openpgp.PGPKeyRingGenerator;
import org.spongycastle.openpgp.PGPPublicKey;
import org.spongycastle.openpgp.PGPSignature;
import org.spongycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.spongycastle.openpgp.operator.PBESecretKeyEncryptor;
import org.spongycastle.openpgp.operator.PGPDigestCalculator;
import org.spongycastle.openpgp.operator.bc.BcPBESecretKeyEncryptorBuilder;
import org.spongycastle.openpgp.operator.bc.BcPGPContentSignerBuilder;
import org.spongycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;
import org.spongycastle.openpgp.operator.bc.BcPGPKeyPair;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.ECKey.ECDSASignature;
import com.google.bitcoin.core.Sha256Hash;
import com.google.bitcoin.core.Utils;
import com.google.bitcoin.crypto.EncryptedPrivateKey;
import com.google.bitcoin.crypto.KeyCrypterException;
import com.google.bitcoin.crypto.KeyCrypterScrypt;
import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;

public class MainPrivKeyShareTest {

	private KeyCrypterScrypt keyCrypter;

	@Before
	public void setUp() throws Exception {
		SecureRandom secureRandom = new SecureRandom();

		byte[] salt = new byte[KeyCrypterScrypt.SALT_LENGTH];
		secureRandom.nextBytes(salt);
		Protos.ScryptParameters.Builder scryptParametersBuilder = Protos.ScryptParameters
				.newBuilder().setSalt(ByteString.copyFrom(salt));
		ScryptParameters scryptParameters = scryptParametersBuilder.build();
		keyCrypter = new KeyCrypterScrypt(scryptParameters);

	}

	@Test
	public void testGenerateKey() {
		UserObj uo = MainPrivKeyShare.generateKey("haha123");
		assertNotNull(uo);
		String jstr = MainPrivKeyShare.toJson(uo);
		System.out.println(jstr);
		Gson gson = new Gson();
		UserObj uoTarget = gson.fromJson(jstr, UserObj.class);
		assertNotNull(uoTarget);
		KeyPair kp = MainPrivKeyShare.toKeyPair(uoTarget);
		assertNotNull(kp);
		String kpPriTarget = BaseEncoding.base64Url().encode(
				kp.getPrivate().getEncoded());
		String kpPubTarget = BaseEncoding.base64Url().encode(
				kp.getPublic().getEncoded());
		assertEquals(kpPriTarget, uo.getKeyPrivate());
		assertEquals(kpPubTarget, uo.getKeyPublic());
	}

	@Test
	public void testCreateSharing() {
		String r = MainPrivKeyShare.createSharing(5, 3);
		assertNotNull(r);
	}

	@Test
	public void testEncrypt() {
		UserObj uo = MainPrivKeyShare.generateKey("haha123");
		assertNotNull(uo);
		KeyPair kp = MainPrivKeyShare.toKeyPair(uo);
		assertNotNull(kp);
		byte[] encTarget = MainPrivKeyShare.encrypt("good".getBytes(),
				kp.getPublic());
		assertEquals(
				"good",
				new String(MainPrivKeyShare.decrypt(encTarget, kp.getPrivate())));
	}

	@Test
	public void testEcKeySign() {

		String password = "pass1234";
		String challenge = "challenge1234";

		byte[] secretTarget = Utils.doubleDigest(password.getBytes());
		ECKey key = new ECKey(new BigInteger(1, secretTarget), null, true);
		ECDSASignature sign = key.sign(Sha256Hash.createDouble(challenge
				.getBytes()));
		assertNotNull(sign);
		System.out.println("sign.r=" + sign.r);
		System.out.println("sign.s=" + sign.s);
		String signHex = BaseEncoding.base16().encode(sign.encodeToDER());
		System.out.println(signHex);

		byte[] sign2byte = BaseEncoding.base16().decode(signHex);
		ECDSASignature signTarget = ECDSASignature.decodeFromDER(sign2byte);
		assertFalse(ECKey.verify(
				Sha256Hash.createDouble("FakeChallenge".getBytes()).getBytes(),
				signTarget, key.getPubKey()));
		assertTrue(ECKey.verify(Sha256Hash.createDouble(challenge.getBytes())
				.getBytes(), signTarget, key.getPubKey()));

	}

	/**
	 * https://github.com/wesabe/grendel/blob/master/src/test/java/com/wesabe/grendel/openpgp/tests/KeySetGeneratorTest.java
	 * @throws PGPException
	 */
	@Test
	public void testOpenPgp() throws PGPException {
		String id = "haha@xxx.fg.xxx";
		String pass = "hello";
		int s2kcount = 0xc1;
		// http://bouncycastle-pgp-cookbook.blogspot.tw/2013/01/generating-rsa-keys.html

		RSAKeyPairGenerator kpg = new RSAKeyPairGenerator();
		kpg.init(new RSAKeyGenerationParameters(BigInteger.valueOf(0x10001),
				new SecureRandom(), 2048, 12));

		BcPGPKeyPair bkpSign = new BcPGPKeyPair(PGPPublicKey.RSA_SIGN,
				kpg.generateKeyPair(), new Date());

		assertNotNull(bkpSign);
		BcPGPKeyPair bkpEncrypt = new BcPGPKeyPair(PGPPublicKey.RSA_ENCRYPT,
				kpg.generateKeyPair(), new Date());

		assertNotNull(bkpEncrypt);

		PGPSignatureSubpacketGenerator signhashgen = new PGPSignatureSubpacketGenerator();

		signhashgen.setKeyFlags(false, KeyFlags.SIGN_DATA
				| KeyFlags.CERTIFY_OTHER);
		signhashgen.setPreferredSymmetricAlgorithms(false, new int[] {
				SymmetricKeyAlgorithmTags.AES_256,
				SymmetricKeyAlgorithmTags.AES_192,
				SymmetricKeyAlgorithmTags.AES_128 });
		signhashgen.setPreferredHashAlgorithms(false, new int[] {
				HashAlgorithmTags.SHA256, HashAlgorithmTags.SHA1,
				HashAlgorithmTags.SHA384, HashAlgorithmTags.SHA512,
				HashAlgorithmTags.SHA224, });

		signhashgen.setFeature(false, Features.FEATURE_MODIFICATION_DETECTION);

		PGPSignatureSubpacketGenerator enchashgen = new PGPSignatureSubpacketGenerator();
		enchashgen.setKeyFlags(false, KeyFlags.ENCRYPT_COMMS
				| KeyFlags.ENCRYPT_STORAGE);

		PGPDigestCalculator sha1Calc = new BcPGPDigestCalculatorProvider()
				.get(HashAlgorithmTags.SHA1);
		PGPDigestCalculator sha256Calc = new BcPGPDigestCalculatorProvider()
				.get(HashAlgorithmTags.SHA256);

		// bcpg 1.48 exposes this API that includes s2kcount. Earlier
		// versions use a default of 0x60.
		PBESecretKeyEncryptor pske = (new BcPBESecretKeyEncryptorBuilder(
				PGPEncryptedData.AES_256, sha256Calc))
				.build(pass.toCharArray());

		// Finally, create the keyring itself. The constructor
		// takes parameters that allow it to generate the self
		// signature.
		PGPKeyRingGenerator keyRingGen = new PGPKeyRingGenerator(
				PGPSignature.POSITIVE_CERTIFICATION, bkpSign, id, sha1Calc,
				signhashgen.generate(), null, new BcPGPContentSignerBuilder(
						bkpSign.getPublicKey().getAlgorithm(),
						HashAlgorithmTags.SHA1), pske);

		// Add our encryption subkey, together with its signature.
		keyRingGen.addSubKey(bkpEncrypt, enchashgen.generate(), null);
		assertNotNull(keyRingGen);
	}

	@Test
	public void testEncryptedEcKey() {

		String password = "12";

		ECKey unencryptedKey = new ECKey();
		KeyParameter aesKey = keyCrypter.deriveKey(password);
		ECKey encryptedKey = unencryptedKey.encrypt(keyCrypter, aesKey);
		EncryptedPrivateKey encPriKey = encryptedKey.getEncryptedPrivateKey();
		assertNotNull(encPriKey);
		String target = BaseEncoding.base16().encode(
				encryptedKey.getEncryptedPrivateKey().getEncryptedBytes());
		// System.out.println(target);
		// assertNotNull(target);
		boolean foundPass = false;
		for (int i = 0; i < 99; i++) {
			String passtry = "" + i;
			KeyParameter aKey = keyCrypter.deriveKey(passtry);
			try {
				byte[] x = keyCrypter.decrypt(encPriKey, aKey);
				// found
				System.out.printf("[%d] Found Pass=%s\n", i, passtry);
				foundPass = true;
				break;
			} catch (KeyCrypterException kce) {

			}
		}

		assertTrue(foundPass);

		String message = "Goodbye Haha!";
		Sha256Hash hash = Sha256Hash.create(message.getBytes());
		ECKey.ECDSASignature sig = encryptedKey.sign(hash, aesKey);

		unencryptedKey = new ECKey(null, unencryptedKey.getPubKey());
		boolean found = false;
		for (int i = 0; i < 4; i++) {
			ECKey key2 = ECKey.recoverFromSignature(i, sig, hash, true);
			if (unencryptedKey.equals(key2)) {
				found = true;
				break;
			}
		}
		assertTrue(found);

	}

}
