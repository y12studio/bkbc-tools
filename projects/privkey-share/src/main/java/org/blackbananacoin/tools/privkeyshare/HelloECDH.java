package org.blackbananacoin.tools.privkeyshare;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.Security;

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

		BigInteger g768 = new BigInteger(
				"9c201473c1316c621df461b71ebb0cdcc90a6e5527e5e126633d131f87461c4dc4afc60c2cb0f053b6758871489a69613e2a8b4c8acde23954c08c81cbd36132cfd64d69e4ed9f8e51ed6e516297206672d5c0a69135df0a5dcf010d289a9ca1",
				16);
		BigInteger p768 = new BigInteger(
				"2c201423debed1b80103b8b309715be009d48860ed5ae9b9d5d8159508efd802e3ad4501a7f7e1cfec78844489148cd72da24b21eddd01aa624291c48393e277cfc529e37075eccef957f3616f962d15b44aeab4039d01b817fde9eaa12fd73f",
				16);

		DHKeyPairGenerator kpGen = getDHKeyPairGenerator(g768, p768);

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
