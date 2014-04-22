package org.blackbananacoin.tools.privkeyshare;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.util.Enumeration;

import org.spongycastle.asn1.sec.SECNamedCurves;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.AsymmetricCipherKeyPair;
import org.spongycastle.crypto.agreement.DHAgreement;
import org.spongycastle.crypto.generators.DHKeyPairGenerator;
import org.spongycastle.crypto.params.DHKeyGenerationParameters;
import org.spongycastle.crypto.params.DHParameters;
import org.spongycastle.crypto.params.DHPrivateKeyParameters;
import org.spongycastle.crypto.params.DHPublicKeyParameters;
import org.spongycastle.jce.provider.BouncyCastleProvider;

public class HelloECDH {

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	private static DHKeyPairGenerator getDHKeyPairGenerator(BigInteger g,
			BigInteger p) {
		DHParameters dhParams = new DHParameters(p, g);
		DHKeyGenerationParameters params = new DHKeyGenerationParameters(
				new SecureRandom(), dhParams);
		DHKeyPairGenerator kpGen = new DHKeyPairGenerator();

		kpGen.init(params);

		return kpGen;
	}

	public static void main(String[] args) throws Exception {
		
		Enumeration<?> names = SECNamedCurves.getNames();
        while (names.hasMoreElements()) {
            System.out.println("\t" + (String) names.nextElement());
        }
        
        // All clients must agree on the curve to use by agreement. Bitcoin uses secp256k1.
        // X9ECParameters params = SECNamedCurves.getByName("secp256k1");
		

		BigInteger g512 = new BigInteger("20145d6172adb43045b68ae8e1de1070b6137005686d29d3d73a7749199681ee5b212c9b96bfdcfa5b20cd5e3fd2044895d609cf9b410b7a0f12ca1cb9a428cc", 16);
	    BigInteger p512 = new BigInteger("2015fec095f3b85ee286542b3836fc81a5dd0a0349b4c239dd38744d488cf8e31db8bcb7d33b41abb9e5a33cca9144b1cef332c94bf0573bf047a3aca98cdf3b", 16);
	    
		DHKeyPairGenerator kpGen = getDHKeyPairGenerator(g512, p512);

		//
		// generate first pair
		//
		AsymmetricCipherKeyPair pair = kpGen.generateKeyPair();

		DHPublicKeyParameters pu1 = (DHPublicKeyParameters) pair.getPublic();
		DHPrivateKeyParameters pv1 = (DHPrivateKeyParameters) pair.getPrivate();
		//
		// generate second pair
		//
		pair = kpGen.generateKeyPair();

		DHPublicKeyParameters pu2 = (DHPublicKeyParameters) pair.getPublic();
		DHPrivateKeyParameters pv2 = (DHPrivateKeyParameters) pair.getPrivate();

		//
		// two way ?
		//
		DHAgreement e1 = new DHAgreement();
		DHAgreement e2 = new DHAgreement();

		e1.init(pv1);
		e2.init(pv2);

		BigInteger m1 = e1.calculateMessage();
		BigInteger m2 = e2.calculateMessage();

		BigInteger k1 = e1.calculateAgreement(pu2, m2);
		BigInteger k2 = e2.calculateAgreement(pu1, m1);
		
		System.out.println(k1);
		System.out.println(k2);

		if (!k1.equals(k2)) {
			System.out.println("test failed.");
		}else{
			System.out.println("test ok.");			
		}
	}

}
