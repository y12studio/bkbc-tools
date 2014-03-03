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

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Utils;
import com.google.bitcoin.params.MainNetParams;
import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.k10ud.cryptography.tss.core.ThresholdSecretSharing;

public class MainPrivKeyShare {

	private static final String OptPort = "p";
	private static Options options;
	private static HelpFormatter formatter = new HelpFormatter();
	private static String OptShares = "s";
	private static String OptThreshold = "t";
	private static String OptGenMode = "g";
	private static String OptRecoverMode = "r";

	public static String toJson(UserObj uo) {
		return new GsonBuilder().disableHtmlEscaping().setPrettyPrinting()
				.create().toJson(uo);
	}

	public static byte[] encrypt(byte[] unencryptedBytes, PublicKey key) {
		byte[] cipherText = null;
		try {
			final Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			cipherText = cipher.doFinal(unencryptedBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cipherText;
	}

	public static byte[] decrypt(byte[] encryptedBytes, PrivateKey key) {
		byte[] dectyptedBytes = null;
		try {
			final Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, key);
			dectyptedBytes = cipher.doFinal(encryptedBytes);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dectyptedBytes;
	}

	public static KeyPair toKeyPair(UserObj uo) {
		KeyPair r = null;
		byte[] priKeyEncode = BaseEncoding.base64Url().decode(
				uo.getKeyPrivate());
		byte[] pubKeyEncode = BaseEncoding.base64Url()
				.decode(uo.getKeyPublic());
		KeyFactory kf;
		try {
			kf = KeyFactory.getInstance("RSA");
			PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(
					pubKeyEncode));
			PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(
					priKeyEncode));
			r = new KeyPair(publicKey, privateKey);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return r;
	}

	public static UserObj generateKey(String id) {
		KeyPairGenerator keyGen;
		UserObj uo = new UserObj();
		uo.setId(id);
		try {
			keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(512);
			KeyPair key = keyGen.generateKeyPair();
			uo.setKeyPrivate(BaseEncoding.base64Url().encode(
					key.getPrivate().getEncoded()));
			uo.setKeyPublic(BaseEncoding.base64Url().encode(
					key.getPublic().getEncoded()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return uo;
	}

	public static void main(String[] args) {
		CommandLineParser parser = new GnuParser();
		options = getOptions();
		try {
			CommandLine line = parser.parse(options, args);
			if (line.hasOption("help")) {
				formatter.printHelp("java -jar app.jar", options);
				System.exit(-1);
			}

			if (line.hasOption(OptGenMode)) {
				int shares = Integer.parseInt(line.getOptionValue(OptShares,
						"5"));
				int threshold = Integer.parseInt(line.getOptionValue(
						OptThreshold, "3"));
				createSharing(shares, threshold);
			} else if (line.hasOption(OptRecoverMode)) {
				// get shares bytearray
				// recover private key
				// create ECKey
				//
			}

		} catch (ParseException e) {
			e.printStackTrace();
			formatter.printHelp("java -jar app.jar", options);
		}

	}

	private static void createSharing(int shares, int threshold) {
		ThresholdSecretSharing tss = new ThresholdSecretSharing();
		// Create 5 shares, secret recoverable from at least 3 different shares
		ECKey key = new ECKey();
		Address addr = new Address(MainNetParams.get(), key.getPubKeyHash());
		System.out.println("Bitcoin address : " + addr);

		byte[] secret = key.getPrivKeyBytes();
		byte[][] sharesArr = tss.createShares(secret, shares, threshold,
				new SecureRandom());
		showKeyDetail(key);
		for (int i = 0; i < sharesArr.length; i++) {
			System.out.printf("sharing_%d=%s\n", i + 1,
					Utils.bytesToHexString(sharesArr[i]));
		}
		// byte[][] sharesTarget = { sharesArr[0], sharesArr[2], sharesArr[3] };
		// recoverPrivateKey(sharesTarget);
	}

	private static void showKeyDetail(ECKey key) {
		System.out.println(key);
		System.out.println("PrivateKey:"
				+ Utils.bytesToHexString(key.getPrivKeyBytes()));
		System.out.println("DumpPrivateKey:"
				+ key.getPrivateKeyEncoded(MainNetParams.get()));
	}

	private static void recoverPrivateKey(byte[][] sharesTarget) {
		ThresholdSecretSharing tss = new ThresholdSecretSharing();
		// byte[][] sharesTarget = { sharesArr[0], sharesArr[2], sharesArr[3] };
		byte[] secretTarget = tss.recoverSecret(sharesTarget);
		// compress key
		ECKey key3 = new ECKey(new BigInteger(1, secretTarget), null, true);
		showKeyDetail(key3);
	}

	private static Options getOptions() {
		// create the command line parser
		// create the Options
		Options options = new Options();
		options.addOption(OptGenMode, "generate", false,
				"generate private key sharing");
		options.addOption("h", "help", false, "Print help.");
		options.addOption(OptionBuilder.withType(Integer.class)
				.withLongOpt("shares")
				.withDescription("number of shares to generate").hasArg()
				.withArgName("int").create(OptShares));
		options.addOption(OptionBuilder.withType(Integer.class)
				.withLongOpt("threshold")
				.withDescription("number of requires shares").hasArg()
				.withArgName("int").create(OptThreshold));
		return options;

	}

}
