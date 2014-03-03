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
package org.blackbananacoin.tools.privkeyshare;

import static org.junit.Assert.*;

import java.security.KeyPair;

import org.junit.Test;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainPrivKeyShareTest {

	@Test
	public void testGenerateKey() {
		UserObj uo = MainPrivKeyShare.generateKey("haha123");
		assertNotNull(uo);
		String jstr = MainPrivKeyShare.toJson(uo);
		System.out.println(jstr);
		Gson gson = new Gson();
		UserObj uoTarget = gson.fromJson(jstr, UserObj.class);
		assertNotNull(uoTarget);
		KeyPair kp = MainPrivKeyShare.toKeyPair(uoTarget);
		assertNotNull(kp);
		String kpPriTarget = BaseEncoding.base64Url().encode(
				kp.getPrivate().getEncoded());
		String kpPubTarget = BaseEncoding.base64Url().encode(
				kp.getPublic().getEncoded());
		assertEquals(kpPriTarget, uo.getKeyPrivate());
		assertEquals(kpPubTarget, uo.getKeyPublic());
	}
	
	@Test
	public void testEncrypt() {
		UserObj uo = MainPrivKeyShare.generateKey("haha123");
		assertNotNull(uo);
		KeyPair kp = MainPrivKeyShare.toKeyPair(uo);
		assertNotNull(kp);
		byte[] encTarget = MainPrivKeyShare.encrypt("good".getBytes(), kp.getPublic());
		assertEquals("good", new String(MainPrivKeyShare.decrypt(encTarget, kp.getPrivate())));
	}

}
