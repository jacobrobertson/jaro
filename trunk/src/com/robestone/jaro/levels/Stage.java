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

}