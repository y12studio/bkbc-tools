package org.blackbananacoin.tools.privkeyshare;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import org.bitcoin.protocols.payments.Protos.PaymentRequest;

import com.google.bitcoin.core.Utils;
import com.google.bitcoin.protocols.payments.PaymentRequestException;
import com.google.bitcoin.protocols.payments.PaymentSession;
import com.google.bitcoin.uri.BitcoinURI;
import com.google.bitcoin.uri.BitcoinURIParseException;
import com.google.common.util.concurrent.ListenableFuture;

public class BitcoinPaymentTool {

	// test url from https://donate.libreoffice.org/

	public static void main(String[] args) throws PaymentRequestException,
			BitcoinURIParseException, InterruptedException, ExecutionException {

		String url = "bitcoin:13dbaDHdYUwDHP24Qg3fEa3yqCBWE5hd9y?amount=0.01&r=https%3A%2F%2Fbitpay.com%2Fi%2FT5P3671YhMuYsqbCt5LsPY";
		ListenableFuture<PaymentSession> future = null;
		if (url.startsWith("http")) {
			future = PaymentSession.createFromUrl(url);
		} else if (url.startsWith("bitcoin:")) {
			future = PaymentSession.createFromBitcoinUri(new BitcoinURI(url));
		}

		PaymentSession session = future.get(); // may throw
												// PaymentRequestException.
		
		System.out.println(session);
		
		String memo = session.getMemo();
		BigInteger amountWanted = session.getValue();
		PaymentRequest pr = session.getPaymentRequest();
		System.out.println(pr);
		System.out.println(session.getPaymentDetails());

		if (session.isExpired()) {
			System.out.println("session expired.");
		}

		PaymentSession.PkiVerificationData identity = null;
		try {
			identity = session.verifyPki();
			System.out.println(identity.name);
			System.out.println(identity.orgName);
			System.out.println(identity.rootAuthorityName);
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

}
