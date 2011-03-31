package com.robestone.gamebase;

import java.util.List;

/**
 * One area in the game, containing pieces.
 * @author jacob
 */
public interface Area extends Iterable<Piece> {

	Object getKey();
	Object getType();
	List<Piece> getPieces();
	boolean isEmpty();
	int size();
	Piece getTopPiece();
	int indexOf(Piece piece);
	Piece get(int index);
	boolean contains(Piece piece);
	
	/**
	 * @return first piece of the type found
	 */
	Piece getPieceByType(Object pieceType);
//	Piece getPieceByKey(Object key);
	
	Object getData(String key);
	void setData(String key, Object data);

}
