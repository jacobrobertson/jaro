package com.robestone.jaro;

import com.robestone.gamebase.Board;
import com.robestone.gamebase.Game;
import com.robestone.gamebase.Grid;
import com.robestone.gamebase.Piece;
import com.robestone.gamebase.SpriteAwareGame;

/**
 * Single point of holding everything you need to know...
 */
public class JaroGame implements Game, SpriteAwareGame {

	protected Grid grid;
	protected JaroGameState state;
	private Piece spritePiece;
	
	public JaroGame(Piece spritePiece) {
		this.spritePiece = spritePiece;
	}
	@Override
	public Board getBoard() {
		return grid;
	}
	public Grid getGrid() {
		return grid;
	}
	@Override
	public JaroGameState getGameState() {
		return state;
	}
	public Piece getJaroPiece() {
		return spritePiece;
	}
	@Override
	public Piece getMainSprite() {
		return getJaroPiece();
	}
}
