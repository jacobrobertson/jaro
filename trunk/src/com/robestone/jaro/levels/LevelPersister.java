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

	boolean isLevelUnlocked(String levelKey);
	void setLevelUnlocked(String levelKey);
	
	boolean isStagePassed(String stageKey);
	void setStagePassed(String stageKey);
	
	boolean isStageWorkedOn(String stageKey);
	void setStageWorkedOn(String stageKey);

	boolean isStageUnlocked(String stageKey);
	void setStageUnlocked(String stageKey);

	String getGameType();
	
	void fillStage(Stage stage);
	void fillLevel(Level level);

}
