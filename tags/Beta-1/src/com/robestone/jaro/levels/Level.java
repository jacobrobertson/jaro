package com.robestone.jaro.levels;


public class Level {
	
	/**
	 * Key should be hidden from user.  Like "1.0" or "A" or "turtles-z"
	 */
	private String levelKey;
	private String stageKey;
	private boolean unlocked;

	public Level(String levelKey, String stageKey) {
		this.levelKey = levelKey;
		this.stageKey = stageKey;
	}

	public boolean isUnlocked() {
		return unlocked;
	}

	public void setUnlocked(boolean unlocked) {
		this.unlocked = unlocked;
	}

	public String getLevelKey() {
		return levelKey;
	}

	public String getStageKey() {
		return stageKey;
	}
	
}