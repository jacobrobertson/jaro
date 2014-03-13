package com.robestone.jaro;

import com.robestone.jaro.piecerules.CaveRules;
import com.robestone.jaro.piecerules.SpiderRules;
import com.robestone.jaro.piecerules.TurtleRules;

import junit.framework.TestCase;

public class SpriteMapperTest extends TestCase {

	public void testCaveRules() {
		CaveRules r = new CaveRules();
		Piece p = new Piece(CaveRules.CAVE_TYPE_ID, "cave_bottom", null);
		String s = r.getSpriteId(p, false);
		assertEquals("cave_bottom", s);
		String sl = r.getSpriteId(p, true);
		assertEquals("cave_right", sl);
	}
	public void testTurtleRules() {
		TurtleRules r = new TurtleRules();
		Piece p = new Piece(TurtleRules.TURTLE_TYPE_ID, null, "turtle_right");
		String s = r.getSpriteId(p, false);
		assertEquals("turtle_right", s);
		String sl = r.getSpriteId(p, true);
		assertEquals("turtle_up", sl);
	}
	public void testSpiderRules() {
		SpiderRules r = new SpiderRules();
		Piece p = r.parsePiece("spider_big.png");
		String s = r.getSpriteId(p, false);
		assertEquals(SpiderRules.SPIDER_BIG_STATE, s);
		p.setState(SpiderRules.SPIDER_SMALL_STATE);
		s = r.getSpriteId(p, false);
		assertEquals(SpiderRules.SPIDER_SMALL_STATE, s);
		
	}
	
}
