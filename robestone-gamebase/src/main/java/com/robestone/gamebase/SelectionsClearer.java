package com.robestone.gamebase;


public class SelectionsClearer implements Action {

	private Board board;
	
	public SelectionsClearer(Board board) {
		this.board = board;
	}

	@Override
	public boolean run() {
		for (Area a: board.getAreas()) {
			for (Piece p: a.getPieces()) {
				p.setSelected(false);
			}
		}
		return false;
	}

}
