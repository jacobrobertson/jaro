package com.robestone.jaro;

import com.robestone.gamebase.Action;
import com.robestone.gamebase.ActionList;
import com.robestone.gamebase.Area;
import com.robestone.gamebase.Grid;
import com.robestone.gamebase.Mover;
import com.robestone.gamebase.Piece;
import com.robestone.gamebase.StateChanger;

public class TurtlePusher extends AbstractActor {

	@Override
	public Action getProposedAction(JaroGame game, Grid grid, Piece jaro) {
		
		// is jaro standing on a turtle?
		Piece turtle = getJaroStandingOn(JaroConstants.turtle, game);
		if (turtle == null) {
			return null;
		}
		
		ActionList actions = new ActionList();
		
		// push it
		Area currentPos = game.getGrid().getAreaContaining(game.getJaroPiece());
		Area otherSide = TurtleBlocker.getOtherSide(
				game.getGameState().getPreviousJaroPosition(), currentPos, grid);
		
		Area toMoveTo;
		if (!otherSide.isEmpty()) {
			// could only be a hole - or this would have been illegal
			Piece hole = otherSide.get(0);
			actions.add(new StateChanger(hole, JaroConstants.hole_full));
			toMoveTo = grid.getAreaByKey(JaroConstants.stomach);
		} else {
			toMoveTo = otherSide;
		}
		actions.add(Mover.moveThisPiece(turtle, currentPos, toMoveTo));
		
		return actions;
	}

}
