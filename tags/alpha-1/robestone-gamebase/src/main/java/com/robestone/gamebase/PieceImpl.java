package com.robestone.gamebase;

public class PieceImpl implements Piece {

	private int id;
	private Object type;
	private Object subType;
	private Object state;
	
	private boolean selected;
	private boolean visible;
	private boolean faceUp;

	public PieceImpl(int id, Object type) {
		this(id, type, type);
	}
	public PieceImpl(int id, Object type, Object subType) {
		this.id = id;
		this.type = type;
		this.subType = subType;
	}

	@Override
	public int getId() {
		return id;
	}
	@Override
	public int hashCode() {
		return id;
	}
	@Override
	public boolean equals(Object obj) {
		Piece that = (Piece) obj;
		if ((this.state == null) != (that.getState() == null)) {
			return false;
		}
		// TODO this has a long way to go!!
		return that.getId() == id &&
			(this.state == null ||
			that.getState().equals(this.state));
	}
	
//	@Override
//	public Object getKey() {
//		return key;
//	}

	@Override
	public Object getType() {
		return type;
	}
	@Override
	public Object getSubType() {
		return subType;
	}
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	@Override
	public void flip() {
		faceUp = !faceUp;
	}

	@Override
	public boolean isFaceDown() {
		return !faceUp;
	}

	@Override
	public boolean isFaceUp() {
		return faceUp;
	}

	@Override
	public boolean setFaceDown() {
		return setFaceUp(false);
	}

	@Override
	public boolean setFaceUp() {
		return setFaceUp(true);
	}

	@Override
	public boolean setFaceUp(boolean faceUp) {
		if (this.faceUp == faceUp) {
			return false;
		}
		this.faceUp = faceUp;
		return true;
	}

	public Object getState() {
		return state;
	}

	public void setState(Object state) {
		this.state = state;
	}
	
}
