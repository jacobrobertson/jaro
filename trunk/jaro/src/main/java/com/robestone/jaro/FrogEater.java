package com.robestone.jaro;

import com.robestone.gamebase.Action;
import com.robestone.gamebase.ActionList;
import com.robestone.gamebase.Area;
import com.robestone.gamebase.Grid;
import com.robestone.gamebase.Mover;
import com.robestone.gamebase.Piece;
import com.robestone.gamebase.StateChanger;

public class FrogEater extends AbstractActor {
	
	@Override
	public Action getProposedAction(JaroGame game, Grid grid, Piece jaro) {
		// see if jaro is standing on a frog
		Piece frog = getJaroStandingOn(JaroConstants.frog, game);
		if (frog != null) {
			Area from = grid.getAreaContaining(frog);
			Area to = grid.getAreaByKey(JaroConstants.stomach);
			ActionList actions = new ActionList();
			actions.add(Mover.moveThisPiece(frog, from, to));
			actions.add(new StateChanger(jaro, JaroConstants.green_power));
			return actions;
		} else {
			return null;
		}
	}
}