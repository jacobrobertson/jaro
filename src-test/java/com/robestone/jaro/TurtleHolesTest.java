package com.robestone.jaro;

import com.robestone.jaro.piecerules.TurtleHoleRules;
import com.robestone.jaro.piecerules.TurtleRules;

public class TurtleHolesTest extends AbstractGameTest {
	
	public void testParse() {
		Piece p = new TurtleRules().parsePiece("turtle_down1.html");
		assertNotNull(p);
		assertEquals(TurtleRules.TURTLE_TYPE_ID, p.getType());
	}
	
	public void testHoleBlocksJaro() {
		initGrid(2, 
				new Piece(TurtleHoleRules.TURTLE_HOLE_TYPE_ID, TurtleHoleRules.TURTLE_HOLE_EMPTY_STATE));
		assertEquals(jaro, getGrid().getPiece(0, 0));
		controller.move(Direction.right);
		
		// jaro didn't move - got blocked
		assertPiece(jaro, 0, 0);
	}
	public void testJaroPushesTurtle() {
		initGrid(3, turtle, null);
		assertPiece(jaro, 0, 0);
		assertPiece(turtle, 1, 0);
		controller.move(Direction.right);
		
		// both jaro and turtle will move
		assertPiece(jaro, 1, 0);
		assertPiece(turtle, 2, 0);
	}
	public void testJaroPushesTurtleIntoHole() {
		
		Piece hole = new Piece(TurtleHoleRules.TURTLE_HOLE_TYPE_ID, TurtleHoleRules.TURTLE_HOLE_EMPTY_STATE);
		
		initGrid(3, turtle, hole);
		assertPiece(jaro, 0, 0);
		assertPiece(turtle, 1, 0);
		assertPiece(hole, 2, 0);
		controller.move(Direction.right);
		
		// both jaro and turtle will move
		assertPiece(jaro, 1, 0);
		assertStateAndType(TurtleHoleRules.TURTLE_HOLE_TYPE_ID, TurtleHoleRules.TURTLE_HOLE_WITH_TURTLE_STATE, 2, 0);
	}
}
