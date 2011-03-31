package com.robestone.jaro;

import com.robestone.jaro.piecerules.CaveRules;

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
	
}
