package org.blackbananacoin.tools.privkeyshare;

import java.util.Map;

import com.google.api.client.util.Maps;

public class GamePublicObj {
	
	private Map<String,GameObj> games = Maps.newHashMap();

	public Map<String,GameObj> getGames() {
		return games;
	}

	public void setGames(Map<String,GameObj> games) {
		this.games = games;
	}

}
