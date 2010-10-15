/**
 * 
 */
package com.robestone.jaro;

import com.robestone.gamebase.Action;
import com.robestone.gamebase.Area;
import com.robestone.gamebase.Grid;
import com.robestone.gamebase.Mover;
import com.robestone.gamebase.Piece;

public class BugEater extends AbstractActor {
	
	@Override
	public Action getProposedAction(JaroGame game, Grid grid, Piece jaro) {
		// see if jar is standing on a bug
		Piece bug = getJaroStandingOn(JaroConstants.bugId, game);
		if (bug == null) {
			bug = getJaroStandingOn(JaroConstants.spider, game);
		}
		if (bug != null) {
			Area from = grid.getAreaContaining(bug);
			Area to = grid.getAreaByKey(JaroConstants.stomach);
			return Mover.moveThisPiece(bug, from, to);
		} else {
			return null;
		}
	}
}