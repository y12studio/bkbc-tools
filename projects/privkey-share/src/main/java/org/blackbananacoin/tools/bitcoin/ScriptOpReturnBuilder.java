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

import static com.google.bitcoin.script.ScriptOpCodes.*;

import java.net.UnknownHostException;

import org.blackbananacoin.tools.bitcoin.BluePuf.ProtoBlue;
import org.blackbananacoin.tools.bitcoin.BluePuf.ProtoBlue.ExType;
import org.blackbananacoin.tools.bitcoin.BluePuf.ProtoBlue.Type;
import org.blackbananacoin.tools.bitcoin.BluePuf.ProtoBlue.VerType;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Transaction;
import com.google.bitcoin.core.TransactionInput;
import com.google.bitcoin.params.MainNetParams;
import com.google.bitcoin.script.Script;
import com.google.bitcoin.script.ScriptBuilder;
import com.google.common.io.BaseEncoding;

/**
 * @author user https://bitcointalk.org/index.php?topic=453086.0
 * 
 */
public class ScriptOpReturnBuilder {

	private static void watchEligius() throws UnknownHostException {
		BitcoinTestUtils.watchDnsPeer("stratum.mining.eligius.st");
	}

	public static void main(String[] args) throws UnknownHostException {

		String rtx = buildRawTx();
		// System.out.println(rtx);

		// watchEligius();
	}

	private static String buildRawTx() {
		ProtoBlue puf = ProtoBlue.newBuilder().setBkbcValue(200855)
				.setProtoType(Type.TEST).setExchangeType(ExType.BTC_TWD)
				.setVersion(VerType.TEST1).build();
		System.out.println(puf);

		ScriptBuilder builder = new ScriptBuilder();
		builder.op(OP_RETURN).data(puf.toByteArray());

		Script outputScript = builder.build();
		Transaction tx1 = new Transaction(MainNetParams.get());
		tx1.addInput(new TransactionInput(MainNetParams.get(), tx1,
				new byte[] {}));
		ECKey key = new ECKey();
		tx1.addOutput(Bitcoins.toSatoshiEndBully(), key);
		Transaction tx2 = new Transaction(MainNetParams.get());

		tx2.addInput(tx1.getOutput(0));
		tx2.addOutput(Bitcoins.toSatoshiEndBully(), key);
		tx2.addOutput(Bitcoins.toSatoshiEndBully(), outputScript);
		System.out.println(tx2);
		String rawTx = BaseEncoding.base16().encode(
				tx2.unsafeBitcoinSerialize());
		return rawTx;
	}

}
