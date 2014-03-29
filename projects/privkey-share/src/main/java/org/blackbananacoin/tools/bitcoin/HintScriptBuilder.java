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

import static com.google.bitcoin.script.ScriptOpCodes.OP_1;
import static com.google.bitcoin.script.ScriptOpCodes.OP_CHECKSIG;
import static com.google.bitcoin.script.ScriptOpCodes.OP_DEPTH;
import static com.google.bitcoin.script.ScriptOpCodes.OP_DROP;
import static com.google.bitcoin.script.ScriptOpCodes.OP_DUP;
import static com.google.bitcoin.script.ScriptOpCodes.OP_ELSE;
import static com.google.bitcoin.script.ScriptOpCodes.OP_ENDIF;
import static com.google.bitcoin.script.ScriptOpCodes.OP_EQUAL;
import static com.google.bitcoin.script.ScriptOpCodes.OP_EQUALVERIFY;
import static com.google.bitcoin.script.ScriptOpCodes.OP_HASH160;
import static com.google.bitcoin.script.ScriptOpCodes.OP_IF;
import static com.google.bitcoin.script.ScriptOpCodes.OP_NUMEQUAL;
import static com.google.bitcoin.script.ScriptOpCodes.OP_RIPEMD160;

import java.net.UnknownHostException;

import org.spongycastle.crypto.digests.RIPEMD160Digest;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Transaction;
import com.google.bitcoin.core.TransactionInput;
import com.google.bitcoin.params.MainNetParams;
import com.google.bitcoin.script.Script;
import com.google.bitcoin.script.ScriptBuilder;
import com.google.common.io.BaseEncoding;

/**
 * @author user https://bitcointalk.org/index.php?topic=538423.0;topicseen
 * 
 */
public class HintScriptBuilder {

	private static void watchEligius() throws UnknownHostException {
		BitcoinTestUtils.watchDnsPeer("stratum.mining.eligius.st");
	}

	public static byte[] doublehash160(byte[] input) {
		RIPEMD160Digest digest = new RIPEMD160Digest();
		digest.update(input, 0, input.length);
		byte[] rarr = new byte[20];
		digest.doFinal(rarr, 0);
		digest.reset();
		digest.update(rarr, 0, rarr.length);
		digest.doFinal(rarr, 0);
		return rarr;
	}

	public static void main(String[] args) throws UnknownHostException {

		//String rtx = buildRawTx();
		//System.out.println(rtx);
		
		watchEligius();
	}

	private static String buildRawTx() {
		ScriptBuilder builder = new ScriptBuilder();
		builder.op(OP_DEPTH).op(OP_1).op(OP_NUMEQUAL).op(OP_IF)
				.data("name of nakakamoto".getBytes()).op(OP_DROP)
				.op(OP_RIPEMD160).op(OP_RIPEMD160)
				.data(doublehash160("satoshi".getBytes())).op(OP_EQUAL)
				.op(OP_ELSE).op(OP_DUP).op(OP_HASH160)
				.data(doublehash160("Haha".getBytes())).op(OP_EQUALVERIFY)
				.op(OP_CHECKSIG).op(OP_ENDIF);

		Script outputScript = builder.build();
		Transaction tx1 = new Transaction(MainNetParams.get());
		tx1.addInput(new TransactionInput(MainNetParams.get(), tx1,
				new byte[] {}));
		ECKey key = new ECKey();
		tx1.addOutput(Bitcoins.toSatoshiEndBully(), key);
		Transaction tx2 = new Transaction(MainNetParams.get());

		tx2.addInput(tx1.getOutput(0));
		tx2.addOutput(Bitcoins.toSatoshiEndBully(), outputScript);
		tx2.addOutput(Bitcoins.toSatoshiEndBully(), key);
		System.out.println(tx2);
		String rawTx = BaseEncoding.base16().encode(
				tx2.unsafeBitcoinSerialize());
		return rawTx;
	}

}
