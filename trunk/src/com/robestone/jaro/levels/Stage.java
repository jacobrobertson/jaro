package com.robestone.jaro.levels;


public class Stage {
	
	/**
	 * Useful for loading the stage data from some file.
	 * This needs to be unique within a game type.
	 */
	private String stageKey;
	
	/**
	 * Name should be shown to users.  Like "Turtle Attack"
	 */
	private String caption;
	
	/**
	 *  only needed by db
	 *  Indicates the position of the stage in the database, based on the number of levels.
	 */
	private int stageIndex;

	public Stage(String stageKey, String caption) {
		this.stageKey = stageKey;
		this.caption = caption;
	}

	public String getStageKey() {
		return stageKey;
	}

	public String getCaption() {
		return caption;
	}

	public int getStageIndex() {
		return stageIndex;
	}

	public void setStageIndex(int stageIndex) {
		this.stageIndex = stageIndex;
	}
	
}