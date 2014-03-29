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

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.google.bitcoin.core.AbstractPeerEventListener;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.PeerGroup;
import com.google.bitcoin.params.MainNetParams;
import com.google.bitcoin.utils.BriefLogFormatter;
import com.google.bitcoin.utils.Threading;
import com.google.common.net.InetAddresses;

public class BitcoinTestUtils {

	public static void watchDnsPeer(String host) throws UnknownHostException {
		watchPeer(InetAddress.getByName(host));
	}

	public static void watchIpPeer(String ip) {
		watchPeer(InetAddresses.forString(ip));
	}

	public static void watchPeer(InetAddress addr) {
		// String peerIp = "stratum.mining.eligius.st";
		// String peerIp = "107.170.251.205";
		BriefLogFormatter.init();
		NetworkParameters params = MainNetParams.get();
		PeerGroup peerGroup = new PeerGroup(params);
		peerGroup.addAddress(addr);
		AbstractPeerEventListener evlistener = new DebugPeerEventListener();
		peerGroup.addEventListener(evlistener, Threading.SAME_THREAD);
		peerGroup.start();
	}

}
