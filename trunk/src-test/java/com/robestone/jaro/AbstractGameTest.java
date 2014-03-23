package com.robestone.jaro;

import junit.framework.TestCase;

import com.robestone.jaro.android.HtmlResources;
import com.robestone.jaro.levels.JaroAssets;
import com.robestone.jaro.levels.JaroResources;
import com.robestone.jaro.piecerules.BugRules;
import com.robestone.jaro.piecerules.JaroRules;
import com.robestone.jaro.piecerules.TurtleRules;

public abstract class AbstractGameTest extends TestCase {
	
	JaroGame game;
	JaroController controller;
	Piece jaro;
	Piece bug;
	Piece turtle;

	@Override
	protected void setUp() throws Exception {
		controller = new JaroController(SoundPlayerMock.INSTANCE);
		controller.getPieceRules().add(new LevelPasserPreventer());
		JaroAssets assets = new JaroFileAssets("src-test/resources");
		JaroResources resources = new HtmlResources(assets, new LevelPersisterMock());
		game = new JaroGame(new JaroModel(resources), new JaroView(), controller,
				new LevelPersisterMock(), resources);
		
		bug = new Piece(BugRules.BUG_TYPE_ID, null);
		jaro = new Piece(JaroRules.JARO_TYPE_ID, null);
		turtle = new Piece(TurtleRules.TURTLE_TYPE_ID, TurtleRules.TURTLE_TYPE_ID);
		
		controller.startAcceptingMoves();
	}
	protected void initGrid(int cols, Piece... pieces) {
		Grid grid = new Grid(cols, 1);
		grid.addPiece(jaro, 0, 0);

		game.getModel().setLevelGrid(grid);
		for (int i = 0; i < pieces.length; i++) {
			if (pieces[i] != null) {
				grid.addPiece(pieces[i], i + 1, 0);
			}
		}
		grid.initIds();
	}
	protected void assertPiece(Piece p, int x, int y) {
		assertEquals(p, getGrid().getPiece(x, y));
	}
	protected void assertStateAndType(String type, Object state, int x, int y) {
		Piece p = getGrid().getPieceByType(x, y, type);
		assertNotNull(p);
		assertEquals(state, p.getState());
	}
	protected Grid getGrid() {
		return game.getModel().getGrid();
	}
}
