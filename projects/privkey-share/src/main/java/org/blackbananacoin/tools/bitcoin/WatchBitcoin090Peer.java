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

import com.google.bitcoin.core.AbstractPeerEventListener;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.Peer;
import com.google.bitcoin.core.PeerGroup;
import com.google.bitcoin.core.ScriptException;
import com.google.bitcoin.core.Transaction;
import com.google.bitcoin.core.TransactionInput;
import com.google.bitcoin.core.TransactionOutput;
import com.google.bitcoin.params.MainNetParams;
import com.google.bitcoin.utils.BriefLogFormatter;
import com.google.common.net.InetAddresses;

public class WatchBitcoin090Peer {
	private static Logger log = LoggerFactory.getLogger(WatchBitcoin090Peer.class);

	public static void main(String[] args) {
		BriefLogFormatter.init();
		NetworkParameters params = MainNetParams.get();
		PeerGroup peerGroup = new PeerGroup(params);
		
		//peerGroup.addPeerDiscovery(new DnsDiscovery(params));
		peerGroup.addAddress(InetAddresses.forString("107.170.251.205"));
		
		peerGroup.addEventListener(new AbstractPeerEventListener() {
			private boolean _debug = true;
			
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
		});
		peerGroup.start();
	}
}
