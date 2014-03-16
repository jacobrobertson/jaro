package com.robestone.jaro.levels;

import java.util.ArrayList;
import java.util.List;


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

	private static final float PERCENT_TO_UNLOCK_NEXT = .7f;
	private static final boolean SHOW_ALL_LEVELS = false;
	
	private LevelPersister levelPersister;
	private JaroResources jaroResources;
	
	public LevelManager(LevelPersister levelPersister, JaroResources jaroResources) {
		this.levelPersister = levelPersister;
		this.jaroResources = jaroResources;
	}

	/**
	 * Marks current level as passed, and moves to the next level.
	 */
	public Level passCurrentLevel() {
		Level nextLevel = getNextLevels()[0];
		String currentLevelKey = levelPersister.getCurrentLevel();
		levelPersister.setLevelPassed(currentLevelKey);
		levelPersister.setCurrentLevel(nextLevel.getLevelKey());
		
		// (A) update objects (B) update persistence 
		//		X set current level passed
		Level currentLevel = getLevel(currentLevelKey);
		currentLevel.setPassed(true);
		//		X set next level unlocked
		nextLevel.setUnlocked(true);
		levelPersister.setLevelUnlocked(nextLevel.getLevelKey());
		//		X set this stage as "worked on"
		String currentStageKey = currentLevel.getStageKey();
		Stage currentStage = null;
		Stage nextStage = null;
		for (Stage stage: jaroResources.getStages()) {
			if (nextStage == null && currentStage != null) {
				nextStage = stage;
			}
			if (stage.getStageKey().equals(currentStageKey)) {
				currentStage = stage;
			}
		}
		currentStage.setWorkedOn(true);
		levelPersister.setStageWorkedOn(currentStageKey);
		//		X if this is the last level, set stage as passed
		//		X calculate whether next stage will be unlocked, and do that
		//			this includes unlocking first level in that stage
		float levelsCount = 0;
		float passedLevelsCount = 0;
		for (Level level: jaroResources.getLevels(currentStageKey)) {
			levelsCount++;
			if (level.isPassed()) {
				passedLevelsCount++;
			}
		}
		float percentPassed = passedLevelsCount / levelsCount;
		if (percentPassed == 1f) {
			currentStage.setPassed(true);
			levelPersister.setStagePassed(currentStageKey);
		}
		if (percentPassed >= PERCENT_TO_UNLOCK_NEXT && nextStage != currentStage) {
			nextStage.setUnlocked(true);
			levelPersister.setStageUnlocked(nextStage.getStageKey());
			// unlock the first level of this stage too
			Level first = jaroResources.getLevel(nextStage.getStageKey(), 0);
			first.setUnlocked(true);
			levelPersister.setLevelUnlocked(first.getLevelKey());
		}
		
		return nextLevel;
	}
	public Level[] getNextLevels() {
		String currentLevel = levelPersister.getCurrentLevel();
		Level nextLevel = null;
		Level nextUnlockedUnpassedLevel = null;
		boolean found = false;
		for (Stage stage: jaroResources.getStages()) {
			String stageKey = stage.getStageKey();
			for (Level level: jaroResources.getLevels(stageKey)) {
				if (found) {
					if (nextLevel == null) {
						nextLevel = level;
						// if this one isn't passed, we don't need to keep looking
						if (!level.isPassed()) {
							break;
						}
					} else if (level.isUnlocked()) {
						if (!level.isPassed()) {
							nextUnlockedUnpassedLevel = level;
							break;
						}
					} else {
						// within this stage, if the level is locked, we don't need to keep looking
						break;
					}
				} else if (level.getLevelKey().equals(currentLevel)) {
					found = true;
				}
			}
			// don't need to look at further stages - we only care about the current stage
			if (nextLevel != null) {
				break;
			}
		}
		if (nextLevel == null) {
			// TODO - this should instead take you to a special screen "You've passed the game!!!"
			nextLevel = getFirstLevel();
		}
		if (nextUnlockedUnpassedLevel == null) {
			return new Level[] { nextLevel };
		} else {
			return new Level[] { nextLevel, nextUnlockedUnpassedLevel };
		}
	}
	public void setCurrentLevel(Level level) {
		levelPersister.setCurrentLevel(level.getLevelKey());
	}
	public boolean isShowAllLevels() {
		return SHOW_ALL_LEVELS;
	}
	private Level getFirstLevel() {
		Stage s = jaroResources.getStage(0);
		Level l = jaroResources.getLevel(s.getStageKey(), 0);
		return l;
	}
	public List<Stage> getOtherUnlockedStages(String stageKey) {
		List<Stage> unlocked = new ArrayList<Stage>();
		for (Stage stage: jaroResources.getStages()) {
			if (!stageKey.equals(stage.getStageKey()) &&
					stage.isUnlocked() && 
					!stage.isPassed()) {
				unlocked.add(stage);
			}
		}
		return unlocked;
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
		if (levelKey == null) {
			return null;
		}
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
