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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.repackaged.com.google.common.base.Joiner;
import com.google.bitcoin.core.AbstractPeerEventListener;
import com.google.bitcoin.core.InventoryItem;
import com.google.bitcoin.core.InventoryMessage;
import com.google.bitcoin.core.Message;
import com.google.bitcoin.core.Peer;
import com.google.bitcoin.core.Pong;
import com.google.bitcoin.core.ScriptException;
import com.google.bitcoin.core.Transaction;
import com.google.bitcoin.core.TransactionInput;
import com.google.bitcoin.core.TransactionOutput;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class DebugPeerEventListener extends AbstractPeerEventListener {
	
	private static Logger log = LoggerFactory
			.getLogger(DebugPeerEventListener.class);
	
	private static final Function<InventoryItem, String> FunInvItemToString = new Function<InventoryItem,String>() {
	    public String apply(InventoryItem item) {
	        return item.type.name();
	    }
	};
	
	private boolean _debug = true;

	@Override
	public Message onPreMessageReceived(Peer peer, Message m) {
		if (m instanceof Pong) {
			// nothing
		} else if (m instanceof InventoryMessage) {
			InventoryMessage inv = (InventoryMessage) m;
			log.info("[MESSAGE INV] size={}\nitem={}",
					inv.getItems().size(),
					Joiner.on(",").join(Lists.transform(inv.getItems(), FunInvItemToString)));
		} else {
			log.info("[MESSAGE] = {}", m);
		}
		return super.onPreMessageReceived(peer, m);
	}

	@Override
	public void onTransaction(Peer peer, Transaction tx) {
		try {
			log.info("TxLockTime {}", tx.getLockTime());
			log.info("TxIn{}/Out{}", tx.getInputs().size(), tx
					.getOutputs().size());
			log.info("Saw Tx {}", tx);
			if (_debug) {
				for (TransactionInput tin : tx.getInputs()) {
					log.info("InputSequenceNumber {}",
							tin.getSequenceNumber());

					if (tin.getSequenceNumber() == TransactionInput.NO_SEQUENCE) {
						log.info("InputSequenceNumber==UINT_MAX lock time ignored");
					}

					log.info("InputScriptSig {}", tin.getScriptSig()
							.toString());
					log.info("InputOutpoint previous output hash {}",
							tin.getOutpoint().getHash());
					log.info("InputOutpoint previous output index {}",
							tin.getOutpoint().getIndex());
					TransactionOutput tout = tin.getOutpoint()
							.getConnectedOutput();
					// Map<Sha256Hash, Integer> appearInHashes =
					// preTx.getAppearsInHashes();
					log.info("OutpointTx output", tout);
				}
			}
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

}
