package com.robestone.jaro;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.robestone.jaro.android.HtmlResources;
import com.robestone.jaro.levels.JaroAssets;
import com.robestone.jaro.piecerules.JaroRules;

public class JaroViewTest extends TestCase {

	public void testLandscape() {
		doTestLandscape();
	}
	private JaroGame doTestLandscape() {
		
		JaroAssets assets = new JaroFileAssets("src-test/resources");
		HtmlResources resources = new HtmlResources(assets, new LevelPersisterMock());
		
		JaroGame game = new JaroGame(new JaroModel(resources), new JaroView(), new JaroController(),
				new LevelPersisterMock(), resources);
		JaroModel model = game.getModel();
		
		int c = 3;
		int r = 2;
		Grid grid = new Grid(c, r);
		grid.addPiece(new Piece(JaroRules.JARO_TYPE_ID, null), 0, 0);
		model.setLevelGrid(grid);
		JaroView view = game.getView();
		
		assertEquals(grid.getColumns(), view.getColumns(false));
		assertEquals(grid.getColumns(), view.getRows(true));

		assertEquals(grid.getRows(), view.getRows(false));
		assertEquals(grid.getRows(), view.getColumns(true));

		grid.addPiece(buildPiece("A"), 0, 0);
		grid.addPiece(buildPiece("B"), 1, 0);
		grid.addPiece(buildPiece("C"), 2, 0);

		grid.addPiece(buildPiece("D"), 0, 1);
		grid.addPiece(buildPiece("E"), 1, 1);
		grid.addPiece(buildPiece("F"), 2, 1);
		
		assertPiece("F", 2, 1, view, false);
		assertPiece("D", 1, 2, view, true);
		
		assertPiece("E", 1, 1, view, false);
		assertPiece("E", 1, 1, view, true);

		assertPiece("A", 0, 0, view, false);
		assertPiece("A", 0, 2, view, true);
		
		game.getModel().saveJaroPosition();
		
		return game;
	}
	private void assertPiece(String s, int x, int y, JaroView view, boolean landscape) {
		List<Piece> list = view.getPieces(x, y, landscape);
		Piece p = list.get(0);
		assertEquals(s, p.getType());
	}
	private Piece buildPiece(String s) {
		return new Piece(s, null, null);
	}

	/**
	 * Try to see what happens with multiple threads on the view.
	 */
	public void testThreads() throws Exception {
		final JaroGame game = doTestLandscape();
		
		// test once - show it's not the test that failes
		doTestThreadOnce(game);
		
		int threads = 1000;
		int threadCount = 0;

		// test that it works without threads
		for (int i = 0; i < threads; i++) {
			threadCount += doTestThreadOnce(game);
		}
		assertEquals(threads, threadCount);

		List<Runner> runners = new ArrayList<Runner>();
		for (int i = 0; i < threads; i++) {
			Runner r = new Runner();
			r.game = game;
			runners.add(r);
		}
		
		// prove that status is false
		threadCount = 0;
		for (int i = 0; i < threads; i++) {
			Runner r = runners.get(i);
			if (r.status) {
				threadCount++;
			}
		}
		assertEquals(0, threadCount);
		
		// run all threads
		for (int i = 0; i < threads; i++) {
			Runner r = runners.get(i);
			new Thread(r).start();
		}
		
		// wait until all threads are done
		while (threadCount < threads) {
			threadCount = 0;
			for (int i = 0; i < threads; i++) {
				Runner r = runners.get(i);
				if (r.complete) {
					threadCount++;
				}
			}
			Thread.sleep(1);
		}
		
		// assert that each thread ran okay
		threadCount = 0;
		for (int i = 0; i < threads; i++) {
			Runner r = runners.get(i);
			if (r.status) {
				threadCount++;
			}
		}
		assertEquals(threads, threadCount);
	}
	private class Runner implements Runnable {
		public boolean complete = false;
		public boolean status = false;
		JaroGame game;
		@Override
		public void run() {
			try {
				doTestThreadOnce(game);
				status = true;
			} finally {
				complete = true;
			}
		}
	}
	int doTestThreadOnce(JaroGame game) {
		game.getView().getRows(false);
		JaroModel model = game.getModel();
		model.cloneCurrent();
		game.getView().getRows(false);
		model.undo();
		return 1;
	}
	
}
