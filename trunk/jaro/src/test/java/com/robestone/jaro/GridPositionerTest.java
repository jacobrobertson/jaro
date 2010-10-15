package com.robestone.jaro;

import com.robestone.jaro.example.ExampleGame;

import junit.framework.TestCase;

public class GridPositionerTest extends TestCase {

	public void testJaroGameBuilder() {
		doTestJaroGameBuilder(0, 5, 5, false, 0, 0);
		doTestJaroGameBuilder(4, 5, 5, false, 4, 0);
		doTestJaroGameBuilder(0, 5, 5, true, 0, 4);
		doTestJaroGameBuilder(4, 5, 5, true, 0, 0);
	}
	private void doTestJaroGameBuilder(int i, int c, int r, boolean landscape, int x, int y) {
		assertEquals(x, JaroGameBuilder.getX(i, landscape, c, r));
		assertEquals(y, JaroGameBuilder.getY(i, landscape, c, r));
	}
	public void testJaroGameBuilderData() {
		doTestJaroGameBuilderData("xxxx", 2, 2);
		doTestJaroGameBuilderData("xxxxxxxxx", 3, 3);
		doTestJaroGameBuilderData("xxxxxx", 2, 3);
		doTestJaroGameBuilderData("xxxxxx", 3, 2);
	}
	private void doTestJaroGameBuilderData(String data, int c, int r) {
		LevelData ld = new LevelData(data, c, r);
		ExampleGame game = new ExampleGame(ld, false);
		assertEquals(r, game.getGrid().getRows());
		ld = new LevelData(data, r, c);
		game = new ExampleGame(ld, true);
		assertEquals(r, game.getGrid().getRows());
	}
	
	public void testTranslateForLandscape() {
		doTestLandscape(0, 0, false, 0, 0, 0, 0);
		doTestLandscape(3, 2, false, 0, 0, 3, 2);
		doTestLandscape(2, 3, true, 7, 7, 3, 2);
		doTestLandscape(2, 0, true, 5, 6, 4, 2);
	}
	private void doTestLandscape(int x, int y, boolean landscape, int c, int r, int tX, int tY) {
//		GridPositioner pos = new GridPositioner(landscape, c, r);
//		int[] translated = pos.translateForLandscape(x, y);
//		assertEquals(tX, translated[0]);
//		assertEquals(tY, translated[1]);
	}
	
}
