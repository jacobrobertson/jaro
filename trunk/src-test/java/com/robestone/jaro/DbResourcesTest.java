package com.robestone.jaro;

import java.io.IOException;

import junit.framework.TestCase;

import com.robestone.jaro.android.DbResources;
import com.robestone.jaro.levels.JaroAssets;
import com.robestone.jaro.levels.Level;

public class DbResourcesTest extends TestCase {

	public void testParseStages() throws IOException {
		JaroAssets assets = new JaroFileAssets("assets");
		DbResources resources = new DbResources(assets);
		
		assertEquals("Alberto", resources.getStage(1).getStageKey());
		assertEquals("Alberto Garcia Best 4 U", resources.getStage(2).getCaption());
		
		/*
Alberto_Garcia_Plus_2
sokoboxes_plus_01:7:11
sokoboxes_plus_02:11:12
sokoboxes_plus_03:12:14
sokoboxes_plus_04:14:12

		*/
		assertEquals("sokoboxes plus 02", resources.getLevel("Alberto_Garcia_Plus_2", 1).getCaption());
		assertEquals(7, resources.getLevel("Alberto_Garcia_Plus_2", 0).getCols());
		assertEquals(12, resources.getLevel("Alberto_Garcia_Plus_2", 3).getRows());
	}
	// 2_8w2_2w2f
	public void testParseGrid() throws Exception {
		JaroAssets assets = new JaroFileAssets("assets");
		DbResources resources = new DbResources(assets);
		Level level = resources.getLevel("Sasquatch__V", 5);
		String gs = resources.getGridString(level);
		System.out.println(gs);
//		Level level2 = resources.getLevel("Sasquatch__V", 6);
		Grid g = resources.getGrid(level);
		assertNotNull(g);
		assertEquals(11, level.getCols());
		assertEquals(10, level.getRows());
		System.out.println(g.toString());
		Piece p = g.getPiece(4, 6);
		assertEquals("jaro", p.getType());
	}
	
}
