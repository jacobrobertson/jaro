package com.robestone.jaro.levels;


/**
 * Responsibilities (of this package)
 * - store all progress - unlocked stages and passed levels, plus current level working on
 * - map "raw" levels into stages
 * - parse level data into grids
 * - provide icons, images (etc), and captions for stages, whatever that ends up looking like
 * 
 * TODO need to completely revisit the way I'm holding level information on disk or in memory
 * 		right now, I am reading the level information from disk over and over, but the reason I did that 
 * 		is because jaroban has 10,000 levels.  It's probably okay to hold one stage's levels in memory at one time.
 * 
 * @author jacob
 */
public class LevelManager {

	private LevelPersister levelPersister;
	private JaroResources jaroResources;
	
	public LevelManager(LevelPersister levelPersister, JaroResources jaroResources) {
		this.levelPersister = levelPersister;
		this.jaroResources = jaroResources;
//		readUnlockedState();
		setFirstStageUnlocked();
	}

	private void setFirstStageUnlocked() {
		String stageKey = jaroResources.getStage(0).getStageKey();
		Level level1 = jaroResources.getLevel(stageKey, 0);
//		level1.setUnlocked(true);
		// this is new - but needed because we are not keeping levels in memory
		levelPersister.setLevelUnlocked(level1.getLevelKey());
	}
	/*
	private void readUnlockedState() {
		for (Stage stage: jaroResources.getStages()) {
			String stageKey = stage.getStageKey();
			for (Level level: jaroResources.getLevels(stageKey)) {
				String levelKey = level.getLevelKey();
				boolean unlocked = levelPersister.isLevelUnlocked(levelKey);
				level.setUnlocked(unlocked);
			}
		}
	}
	*/
	
	/**
	 * Marks current level as passed, and moves to the next level.
	 * This implementation is extremely inefficient for current storage
	 */
	public Level passCurrentLevel() {
		Level nextLevel = getNextLevel();
		levelPersister.setLevelUnlocked(nextLevel.getLevelKey());
		levelPersister.setCurrentLevel(nextLevel.getLevelKey());
		return nextLevel;
	}
	public Level getNextLevel() {
		String currentLevel = levelPersister.getCurrentLevel();
		String nextLevelKey = null;
		boolean found = false;
		for (Stage stage: jaroResources.getStages()) {
			String stageKey = stage.getStageKey();
			for (Level level: jaroResources.getLevels(stageKey)) {
				if (found) {
					nextLevelKey = level.getLevelKey();
					break;
				} else if (level.getLevelKey().equals(currentLevel)) {
					found = true;
				}
			}
			if (nextLevelKey != null) {
				break;
			}
		}
		// TODO this "first level" logic isn't really what I want.
		Level nextLevel = getLevel(nextLevelKey);
		if (nextLevel == null) {
			nextLevel = getFirstLevel();
		}
		return nextLevel;
	}
	public void setCurrentLevel(Level level) {
		levelPersister.setCurrentLevel(level.getLevelKey());
	}
	public boolean isLevelUnlocked(String levelKey) {
		return levelPersister.isLevelUnlocked(levelKey);
	}
	public boolean isLevelUnlocked(Level level) {
		return levelPersister.isLevelUnlocked(level.getLevelKey());
	}
	private Level getFirstLevel() {
		Stage s = jaroResources.getStage(0);
		Level l = jaroResources.getLevel(s.getStageKey(), 0);
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
		for (Stage stage: jaroResources.getStages()) {
			String stageKey = stage.getStageKey();
			for (Level level: jaroResources.getLevels(stageKey)) {
				if (level.getLevelKey().equals(levelKey)) {
					return level;
				}
			}
		}
		// means we fell through due to switching games or some other case
		return getFirstLevel();
	}

}
