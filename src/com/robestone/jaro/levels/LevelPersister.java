package com.robestone.jaro.levels;

/**
 * Level manager interacts with persistance (preferences, etc) through this api.
 * 
 * TODO merge into JaroResources - there's no need for two APIs for persisting things
 * 
 * @author jacob
 */
public interface LevelPersister {

	void setCurrentLevel(String levelKey);
	String getCurrentLevel();

	boolean isLevelPassed(String levelKey);
	void setLevelPassed(String levelKey);
	
	String getGameType();

}
