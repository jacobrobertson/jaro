package com.robestone.jaro;

import com.robestone.jaro.levels.LevelPersister;

public class LevelPersisterMock implements LevelPersister {

	private String currentLevel = "004.test-level-1.html";
	
	@Override
	public String getCurrentLevel() {
		return currentLevel;
	}
	@Override
	public boolean isLevelUnlocked(String levelKey) {
		return false;
	}
	@Override
	public void setCurrentLevel(String levelKey) {
		currentLevel = levelKey;
	}

	@Override
	public void setLevelUnlocked(String levelKey) {
		
	}

	@Override
	public boolean isLevelPassed(String levelKey) {
		return false;
	}
	
	@Override
	public void setLevelPassed(String levelKey) {
		
	}
}
