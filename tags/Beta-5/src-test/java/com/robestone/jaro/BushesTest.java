package com.robestone.jaro;

import com.robestone.jaro.piecerules.BushRules;

public class BushesTest extends AbstractGameTest {
	
	private Piece bush = new Piece(BushRules.BUSH_TYPE_ID, BushRules.BUSH_EMPTY_STATE);
	
	public void testJaroWalksOverBush() {
		initGrid(2, bush);
		assertPiece(jaro, 0, 0);
		assertPiece(bush, 1, 0);
		controller.move(Direction.right);
		
		// jaro standing on bush - a bird is in it
		assertPiece(jaro, 1, 0);
		assertStateAndType(BushRules.BUSH_TYPE_ID, BushRules.BUSH_WITH_BIRD_STATE, 1, 0);
		
		game.getModel().saveJaroPosition();
	}
}
