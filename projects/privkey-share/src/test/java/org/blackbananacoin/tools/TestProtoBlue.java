package org.blackbananacoin.tools;

import org.blackbananacoin.tools.bitcoin.BluePuf.ProtoBlue;
import org.blackbananacoin.tools.bitcoin.BluePuf.ProtoBlue.ExType;
import org.blackbananacoin.tools.bitcoin.BluePuf.ProtoBlue.Type;
import org.blackbananacoin.tools.bitcoin.BluePuf.ProtoBlue.VerType;

import com.google.bitcoin.core.Utils;


public class TestProtoBlue {

	public static void main(String[] args) {
		
		ProtoBlue pb = ProtoBlue.newBuilder().setVersion(VerType.TEST1).setProtoType(Type.BURN).setBkbcValue(9999999998L).build();
		detailPrint(pb);
		ProtoBlue pb2 = ProtoBlue.newBuilder().setVersion(VerType.MAIN1).setProtoType(Type.ORDER).setExchangeType(ExType.BTC_TWD).setBkbcValue(9999999998L).build();
		detailPrint(pb2);

	}

	private static void detailPrint(ProtoBlue pb) {
		System.out.println(pb);
		System.out.println(pb.getSerializedSize());
		System.out.println(Utils.bytesToHexString(pb.toByteArray()));
	}

}
