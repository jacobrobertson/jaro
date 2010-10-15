package com.robestone.gamebase;

import java.util.List;


/**
 * All areas.  Could be called "Areas" instead.
 * For example, will include all common and privately owned areas.
 * 
 * @author jacob
 */
public interface Board {

	List<Area> getAreas();
	Area getAreaByKey(Object key);
	List<Area> getAreasByType(Object type);
	Area getAreaContaining(Piece piece);
	boolean isPieceInArea(Piece piece, Object areaKey);
	List<Piece> getPiecesByTypeAndState(Object type, Object state);
}
