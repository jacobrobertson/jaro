/**
 * 
 */
package com.robestone.jaro;

import com.robestone.gamebase.Action;
import com.robestone.gamebase.Grid;
import com.robestone.gamebase.Piece;
import com.robestone.gamebase.StateChanger;

public class BushSwitcher extends AbstractActor {
	
	@Override
	public Action getProposedAction(JaroGame game, Grid grid, Piece jaro) {
		// see if jar is standing on a bush
		Piece bush = getJaroStandingOn(JaroConstants.bushId, game);
		if (bush != null) {
			return new StateChanger(bush, JaroConstants.bushClosed);
		} else {
			return null;
		}
	}
}