package com.robestone.gamebase;



public class GameImpl implements Game {

	private Board board;
	private GameState gameState;
	
	@Override
	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	

}
