package com.robestone.gamebase;


public class GameEnder implements Action {

	private Game game;
	
	public GameEnder(Game game) {
		this.game = game;
	}

	@Override
	public boolean run() {
		game.getGameState().setOver(true);
		return true;
	}

}
