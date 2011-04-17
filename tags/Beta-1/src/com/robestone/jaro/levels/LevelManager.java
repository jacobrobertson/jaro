package com.robestone.jaro.levels;

import java.io.InputStream;
import java.util.List;

import com.robestone.jaro.Grid;

/**
 * Responsibilities (of this package)
 * - store all progress - unlocked stages and passed levels, plus current level working on
 * - map "raw" levels into stages
 * - parse level data into grids
 * - provide icons, images (etc), and captions for stages, whatever that ends up looking like
 * 
 * @author jacob
 */
public class LevelManager {

	private List<Stage> stages;
	
	private InputStreamBuilder inputStreamBuilder;
	private LevelParser levelParser;

	private LevelPersister levelPersister;
	
	public LevelManager(InputStreamBuilder inputStreamBuilder, LevelParser levelParser, LevelPersister levelPersister) {
		this.inputStreamBuilder = inputStreamBuilder;
		this.levelParser = levelParser;
		InputStream in = this.inputStreamBuilder.buildStagesInputStream();
		stages = this.levelParser.parseStages(in);
		this.levelPersister = levelPersister;
		readUnlockedState();
		setFirstStageUnlocked();
	}

	private void setFirstStageUnlocked() {
		stages.get(0).getLevels().get(0).setUnlocked(true);
	}
	private void readUnlockedState() {
		for (Stage stage: stages) {
			for (Level level: stage.getLevels()) {
				String levelKey = level.getLevelKey();
				boolean unlocked = levelPersister.isLevelUnlocked(levelKey);
				level.setUnlocked(unlocked);
			}
		}
	}
	
	/**
	 * Used, for example, by the menu maker to show the list of stages.
	 */
	public List<Stage> getStages() {
		return stages;
	}
    public Level findLevel(int levelIndex) {
    	int findIndex = 0;
    	for (Stage stage: stages) {
    		for (Level level: stage.getLevels()) {
    			if (findIndex == levelIndex) {
    				return level;
    			}
    			findIndex++;
    		}
    	}
    	return null;
    }

	/**
	 * Marks current level as passed, and moves to the next level.
	 */
	public Level passCurrentLevel() {
		Level currentLevel = getCurrentLevel();
		Level nextLevel = null;
		boolean found = false;
		for (Stage stage: getStages()) {
			for (Level level: stage.getLevels()) {
				if (found) {
					nextLevel = level;
					break;
				} else if (level == currentLevel) {
					found = true;
				}
			}
			if (nextLevel != null) {
				break;
			}
		}
		// TODO this "first level" logic isn't really what I want.
		if (nextLevel == null) {
			nextLevel = getFirstLevel();
		}
		nextLevel.setUnlocked(true);
		levelPersister.setLevelUnlocked(nextLevel.getLevelKey());
		levelPersister.setCurrentLevel(nextLevel.getLevelKey());
		return nextLevel;
	}
	public void setCurrentLevel(Level level) {
		levelPersister.setCurrentLevel(level.getLevelKey());
	}
	private Level getFirstLevel() {
		Stage s = stages.get(0);
		Level l = s.getLevels().get(0);
		return l;
	}
	public Level getCurrentLevel() {
		String levelKey = levelPersister.getCurrentLevel();
		if (levelKey == null) {
			Level l = getFirstLevel();
			levelKey = l.getLevelKey();
			levelPersister.setCurrentLevel(levelKey);
			return l;
		} else {
			return getLevel(levelKey);
		}
	}
	public Level getLevel(String levelKey) {
		for (Stage stage: getStages()) {
			for (Level level: stage.getLevels()) {
				if (level.getLevelKey().equals(levelKey)) {
					return level;
				}
			}
		}
		return null;
	}

	/**
	 * Call when a level is passed.
	 */
	public Grid getGrid(Level level) {
		InputStream in = inputStreamBuilder.buildLevelInputStream(level.getLevelKey());
		return levelParser.parseGrid(in);
	}
	public LevelParser getLevelParser() {
		return levelParser;
	}
	
}
