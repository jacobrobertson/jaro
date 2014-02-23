package com.robestone.jaro.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.robestone.jaro.levels.LevelPersister;

public class JaroPreferences implements LevelPersister {

	private static final String CURRENT_LEVEL = "jaro.currentLevel.";
	private static final String EULA = "jaro.eula";
	private static final String LEVEL_UNLOCKED = "jaro.levelUnlocked.";
	private static final String LEVEL_PASSED = "jaro.levelPassed.";
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
	public boolean isLevelUnlocked(String levelKey) {
		return sharedPreferences.getBoolean(LEVEL_UNLOCKED + levelKey, false);
	}
	
	@Override
	public boolean isLevelPassed(String levelKey) {
		return sharedPreferences.getBoolean(LEVEL_PASSED + levelKey, false);
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
	public void setLevelUnlocked(String levelKey) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(LEVEL_UNLOCKED + levelKey, true);
		editor.commit();
	}

	@Override
	public void setLevelPassed(String levelKey) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(LEVEL_PASSED + levelKey, true);
		editor.commit();
	}

}
