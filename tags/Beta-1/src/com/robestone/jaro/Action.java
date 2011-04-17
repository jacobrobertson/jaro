package com.robestone.jaro;

import java.util.ArrayList;
import java.util.List;


public class Action {

	private Piece actingPiece;
	private int fromX;
	private int fromY;
	private int toX;
	private int toY;
	private Object newStateId;
	private boolean removePiece;

	public Action(Piece actingPiece, Object newStateId) {
		this.actingPiece = actingPiece;
		this.newStateId = newStateId;
	}

	public Action(Piece actingPiece, int fromX, int fromY, boolean removePiece) {
		this.actingPiece = actingPiece;
		this.fromX = fromX;
		this.fromY = fromY;
		this.removePiece = removePiece;
	}

	public Action(Piece actingPiece, int fromX, int fromY, int toX, int toY) {
		this.actingPiece = actingPiece;
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
	}
	
	public void run(JaroModel model) {
		if (removePiece) {
			Grid grid = model.getGrid();
			grid.removePiece(actingPiece, fromX, fromY);
		} else if (newStateId != null) {
			actingPiece.setState(newStateId);
		} else {
			Grid grid = model.getGrid();
			grid.movePiece(actingPiece, fromX, fromY, toX, toY);
		}
	}

	public Piece getActingPiece() {
		return actingPiece;
	}

	public int getFromX() {
		return fromX;
	}

	public int getFromY() {
		return fromY;
	}

	public int getToX() {
		return toX;
	}

	public int getToY() {
		return toY;
	}

	public Object getNewStateId() {
		return newStateId;
	}
	public boolean isRemovePiece() {
		return removePiece;
	}
	public boolean isMovePiece() {
		return !removePiece && newStateId == null;
	}
	public List<Action> toList() {
		List<Action> actions = new ArrayList<Action>();
		actions.add(this);
		return actions;
	}
	
}
