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
package org.blackbananacoin.tools.bitcoin;

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
