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

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GamePublicObjTest {

	@Test
	public void testGetGames() {
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping()
				.create();
		GameObj game1 = new GameObj();
		game1.setBtcAddr(Coffee3of5.TEST_ADDR1);
		GamePublicObj gpo = new GamePublicObj();
		gpo.getGames().put(game1.getBtcAddr(), game1);
		assertNotNull(gpo);
		System.out.println(gson.toJson(gpo));
	}

}
