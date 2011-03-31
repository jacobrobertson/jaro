package com.robestone.gamebase;

public interface Piece {

	boolean isFaceUp();
	boolean isFaceDown();
	
	boolean setFaceUp();
	boolean setFaceDown();
	boolean setFaceUp(boolean faceUp);
	
	void flip();

	/**
	 * Must be unique across all pieces in a game.  Should probably put as hashCode as well.
	 */
	int getId();
	Object getType();
	Object getSubType();
	boolean isSelected();
	boolean isVisible();
	void setSelected(boolean selected);
	void setVisible(boolean visible);
	
	Object getState();
	void setState(Object state);

}
