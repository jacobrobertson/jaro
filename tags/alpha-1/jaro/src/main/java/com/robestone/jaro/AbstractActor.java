/**
 * 
 */
package com.robestone.jaro;

import com.robestone.gamebase.Action;
import com.robestone.gamebase.Actor;
import com.robestone.gamebase.Area;
import com.robestone.gamebase.Game;
import com.robestone.gamebase.Grid;
import com.robestone.gamebase.Piece;

public abstract class AbstractActor implements Actor {

	public Piece getJaroStandingOn(Object type, JaroGame game) {
		// see if jaro is standing on it
		Area area = game.getGrid().getAreaContaining(game.getJaroPiece());
		Piece standingOn = area.getPieceByType(type);
		return standingOn;
	}
	
	public abstract Action getProposedAction(JaroGame game, Grid grid, Piece jaro);
	public final Action getProposedAction(Game game) {
		JaroGame jg = (JaroGame) game;
		return getProposedAction(jg, jg.getGrid(), jg.getJaroPiece());
	}
	
}