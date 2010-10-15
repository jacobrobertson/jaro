package com.robestone.gamebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BoardImpl implements Board {

	private List<Area> areas;
	private Map<Object, List<Area>> areasByType = new HashMap<Object, List<Area>>();
	private Map<Object, Area> areasByKey = new HashMap<Object, Area>();
	
	public BoardImpl(List<Area> areas) {
		setAreas(areas);
	}
	
	@Override
	public List<Area> getAreas() {
		return areas;
	}
	public void setAreas(List<Area> areas) {
		this.areas = areas;
		for (Area a: areas) {
			List<Area> list = areasByType.get(a.getType());
			if (list == null) {
				list = new ArrayList<Area>();
				areasByType.put(a.getType(), list);
			}
			list.add(a);
			areasByKey.put(a.getKey(), a);
		}
	}
	@Override
	public Area getAreaByKey(Object key) {
		return areasByKey.get(key);
	}
	@Override
	public List<Area> getAreasByType(Object type) {
		return areasByType.get(type);
	}
	@Override
	public Area getAreaContaining(Piece piece) {
		for (Area a: areas) {
			int index = a.indexOf(piece);
			if (index >= 0) {
				return a;
			}
		}
		return null;
	}
	@Override
	public boolean isPieceInArea(Piece piece, Object areaKey) {
		Area containing = getAreaByKey(areaKey);
		return containing.indexOf(piece) >= 0;
	}
	public List<Piece> getAllPiecesByType(Object type) {
		List<Piece> pieces = new ArrayList<Piece>();
		for (Area a: areas) {
			for (Piece p: a) {
				if (type.equals(p.getType())) {
					pieces.add(p);
				}
			}
		}
		return pieces;
	}
	public List<Piece> getPiecesByTypeAndState(Object type, Object state) {
		List<Piece> pieces = new ArrayList<Piece>();
		
		for (Area a: areas) {
			for (Piece p: a) {
				if (type.equals(p.getType()) && state.equals(p.getState())) {
					pieces.add(p);
				}
			}
		}
		
		return pieces;
	}
	public boolean equals(Object o) {
		Board that = (Board) o;
		if (this.getAreas().size() != that.getAreas().size()) {
			return false;
		}
		for (int i = 0; i < this.getAreas().size(); i++) {
			Area thisArea = this.getAreas().get(i);
			Area thatArea = that.getAreas().get(i);
			if (!thisArea.equals(thatArea)) {
				return false;
			}
		}
		return true;
	}
}
