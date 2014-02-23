package com.robestone.jaro;

import junit.framework.TestCase;

import com.robestone.jaro.piecerules.StubbornTurtleRules;
import com.robestone.jaro.piecerules.TurtleRules;

public class PieceRulesAdapterTest extends TestCase {

	public void testParsePiece() {
		assertPiece(new TurtleRules(), "turtle_left", "turtle", null, "turtle_left");
		assertPiece(new StubbornTurtleRules(), "turtlestubborn_left", "turtle", "stubborn", "turtlestubborn_left");
	}
	private void assertPiece(PieceRules r, String k, String type, String sub, String state) {
		Piece p = r.parsePiece(k);
		assertEquals(p.getType(), type);
		assertEquals(p.getState(), state);
		assertEquals(p.getSubType(), sub);

		String id = r.getSpriteId(p, false);
		assertEquals(k, id);
	}
	
}
