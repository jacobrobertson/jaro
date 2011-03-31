package com.robestone.jaro;

public class Piece {

	private int id;
	private String type;
	private String subType;

	private Object state;

	public Piece(String typeId, Object state) {
		this(typeId, null, state);
	}
	public Piece(String typeId, String subTypeId, Object state) {
		this.type = typeId;
		this.subType = subTypeId;
		this.state = state;
	}
	public String getType() {
		return type;
	}
	public Object getState() {
		return state;
	}
	public void setState(Object state) {
		this.state = state;
	}
	public String getSubType() {
		return subType;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder("Piece: ");
		buf.append(type);
		if (subType != null) {
			buf.append("/");
			buf.append(subType);
		}
		if (state != null) {
			buf.append("/");
			buf.append(state);
		}
		return buf.toString();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		Piece other = (Piece) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
