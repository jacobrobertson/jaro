package com.robestone.jaro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grid {

	/**
	 * With this strategy, all pieces are unique across grids, which will help slightly
	 * in cases where we're transitioning between two levels.
	 */
	private static int nextId = 0;
	
	private Map<Integer, List<Piece>> pieces = new HashMap<Integer, List<Piece>>();
	private int columns;
	private int rows;
	
	public Grid(int columns, int rows) {
		this.columns = columns;
		this.rows = rows;
	}
	public void initIds() {
		for (List<Piece> list: pieces.values()) {
			for (Piece piece: list) {
				piece.setId(nextId++);
			}
		}
	}
	
	/**
	 * TODO this doesn't try and be efficient at all.  There are any number of strategies to use instead of this.
	 * The point of this method is to allow for undos.
	 */
	public Grid makeHistoricalCopy() {
		Grid clone = new Grid(columns, rows);
		for (Integer id: pieces.keySet()) {
			List<Piece> list = pieces.get(id);
			List<Piece> clist = new ArrayList<Piece>();
			for (Piece piece: list) {
				Piece cpiece = new Piece(piece.getType(), piece.getSubType(), piece.getState());
				cpiece.setId(piece.getId());
				clist.add(cpiece);
			}
			clone.pieces.put(id, clist);
		}
		return clone;
	}
	
	public Piece getPiece(int x, int y) {
		List<Piece> list = getPieces(x, y);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	public int countPieces(String type, Object state) {
		int count = 0;
		for (List<Piece> area: pieces.values()) {
			for (Piece p: area) {
				if (p.getType().equals(type)) {
					if (state == null || p.getState().equals(state)) {
						count++;
					}
				}
			}
		}
		return count;
	}
	public List<Piece> getPieces(String type, Object state) {
		List<Piece> get = new ArrayList<Piece>();
		for (List<Piece> area: pieces.values()) {
			for (Piece p: area) {
				if (p.getType().equals(type)) {
					if (state == null || p.getState().equals(state)) {
						get.add(p);
					}
				}
			}
		}
		return get;
	}
	public List<Piece> getPieces(int x, int y) {
		return getPieces(x, y, false);
	}
	private List<Piece> getPieces(int x, int y, boolean nonNull) {
		Integer key = toIndex(x, y);
		List<Piece> list = pieces.get(key);
		if (list == null && nonNull) {
			list = new ArrayList<Piece>();
			pieces.put(key, list);
		}
		return list;
	}
	public void movePiece(Piece piece, int fromX, int fromY, int toX, int toY) {
		removePiece(piece, fromX, fromY);
		addPiece(piece, toX, toY);
	}
	public void addPiece(Piece piece, int x, int y) {
		List<Piece> list = getPieces(x, y, true);
		list.add(0, piece);
	}
	public void removePiece(Piece piece, int x, int y) {
		List<Piece> list = getPieces(x, y);
		list.remove(piece);
	}
	private Integer toIndex(int x, int y) {
		return x * rows + y;
	}
	public int getColumns() {
		return columns;
	}
	public int getRows() {
		return rows;
	}
	public Piece getPieceByType(int x, int y, String type) {
		List<Piece> pieces = getPieces(x, y);
		if (pieces == null) {
			return null;
		}
		for (Piece p: pieces) {
			if (p.getType().equals(type)) {
				return p;
			}
		}
		return null;
	}
	@Override
	public String toString() {
		return "Grid: " + pieces.toString();
	}

}
