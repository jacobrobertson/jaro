package com.robestone.jaro;

import java.util.HashMap;

import java.util.Map;

import com.robestone.jaro.levels.LevelPersister;

public class LevelPersisterMock implements LevelPersister {

	private String currentLevel = "004.test-level-1.html";
	private Map<String, Boolean> passed = new HashMap<String, Boolean>();
	
	@Override
	public String getCurrentLevel() {
		return currentLevel;
	}
	@Override
	public void setCurrentLevel(String levelKey) {
		currentLevel = levelKey;
	}
	@Override
	public boolean isLevelPassed(String levelKey) {
		Boolean b = passed.get(levelKey);
		if (b == null) {
			return false;
		}
		return b;
	}
	@Override
	public void setLevelPassed(String levelKey) {
		passed.put(levelKey, true);
	}
	@Override
	public String getGameType() {
		return "funk";
	}
}
