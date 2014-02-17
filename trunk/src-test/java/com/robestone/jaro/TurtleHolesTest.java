package com.robestone.jaro;

import com.robestone.jaro.piecerules.JaroPieceRules;
import com.robestone.jaro.piecerules.TurtleHoleRules;
import com.robestone.jaro.piecerules.TurtleRules;

public class TurtleHolesTest extends AbstractGameTest {
	
	public void testParse() {
		Piece p = new TurtleRules().parsePiece("turtle_down1.html");
		assertNotNull(p);
		assertEquals(TurtleRules.TURTLE_TYPE_ID, p.getType());
		
		game.getModel().saveJaroPosition();
	}
	
	public void testHoleBlocksJaro() {
		initGrid(2, 
				new Piece(TurtleHoleRules.TURTLE_HOLE_TYPE_ID, TurtleHoleRules.TURTLE_HOLE_WITH_SNAKE));
		assertEquals(jaro, getGrid().getPiece(0, 0));
		controller.move(Direction.right);
		
		// jaro didn't move - got blocked
		assertPiece(jaro, 0, 0);
		
		game.getModel().saveJaroPosition();
	}
	public void testJaroPushesTurtle() {
		initGrid(3, turtle, null);
		assertPiece(jaro, 0, 0);
		assertPiece(turtle, 1, 0);
		controller.move(Direction.right);
		
		// both jaro and turtle will move
		assertPiece(jaro, 1, 0);
		assertPiece(turtle, 2, 0);

		game.getModel().saveJaroPosition();
	}
	public void testJaroPushesTurtleIntoHole() {
		
		Piece hole = new Piece(TurtleHoleRules.TURTLE_HOLE_TYPE_ID, TurtleHoleRules.TURTLE_HOLE_WITH_SNAKE);
		
		initGrid(3, turtle, hole);
		assertPiece(jaro, 0, 0);
		assertPiece(turtle, 1, 0);
		assertPiece(hole, 2, 0);
		controller.move(Direction.right);
		
		// both jaro and turtle will move
		assertPiece(jaro, 1, 0);
		assertStateAndType(TurtleHoleRules.TURTLE_HOLE_TYPE_ID, TurtleHoleRules.TURTLE_HOLE_WITH_TURTLE_STATE, 2, 0);
		
		game.getModel().saveJaroPosition();
	}
	public void testParseIds() {
		doTestParseIds("turtlenest");
		doTestParseIds("turtlehole_snake");

		game.getModel().saveJaroPosition();
	}
	private void doTestParseIds(String parseKey) {
		Piece piece = JaroPieceRules.parsePiece(parseKey);
		assertNotNull(piece);
	}
}
