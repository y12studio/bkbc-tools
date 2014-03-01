package org.blackbananacoin.tools.privkeyshare;

import java.math.BigInteger;
import java.security.SecureRandom;

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
import com.k10ud.cryptography.tss.core.ThresholdSecretSharing;

public class MainPrivKeyShare {

	private static final String OptPort = "p";
	private static Options options;
	private static HelpFormatter formatter = new HelpFormatter();
	private static String OptShares = "s";
	private static String OptThreshold = "t";
	private static String OptGenMode = "g";

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
		System.out.println(key);
		System.out.println("pri:"
				+ Utils.bytesToHexString(key.getPrivKeyBytes()));
		for (int i = 0; i < sharesArr.length; i++) {
			System.out.printf("sharing_%d=%s\n", i + 1,
					Utils.bytesToHexString(sharesArr[i]));
		}
	}

	private void recoverPrivateKey(byte[][] sharesTarget) {
		ThresholdSecretSharing tss = new ThresholdSecretSharing();
		// byte[][] sharesTarget = { sharesArr[0], sharesArr[2], sharesArr[3] };
		byte[] secretTarget = tss.recoverSecret(sharesTarget);
		// compress pub key
		ECKey key3 = new ECKey(new BigInteger(1, secretTarget), null, true);
		System.out.println(key3);
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
