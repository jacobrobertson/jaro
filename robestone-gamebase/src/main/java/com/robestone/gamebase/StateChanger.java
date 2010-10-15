package com.robestone.gamebase;

public class StateChanger implements Action {

	private Piece piece;
	private Object newState;
	
	public StateChanger(Piece piece, Object newState) {
		this.piece = piece;
		this.newState = newState;
	}

	@Override
	public boolean run() {
		piece.setState(newState);
		return true;
	}

	public Piece getPiece() {
		return piece;
	}
	

}
