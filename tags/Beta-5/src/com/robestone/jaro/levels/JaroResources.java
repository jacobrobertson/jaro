package com.robestone.jaro.levels;

import com.robestone.jaro.Grid;


/**
 * Provides the single point of entry for retrieving all stages and level information.
 * @author Jacob
 */
public interface JaroResources {

//	String getGameType();
	
	Iterable<Stage> getStages();
	
	int getStagesCount();
	Stage getStage(int index);
	
	Iterable<Level> getLevels(String stageKey);
	
	int getLevelsCount(String stageKey);
	Level getLevel(String stageKey, int index);
	
	Grid getGrid(Level level);
	
}
