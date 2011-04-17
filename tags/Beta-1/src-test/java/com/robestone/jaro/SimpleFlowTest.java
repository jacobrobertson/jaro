package com.robestone.jaro;

import junit.framework.TestCase;

import com.robestone.jaro.piecerules.BoulderRules;
import com.robestone.jaro.piecerules.BugRules;
import com.robestone.jaro.piecerules.JaroRules;

public class SimpleFlowTest extends TestCase {
	
	private JaroGame game;
	private JaroController controller;
	private Piece jaro;
	private Piece bug;
	
	@Override
	protected void setUp() throws Exception {
		controller = new JaroController();
		controller.getPieceRules().add(new LevelPasserPreventer());
		game = new JaroGame(new JaroModel(), new JaroView(), controller,
				new LevelManagerTest(), new LevelManagerTest());
		JaroModel model = game.getModel();
		
		Grid grid = new Grid(2, 2);
		
		jaro = new Piece(JaroRules.JARO_TYPE_ID, null);
		grid.addPiece(jaro, 0, 0);

		model.setLevelGrid(grid);

		bug = new Piece(BugRules.BUG_TYPE_ID, null);
		grid.addPiece(bug, 1, 1);

		Piece boulder = new Piece(BoulderRules.BOULDER_TYPE_ID, null);
		grid.addPiece(boulder, 1, 0);
		grid.initIds();
		
		controller.startAcceptingMoves();
	}
	private Grid getGrid() {
		return game.getModel().getGrid();
	}
	public void testSimpleFlow() {
		// show that we can eat a bug
		assertEquals(jaro, getGrid().getPiece(0, 0));
		assertEquals(bug, getGrid().getPiece(1, 1));
		
		controller.move(Direction.down);
		assertEquals(jaro, getGrid().getPiece(0, 1));
		assertEquals(bug, getGrid().getPiece(1, 1));
		
		controller.move(Direction.right);
		assertEquals(jaro, getGrid().getPiece(1, 1));
		assertEquals(1, getGrid().getPieces(1, 1).size());

		// show we can't walk into the boulder
		controller.move(Direction.up);
		assertEquals(jaro, getGrid().getPiece(1, 1));
	}
	public void testUndo() {
		// eat a bug
		assertEquals(jaro, getGrid().getPiece(0, 0));
		assertEquals(bug, getGrid().getPiece(1, 1));
		
		controller.move(Direction.down);
		assertEquals(jaro, getGrid().getPiece(0, 1));
		assertEquals(bug, getGrid().getPiece(1, 1));
		
		controller.move(Direction.right);
		assertEquals(jaro, getGrid().getPiece(1, 1));
		assertEquals(1, getGrid().getPieces(1, 1).size());

		// undo that move
		controller.undoLastMove();
		assertEquals(jaro, getGrid().getPiece(0, 1));
		assertEquals(bug, getGrid().getPiece(1, 1));
		
		// eat it again - this was breaking at one point
		controller.move(Direction.right);
		assertEquals(jaro, getGrid().getPiece(1, 1));
		assertEquals(1, getGrid().getPieces(1, 1).size());
	}
}
