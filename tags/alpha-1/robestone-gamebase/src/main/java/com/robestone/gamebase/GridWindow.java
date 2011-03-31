package com.robestone.gamebase;

import java.util.List;

/**
 * Shows just one part of the grid, but provides the grid interface conveniently.
 * @author jacob
 */
public class GridWindow implements Grid {

	private Grid delegate;
	
	private int rows;
	private int columns;
	
	private int xOffset;
	private int yOffset;

	public GridWindow(Grid delegate) {
		this(delegate, delegate.getRows(), delegate.getColumns(), 0, 0);
	}
	public GridWindow(Grid delegate, int rows, int columns, int xOffset, int yOffset) {
		this.delegate = delegate;
		this.rows = rows;
		this.columns = columns;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	@Override
	public List<Area> getAreas() {
		return delegate.getAreas();
	}
	@Override
	public Area getAreaByKey(Object key) {
		return delegate.getAreaByKey(key);
	}
	@Override
	public List<Area> getAreasByType(Object type) {
		return delegate.getAreasByType(type);
	}
	@Override
	public Area getAreaContaining(Piece piece) {
		return delegate.getAreaContaining(piece);
	}
	@Override
	public boolean isPieceInArea(Piece piece, Object areaKey) {
		return delegate.isPieceInArea(piece, areaKey);
	}
	@Override
	public List<Piece> getPiecesByTypeAndState(Object type, Object state) {
		return delegate.getPiecesByTypeAndState(type, state);
	}
	@Override
	public int getColumns() {
		return columns;
	}
	@Override
	public int getRows() {
		return rows;
	}
	// TODO rename, refactor all these tx things
	private int tx(int x) {
		return x + xOffset;
	}
	private int ty(int y) {
		return y + yOffset;
	}
	private int _tx(int x) {
		return x - xOffset;
	}
	private int _ty(int y) {
		return y - yOffset;
	}
	@Override
	public Area getArea(int x, int y) {
		return delegate.getArea(tx(x), ty(y));
	}
	@Override
	public Piece getPiece(int x, int y) {
		return delegate.getPiece(tx(x), ty(y));
	}
	@Override
	public Area getAreaContainingPieceType(Object pieceType) {
		return delegate.getAreaContainingPieceType(pieceType);
	}
	@Override
	public void addPiece(Piece piece, int x, int y) {
		delegate.addPiece(piece, tx(x), ty(y));
	}
	public void setXOffset(int xOffset) {
		this.xOffset = xOffset;
	}
	public void setYOffset(int yOffset) {
		this.yOffset = yOffset;
	}
	
	/**
	 * @return the underlying Grid
	 */
	public Grid getGrid() {
		return delegate;
	}
	@Override
	public int getColumn(Area a) {
		return _tx(delegate.getColumn(a));
	}
	@Override
	public int getRow(Area a) {
		return _ty(delegate.getRow(a));
	}
}
