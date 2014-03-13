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
	private static final boolean SHOW_ALL_LEVELS = true;
	
	private LevelPersister levelPersister;
	private JaroResources jaroResources;
	
	public LevelManager(LevelPersister levelPersister, JaroResources jaroResources) {
		this.levelPersister = levelPersister;
		this.jaroResources = jaroResources;
	}

	/**
	 * Marks current level as passed, and moves to the next level.
	 * This implementation is extremely inefficient for current storage
	 */
	public Level passCurrentLevel() {
		Level nextLevel = getNextLevel();
		String currentLevelKey = levelPersister.getCurrentLevel();
		levelPersister.setLevelPassed(currentLevelKey);
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
		return isLevelUnlocked(getLevel(levelKey));
	}
	public boolean isLevelUnlocked(Level level) {
		if (isLevelPassed(level)) {
			return true;
		}
		// a level is only unlocked if the stage is unlocked
		for (Stage stage: jaroResources.getStages()) {
			if (stage.getStageKey().equals(level.getStageKey())) {
				if (!isStageUnlocked(stage)) {
					return false;
				}
				break;
			}
		}
		// a level is unlocked if it is the first level of the stage,
		// or if the previous level is passed
		int count = jaroResources.getLevelsCount(level.getStageKey());
		Level previous = null;
		for (int i = 0; i < count; i++) {
			Level other = jaroResources.getLevel(level.getStageKey(), i);
			if (other.getLevelKey().equals(level.getLevelKey())) {
				if (i == 0) {
					return true;
				} else if (isLevelPassed(previous)) {
					return true;
				} else {
					return false;
				}
			}
			previous = other;
		}
		return false;
	}
	public boolean isLevelPassed(Level level) {
		return levelPersister.isLevelPassed(level.getLevelKey());
	}
	public boolean isStagePassed(Stage stage) {
		for (Level level: jaroResources.getLevels(stage.getStageKey())) {
			if (!isLevelPassed(level)) {
				return false;
			}
		}
		return true;
	}
	public boolean isStageWorkedOn(Stage stage) {
		for (Level level: jaroResources.getLevels(stage.getStageKey())) {
			if (isLevelPassed(level)) {
				return true;
			}
		}
		return false;
	}
	public boolean isStageUnlocked(Stage stage) {
		// if any of the levels are passed that is a no-brainer
		for (Level level: jaroResources.getLevels(stage.getStageKey())) {
			if (isLevelPassed(level)) {
				return true;
			}
		}
		// first stage is always unlocked
		if (stage.getStageKey().equals(jaroResources.getStage(0).getStageKey())) {
			return true;
		}
		Stage previous = null;
		int count = jaroResources.getStagesCount();
		for (int i = 0; i < count; i++) {
			Stage other = jaroResources.getStage(i);
			if (other.getStageKey().equals(stage.getStageKey())) {
				if (i == 0) {
					// first stage is always unlocked
					return true;
				} else if (isStageUnlocked(previous)) {
					// check the percentage
					float passed = getPercentagePassed(previous);
					return (passed >= PERCENT_TO_UNLOCK_NEXT);
				} else {
					// can't be unlocked if previous stage is still locked
					return false;
				}
			}
			previous = other;
		}
		throw new IllegalArgumentException("Wasn't able to determine lock state of stage " + stage.getStageKey());
	}
	public float getPercentagePassed(Stage stage) {
		float total = 0;
		float passed = 0;
		for (Level level: jaroResources.getLevels(stage.getStageKey())) {
			if (isLevelPassed(level)) {
				passed++;
			}
			total++;
		}
		return passed / total;
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
					isStageUnlocked(stage) && 
					!isStagePassed(stage)) {
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
