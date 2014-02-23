package com.robestone.jaro.levels;

/**
 * Level manager interacts with persistance (preferences, etc) through this api.
 * @author jacob
 */
public interface LevelPersister {

	boolean isLevelUnlocked(String levelKey);
	void setLevelUnlocked(String levelKey);
	void setCurrentLevel(String levelKey);
	String getCurrentLevel();

	boolean isLevelPassed(String levelKey);
	void setLevelPassed(String levelKey);

}
