package org.blackbananacoin.tools.bitcoin;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Bitcoins {

	public static final BigInteger milliBTC = new BigInteger("100000", 10);
	public static final BigInteger microBTC = new BigInteger("100", 10);
	public static final BigDecimal COINDec = new BigDecimal(100000000.0);

	public static final BigInteger toSatoshi(double btc) {
		return BigDecimal.valueOf(btc).multiply(COINDec).toBigInteger();
	}

	public static final BigInteger toSatoshiEndBully() {
		return BigDecimal.valueOf(0.00200855).multiply(COINDec).toBigInteger();
	}

}
