package com.robestone.jaro;

import com.robestone.jaro.android.HtmlResources.LevelsIterable;
import com.robestone.jaro.android.HtmlResources.StagesIterable;
import com.robestone.jaro.levels.Level;
import com.robestone.jaro.levels.Stage;

import junit.framework.TestCase;

public class HtmlResourcesTest extends TestCase {

	public void testStagesIterable() {
		String[] fileNames = {"001.funk", "002.junk"};
		StagesIterable it = new StagesIterable(fileNames);
		StringBuilder buf = new StringBuilder();
		for (Stage stage: it) {
			buf.append(stage.getStageKey() + ":" + stage.getCaption() + ":");
		}
		assertEquals("001.funk:funk:002.junk:junk:", buf.toString());
	}
	public void testLevelsIterable() {
		String[] fileNames = {"001.funk.txt", "002.junk.zoo"};
		LevelsIterable it = new LevelsIterable("stage1", fileNames);
		StringBuilder buf = new StringBuilder();
		for (Level level: it) {
			buf.append(level.getStageKey() + ":" + level.getLevelKey() + ":" + level.getCaption() + ":");
		}
		assertEquals("stage1:001.funk.txt:funk:stage1:002.junk.zoo:junk:", buf.toString());
	}
	
}
