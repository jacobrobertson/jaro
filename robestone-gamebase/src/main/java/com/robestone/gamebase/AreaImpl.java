package com.robestone.gamebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AreaImpl implements Area {

	private Object key;
	private Object type;
	
	private List<Piece> pieces;
	private Map<String, Object> dataMap;
	
	public AreaImpl(Object key, Object type) {
		this.key = key;
		this.type = type;
		this.pieces = new ArrayList<Piece>();
	}
	
	public Object getKey() {
		return key;
	}
	public void setKey(Object key) {
		this.key = key;
	}
	public List<Piece> getPieces() {
		return pieces;
	}
	public void setPieces(List<Piece> pieces) {
		this.pieces = pieces;
	}
	public Object getType() {
		return type;
	}
	@Override
	public boolean isEmpty() {
		return pieces.isEmpty();
	}
	@Override
	public int size() {
		return pieces.size();
	}
	@Override
	public Iterator<Piece> iterator() {
		return pieces.iterator();
	}
	@Override
	public Piece get(int index) {
		return pieces.get(index);
	}
	@Override
	public Piece getTopPiece() {
		if (pieces.isEmpty()) {
			return null;
		} else {
			return pieces.get(0);
		}
	}
	@Override
	public int indexOf(Piece piece) {
		return pieces.indexOf(piece);
	}
	@Override
	public boolean contains(Piece piece) {
		return pieces.contains(piece);
	}
	@Override
	public Piece getPieceByType(Object pieceType) {
		for (Piece p: pieces) {
			if (pieceType.equals(p.getType())) {
				return p;
			}
		}
		return null;
	}
	@Override
	public String toString() {
		return String.valueOf(key) + "(" + pieces.size() + ")" + pieces.toString();
	}
	@Override
	public Object getData(String key) {
		if (dataMap == null) {
			return null;
		} else {
			return dataMap.get(key);
		}
	}
	@Override
	public void setData(String key, Object data) {
		if (dataMap == null) {
			dataMap = new HashMap<String, Object>();
		}
		dataMap.put(key, data);
	}
	public boolean equals(Object o) {
		Area that = (Area) o;
		if (!this.getKey().equals(that.getKey())) {
			return false;
		}
		if (this.getPieces().size() != that.getPieces().size()) {
			return false;
		}
		for (int i = 0; i < this.getPieces().size(); i++) {
			Piece thisPiece = this.getPieces().get(i);
			Piece thatPiece = that.getPieces().get(i);
			if (!thisPiece.equals(thatPiece)) {
				return false;
			}
		}
		return true;
	}
}
