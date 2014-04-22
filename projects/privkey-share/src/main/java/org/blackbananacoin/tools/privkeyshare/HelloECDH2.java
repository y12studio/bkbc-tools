package org.blackbananacoin.tools.privkeyshare;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;

import com.google.common.io.BaseEncoding;

/**
 * @author user modify from
 *         http://stackoverflow.com/questions/20260097/not-generating
 *         -proper-aes-key-size-with-ecdh-keyagreement-in-java
 *         <p>
 *         java.security.NoSuchProviderException: JCE cannot authenticate the
 *         provider SC
 */
public class HelloECDH2 {

	public static byte[] SEED = new SecureRandom().generateSeed(16);

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	/**
	 * java.security.NoSuchProviderException: no such provider: SC
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String plainText = "Look foo, I'm a message!";
		System.out.println("Original plaintext message: " + plainText);

		// Initialize two key pairs
		KeyPair keyPairA = generateECKeys();
		KeyPair keyPairB = generateECKeys();

		// Create two AES secret keys to encrypt/decrypt the message
		SecretKey secretKeyA = generateSharedSecret(keyPairA.getPrivate(),
				keyPairB.getPublic());
		System.out.println(BaseEncoding.base16()
				.encode(secretKeyA.getEncoded()));
		SecretKey secretKeyB = generateSharedSecret(keyPairB.getPrivate(),
				keyPairA.getPublic());
		System.out.println(BaseEncoding.base16()
				.encode(secretKeyB.getEncoded()));

		// Encrypt the message using 'secretKeyA'
		String cipherText = encryptString(secretKeyA, plainText);
		System.out.println("Encrypted cipher text: " + cipherText);

		// Decrypt the message using 'secretKeyB'
		String decryptedPlainText = decryptString(secretKeyB, cipherText);
		System.out.println("Decrypted cipher text: " + decryptedPlainText);

	}

	private static String decryptString(SecretKey key, String cipherText) {
		try {
			Key decryptionKey = new SecretKeySpec(key.getEncoded(),
					key.getAlgorithm());
			IvParameterSpec ivSpec = new IvParameterSpec(SEED);
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
			byte[] cipherTextBytes = BaseEncoding.base16().decode(cipherText);
			byte[] plainText;

			cipher.init(Cipher.DECRYPT_MODE, decryptionKey, ivSpec);
			plainText = new byte[cipher.getOutputSize(cipherTextBytes.length)];
			int decryptLength = cipher.update(cipherTextBytes, 0,
					cipherTextBytes.length, plainText, 0);
			decryptLength += cipher.doFinal(plainText, decryptLength);

			return new String(plainText, "UTF-8");
		} catch (NoSuchAlgorithmException | NoSuchProviderException
				| NoSuchPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException
				| IllegalBlockSizeException | BadPaddingException
				| ShortBufferException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String encryptString(SecretKey key, String plainText) {
		try {
			IvParameterSpec ivSpec = new IvParameterSpec(SEED);
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", BouncyCastleProvider.PROVIDER_NAME);
			byte[] plainTextBytes = plainText.getBytes("UTF-8");
			byte[] cipherText;

			cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
			cipherText = new byte[cipher.getOutputSize(plainTextBytes.length)];
			int encryptLength = cipher.update(plainTextBytes, 0,
					plainTextBytes.length, cipherText, 0);
			encryptLength += cipher.doFinal(cipherText, encryptLength);

			return BaseEncoding.base16().encode(cipherText);
		} catch (NoSuchAlgorithmException | NoSuchProviderException
				| NoSuchPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException
				| UnsupportedEncodingException | ShortBufferException
				| IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static SecretKey generateSharedSecret(PrivateKey privateKey,
			PublicKey publicKey) {
		try {
			KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH",
					BouncyCastleProvider.PROVIDER_NAME);
			keyAgreement.init(privateKey);
			keyAgreement.doPhase(publicKey, true);

			SecretKey key = keyAgreement.generateSecret("AES");
			System.out.println("Shared key length: " + key.getEncoded().length);
			return key;
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchProviderException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static KeyPair generateECKeys() {
		try {
			ECNamedCurveParameterSpec parameterSpec = ECNamedCurveTable
					.getParameterSpec("secp256k1");
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
					"ECDH", BouncyCastleProvider.PROVIDER_NAME);

			keyPairGenerator.initialize(parameterSpec);

			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			System.out.println("Private key length: "
					+ keyPair.getPrivate().getEncoded().length);
			System.out.println("Public key length: "
					+ keyPair.getPublic().getEncoded().length);
			return keyPair;
		} catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException
				| NoSuchProviderException e) {
			e.printStackTrace();
			return null;
		}
	}

}
