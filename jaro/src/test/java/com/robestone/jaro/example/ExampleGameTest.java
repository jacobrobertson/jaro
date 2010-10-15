package com.robestone.jaro.example;

import junit.framework.TestCase;

import com.robestone.gamebase.GameController;
import com.robestone.gamebase.Gesture;
import com.robestone.gamebase.Grid;
import com.robestone.jaro.JaroConstants;
import com.robestone.jaro.LevelData;
import com.robestone.jaro.example.ExampleGame;

/**
 * Test jaro can eat a bug!
 * @author jacob
 */
public class ExampleGameTest extends TestCase {

	private String data = 
		"j ozo  " +
		"o n ooo" +
		"ooooooo" +
		"";
	
	private ExampleGame example = new ExampleGame(new LevelData(data, 7, 3), false);
	
	public void testFlow() {
		Grid grid = example.getGrid();
		GameController controller = example.getController();
//		MoverList mover = example.getMover();
		
		// assert the grid state is what you expect
		assertSame(grid.getPiece(0, 0).getType(), JaroConstants.jaroId);
		assertEquals(1, grid.getArea(0, 0).size());
		assertNull(grid.getPiece(1, 0));
		
		// move once (gesture)
		controller.gesture(newGesture(1, 0));
		
		// assert that jaro moved
		assertSame(grid.getPiece(1, 0).getType(), JaroConstants.jaroId);
		assertNull(grid.getPiece(0, 0));
		assertEquals(0, grid.getArea(0, 0).size());

		// show that if we click a "bad" spot, jaro does not move
		controller.gesture(newGesture(5, 0));
		assertSame(grid.getPiece(1, 0).getType(), JaroConstants.jaroId);
		assertNull(grid.getPiece(5, 0));
		
		// walk through the bush and show the bush before and after
		controller.gesture(newGesture(1, 1));
		assertSame(grid.getPiece(2,1).getState(), JaroConstants.bushOpen);
		controller.gesture(newGesture(2, 1));
		controller.gesture(newGesture(3, 1));
		assertSame(grid.getPiece(2,1).getState(), JaroConstants.bushClosed);
		
		// cannot walk back through!
		assertSame(grid.getPiece(3, 1).getType(), JaroConstants.jaroId);
		controller.gesture(newGesture(2, 1));
		assertSame(grid.getPiece(3, 1).getType(), JaroConstants.jaroId);
		
		// walk over a bug now
		assertSame(grid.getPiece(3, 0).getType(), JaroConstants.bugId);
		controller.gesture(newGesture(3, 0));
		controller.gesture(newGesture(3, 1));
		
		// assert the bug is gone
		assertSame(grid.getPiece(3, 1).getType(), JaroConstants.jaroId);
		assertNull(grid.getPiece(3, 0));
		
		// show we cannot walk onto the boulder
		assertSame(grid.getPiece(3, 1).getType(), JaroConstants.jaroId);
		assertSame(grid.getPiece(4, 1).getType(), JaroConstants.boulderId);
		controller.gesture(newGesture(4, 1));
		assertSame(grid.getPiece(3, 1).getType(), JaroConstants.jaroId);
		assertSame(grid.getPiece(4, 1).getType(), JaroConstants.boulderId);
	}
	private Gesture newGesture(int x, int y) {
		return example.newGesture(x, y);
	}
}
