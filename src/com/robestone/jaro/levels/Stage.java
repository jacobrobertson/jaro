package com.robestone.jaro.levels;

import java.util.List;

public class Stage {
	
	/**
	 * Useful for loading the stage data from some file.
	 */
	private String stageKey;
	
	/**
	 * Name should be shown to users.  Like "Turtle Attack"
	 */
	private String caption;
	
	private List<Level> levels;

	public Stage(String stageKey, String caption, List<Level> levels) {
		this.stageKey = stageKey;
		this.caption = caption;
		this.levels = levels;
	}

	public String getStageKey() {
		return stageKey;
	}

	public String getCaption() {
		return caption;
	}

	public List<Level> getLevels() {
		return levels;
	}

}