package com.robestone.jaro.levels;

/**
 * TODO put the "unlocked" attribute back in this class - maybe
 * @author Jacob
 */
public class Level {
	
	/**
	 * Key should be hidden from user.  Like "1.0" or "A" or "turtles-z"
	 */
	private String levelKey;
	private String levelCaption;
	private String stageKey;
//	private boolean unlocked;
	private String levelDataFormatType;
	
	// these are only used in sokoban - but might be used later in the compressed format
	private int cols;
	private int rows;

	public Level(String levelKey, String levelCaption, String stageKey, String levelDataFormatType) {
		this.levelKey = levelKey;
		this.levelCaption = levelCaption;
		this.stageKey = stageKey;
		this.levelDataFormatType = levelDataFormatType;
	}

	/*
	public boolean isUnlocked() {
		return unlocked;
	}

	public void setUnlocked(boolean unlocked) {
		this.unlocked = unlocked;
	}
	*/

	/**
	 * Must be unique?
	 */
	public String getLevelKey() {
		return levelKey;
	}

	public String getCaption() {
		return levelCaption;
	}

	public String getStageKey() {
		return stageKey;
	}
	
	public String getLevelDataFormatType() {
		return levelDataFormatType;
	}
	
}