package org.blackbananacoin.tools.privkeyshare;

import java.io.File;
import java.io.IOException;
import java.security.Security;

import org.spongycastle.cms.CMSEnvelopedDataParser;
import org.spongycastle.cms.CMSException;
import org.spongycastle.jce.provider.BouncyCastleProvider;

import com.google.common.io.Files;

public class HelloHiSecureFile {

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	public static void main(String[] args) throws IOException, CMSException {

		byte[] barr = Files.toByteArray(new File(
				"testdata/notupload/Hello_world.txt.bs"));

		CMSEnvelopedDataParser envelopedDataParser = new CMSEnvelopedDataParser(
				barr);
		System.out.println(envelopedDataParser);

	}

}
