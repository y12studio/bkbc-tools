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
