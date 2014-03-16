package com.robestone.jaro.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.robestone.jaro.levels.Level;
import com.robestone.jaro.levels.LevelPersister;
import com.robestone.jaro.levels.Stage;

public class JaroPreferences implements LevelPersister {

	private static final String CURRENT_LEVEL = "jaro.currentLevel.";
	private static final String EULA = "jaro.eula";
	private static final String LEVEL_PASSED = "jaro.levelPassed.";
	private static final String LEVEL_UNLOCKED = "jaro.levelUnlocked.";
	private static final String STAGE_PASSED = "jaro.stagePassed.";
	private static final String STAGE_UNLOCKED = "jaro.stageUnlocked.";
	private static final String STAGE_WORKED_ONE = "jaro.stageWorkedOn.";
	private static final String GAME_TYPE = "jaro.gameType";
	
	private SharedPreferences sharedPreferences;
	
	public JaroPreferences(JaroActivity activity) {
		this.sharedPreferences = activity.getSharedPreferences("jaro", Context.MODE_PRIVATE);
	}
	
	public boolean isEulaRead() {
		return sharedPreferences.getBoolean(EULA, false);
	}
	public void setEulaRead() {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(EULA, true);
		editor.commit();
	}
	public void setGameType(String gameType) {
		Editor editor = sharedPreferences.edit();
		editor.putString(GAME_TYPE, gameType);
		editor.commit();
	}
	public String getGameType() {
		return sharedPreferences.getString(GAME_TYPE, HtmlResources.JARO_GAME_TYPE);
	}
	
	@Override
	public boolean isLevelPassed(String levelKey) {
		return getBoolean(LEVEL_PASSED + levelKey);
	}
	
	@Override
	public void setCurrentLevel(String levelKey) {
		Editor editor = sharedPreferences.edit();
		String gameType = getGameType();
		editor.putString(CURRENT_LEVEL + gameType, levelKey);
		editor.commit();
	}
	@Override
	public String getCurrentLevel() {
		String gameType = getGameType();
		return sharedPreferences.getString(CURRENT_LEVEL + gameType, null);
	}

	@Override
	public boolean isLevelUnlocked(String levelKey) {
		return getBoolean(LEVEL_UNLOCKED + levelKey);
	}

	@Override
	public void setLevelUnlocked(String levelKey) {
		setBooleanTrue(LEVEL_UNLOCKED + levelKey);
	}

	@Override
	public boolean isStagePassed(String stageKey) {
		return getBoolean(STAGE_PASSED + stageKey);
	}

	@Override
	public void setStagePassed(String stageKey) {
		setBooleanTrue(STAGE_PASSED + stageKey);
	}

	@Override
	public boolean isStageWorkedOn(String stageKey) {
		return getBoolean(STAGE_WORKED_ONE + stageKey);
	}

	@Override
	public void setStageWorkedOn(String stageKey) {
		setBooleanTrue(STAGE_WORKED_ONE + stageKey);
	}

	@Override
	public boolean isStageUnlocked(String stageKey) {
		return getBoolean(STAGE_UNLOCKED + stageKey);
	}

	@Override
	public void setStageUnlocked(String stageKey) {
		setBooleanTrue(STAGE_UNLOCKED + stageKey);
	}

	@Override
	public void setLevelPassed(String levelKey) {
		setBooleanTrue(LEVEL_PASSED + levelKey);
	}
	
	private void setBooleanTrue(String key) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, true);
		editor.commit();
	}
	private boolean getBoolean(String key) {
		return sharedPreferences.getBoolean(key, false);
	}
	public void fillStage(Stage stage) {
		String key = stage.getStageKey();
		stage.setPassed(isStagePassed(key));
		stage.setUnlocked(isStageUnlocked(key));
		stage.setWorkedOn(isStageWorkedOn(key));
	}
	public void fillLevel(Level level) {
		String key = level.getLevelKey();
		level.setPassed(isLevelPassed(key));
		level.setUnlocked(isLevelUnlocked(key));
	}

}
