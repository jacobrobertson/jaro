package com.robestone.gamebase;


public class PieceSelector implements Action {

	private Piece piece;
	private boolean selected;
	
	public PieceSelector(Piece piece, boolean selected) {
		this.piece = piece;
		this.selected = selected;
	}

	@Override
	public boolean run() {
		piece.setSelected(selected);
		return true;
	}

}
