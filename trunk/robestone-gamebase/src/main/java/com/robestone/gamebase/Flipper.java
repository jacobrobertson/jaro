package com.robestone.gamebase;


public class Flipper implements Action {

	private Area area;
	
	private boolean showNextPiece = false;
	
	public Flipper(Area area) {
		this.area = area;
	}

	@Override
	public boolean run() {
		area.getTopPiece().flip();
		if (showNextPiece && area.size() > 1) {
			area.get(1).setVisible(true);
		}
		return true;
	}

	/**
	 * When you flip this piece over, do you want the piece below it to be marked as visible.
	 * @param showNextPiece
	 */
	public void setShowNextPiece(boolean showNextPiece) {
		this.showNextPiece = showNextPiece;
	}

}
