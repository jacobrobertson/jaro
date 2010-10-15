package com.robestone.gamebase;

import java.util.List;

public class GesturePart {

	private Piece piece;
	private Area area;

	// private Object gestureType; // future -- drag, click, long click, double
	// click, etc

	public GesturePart(Piece piece, Area area) {
		this.piece = piece;
		this.area = area;
	}

	public Piece getPiece() {
		if (piece != null) {
			return piece;
		} else {
			return area.getTopPiece();
		}
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	
	public boolean isForArea(Area area) {
		if (piece == null) {
			return area == this.area;
		} else {
			return area.contains(piece);
		}
	}
	public Area getArea(Board board) {
		if (this.area != null) {
			return area;
		} else {
			return board.getAreaContaining(piece);
		}
	}
	public boolean isForAreaType(Object areaType, Board board) {
		if (area != null && area.getType().equals(areaType)) {
			return true;
		} else if (area == null) {
			List<Area> areas = board.getAreasByType(areaType);
			for (Area area: areas) {
				if (area.contains(piece)) {
					return true;
				}
			}
		}
		return false;
	}

}
