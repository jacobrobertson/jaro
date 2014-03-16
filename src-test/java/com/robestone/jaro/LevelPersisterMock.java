package com.robestone.jaro;

import java.util.HashMap;

import java.util.Map;

import com.robestone.jaro.levels.Level;
import com.robestone.jaro.levels.LevelPersister;
import com.robestone.jaro.levels.Stage;

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
	@Override
	public boolean isLevelUnlocked(String levelKey) {
		return false;
	}
	@Override
	public void setLevelUnlocked(String levelKey) {
	}
	@Override
	public boolean isStagePassed(String stageKey) {
		return false;
	}
	@Override
	public void setStagePassed(String stageKey) {
	}
	@Override
	public boolean isStageWorkedOn(String stageKey) {
		return false;
	}
	@Override
	public void setStageWorkedOn(String stageKey) {
	}
	@Override
	public boolean isStageUnlocked(String stageKey) {
		return false;
	}
	@Override
	public void setStageUnlocked(String stageKey) {
	}
	@Override
	public void fillStage(Stage stage) {
	}
	@Override
	public void fillLevel(Level level) {
	}
	
}
