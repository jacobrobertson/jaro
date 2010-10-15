package com.robestone.jaro;

import java.util.List;

import com.robestone.gamebase.Action;
import com.robestone.gamebase.ActionList;
import com.robestone.gamebase.Area;
import com.robestone.gamebase.Grid;
import com.robestone.gamebase.Piece;
import com.robestone.gamebase.StateChanger;

public class SpiderScarer extends AbstractActor {
	
	@Override
	public Action getProposedAction(JaroGame game, Grid grid, Piece jaro) {
		// get spiders, but if there are none, don't bother
		List<Piece> spiders = grid.getPiecesByTypeAndState(JaroConstants.spider, JaroConstants.spider_big);
		if (spiders.isEmpty()) {
			return null;
		}

		// get all open bushes - if there are any, then we're done
		List<Piece> openBushes = grid.getPiecesByTypeAndState(JaroConstants.bushId, JaroConstants.bushOpen);
		if (!openBushes.isEmpty()) {
			return null;
		}
		
		// get all closed bushes - if there are none, then that's odd, but it means
		// it's a level with spiders, and no bushes?
		List<Piece> closedBushes = grid.getPiecesByTypeAndState(JaroConstants.bushId, JaroConstants.bushClosed);
		if (closedBushes.isEmpty()) {
			return null;
		}
		
		// look at each closed bush, and if jaro is standing on one, then we're done
		Area ja = grid.getAreaContaining(jaro);
		for (Piece b: closedBushes) {
			if (ja.contains(b)) {
				return null;
			}
		}
		
		// this is it!  create the actions
		ActionList list = new ActionList();
		for (Piece p: spiders) {
			list.add(new StateChanger(p, JaroConstants.spider_small));
		}
		return list;
	}
}