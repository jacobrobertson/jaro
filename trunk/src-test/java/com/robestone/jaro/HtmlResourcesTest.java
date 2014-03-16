package com.robestone.jaro;

import com.robestone.jaro.android.HtmlResources;
import com.robestone.jaro.levels.JaroAssets;
import com.robestone.jaro.levels.Level;
import com.robestone.jaro.levels.Stage;

import junit.framework.TestCase;

public class HtmlResourcesTest extends TestCase {

	public void testHtmlResources() {
		JaroAssets assets = new JaroFileAssets("src-test/resources");
		HtmlResources resources = new HtmlResources(assets, new LevelPersisterMock());
		Stage s2 = resources.getStage(1);
		assertEquals("Scary Spiders", s2.getCaption());
		
		Level l1 = resources.getLevel("003.Turtles", 1);
		assertEquals("html", l1.getLevelDataFormatType());
		assertEquals("002.snakoban2.html", l1.getLevelKey());
	}
	
}
