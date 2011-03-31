package com.robestone.gamebase;

import java.util.ArrayList;
import java.util.List;


public class GridImpl extends BoardImpl implements Grid {
	
	private static final String POS_KEY = "grid-position";
	private int columns;
	private int rows;
	private static class Point { 
		int x, y;
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		} 
	}
	
	public GridImpl(int w, int h, List<Area> additionalAreas, Object areaType) {
		super(initAreas(w, h, additionalAreas, areaType));
		this.columns = w;
		this.rows = h;
	}
	private static List<Area> initAreas(int w, int h, List<Area> additionalAreas, Object areaType) {
		List<Area> areas = new ArrayList<Area>(additionalAreas);
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Object k = toKey(i, j);
				AreaImpl a = new AreaImpl(k, areaType);
				setXY(i, j, a);
				areas.add(a);
			}
		}
		return areas;
	}
	private static void setXY(int x, int y, Area a) {
		a.setData(POS_KEY, new Point(x, y));
	}
	private static Point getPoint(Area a) {
		return (Point) a.getData(POS_KEY);
	}
	private static Object toKey(int x, int y) {
		return Integer.valueOf(x * 1000 + y); // TODO this is dumb
	}
	public int getColumns() {
		return columns;
	}
	public int getRows() {
		return rows;
	}
	public Area getArea(int x, int y) {
		return getAreaByKey(toKey(x, y));
	}
	public Piece getPiece(int x, int y) {
		List<Area> areas = getAreas();
		for (Area area: areas) {
			Point p = getPoint(area);
			if (p != null && p.x == x && p.y == y) {
				List<Piece> s = area.getPieces();
				if (s == null) {
					continue;
				}
				if (s.isEmpty()) {
					continue;
				}
				return area.get(0);
			}
		}
		return null;
	}
	public Area getAreaContainingPieceType(Object pieceType) {
		List<Area> areas = getAreas();
		for (Area area: areas) {
			List<Piece> s = area.getPieces();
			if (s == null) {
				continue;
			}
			if (s.isEmpty()) {
				continue;
			}
			for (Piece piece: s) {
				if (piece.getType().equals(pieceType)) {
					return area;
				}
			}
		}
		return null;
	}
	public void addPiece(Piece piece, int x, int y) {
		Object k = toKey(x, y);
		Area area = getAreaByKey(k);
		if (area == null) {
			throw new IllegalArgumentException("bad coordinates: " + x + ", " + y);
		}
		area.getPieces().add(piece);
	}
	@Override
	public int getRow(Area a) {
		return getPoint(a).y;
	}
	@Override
	public int getColumn(Area a) {
		return getPoint(a).x;
	}
}
