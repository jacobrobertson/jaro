package com.robestone.jaro;

import com.robestone.gamebase.Action;
import com.robestone.gamebase.ActionListener;
import com.robestone.gamebase.Area;
import com.robestone.gamebase.Game;
import com.robestone.gamebase.Grid;
import com.robestone.gamebase.Piece;

public class BugsEatenListener implements ActionListener {

	private Grid grid;
	private GameListener listener;
	private Game game;

	public BugsEatenListener(Grid grid, Game game, GameListener listener) {
		this.grid = grid;
		this.listener = listener;
		this.game = game;
	}
	public static boolean isBug(Piece p) {
		if (JaroConstants.bugId.equals(p.getType())) {
			return true;
		}
		if (JaroConstants.spider.equals(p.getType())) {
			return true;
		}
		return false;
	}
	@Override
	public void actionCompleted(Action action) {
		if (game.getGameState().isOver()) {
			return;
		}
		for (Area a: grid.getAreas()) {
			if (JaroConstants.square.equals(a.getType())) {
				for (Piece p: a) {
					if (isBug(p)) {
						return;
					}
				}
			}
		}
		// no bugs!
		listener.nextLevel();
	}

}
