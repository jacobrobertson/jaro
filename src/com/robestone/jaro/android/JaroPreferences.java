package com.robestone.jaro.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.robestone.jaro.levels.LevelPersister;

public class JaroPreferences implements LevelPersister {

	private static final String CURRENT_LEVEL = "jaro.currentLevel";
	private static final String EULA = "jaro.eula";
	private static final String LEVEL_UNLOCKED = "jaro.levelUnlocked.";
	
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
	
	@Override
	public boolean isLevelUnlocked(String levelKey) {
		return sharedPreferences.getBoolean(LEVEL_UNLOCKED + levelKey, false);
	}
	
	@Override
	public void setCurrentLevel(String levelKey) {
		Editor editor = sharedPreferences.edit();
		editor.putString(CURRENT_LEVEL, levelKey);
		editor.commit();
	}
	@Override
	public String getCurrentLevel() {
		return sharedPreferences.getString(CURRENT_LEVEL, null);
	}

	@Override
	public void setLevelUnlocked(String levelKey) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(LEVEL_UNLOCKED + levelKey, true);
		editor.commit();
	}

}
