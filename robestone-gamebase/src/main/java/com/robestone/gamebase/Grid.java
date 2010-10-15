package com.robestone.gamebase;


/**
 * TODO might want to abstract out some of Board interface, and have subtypes that apply more to 
 * 		"card games" versus "grid games" - for example one wants coordinates and one doesn't.
 * TODO should move to game base
 * TODO some of these methods should move up to Board
 * @author jacob
 */
public interface Grid extends Board {

	int getColumns();
	int getRows();
	Area getArea(int x, int y);
	Piece getPiece(int x, int y);
//	Piece getPiece(int x, int y, Object pieceId);
	Area getAreaContainingPieceType(Object pieceType);
//	Piece getPieceByAreaKeyAndPieceKey(Object areaKey, Object pieceId);
//	Object getPieceKey(int x, int y);
	void addPiece(Piece piece, int x, int y);
	int getRow(Area a);
	int getColumn(Area a);
	
}
