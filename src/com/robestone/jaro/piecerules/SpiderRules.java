package com.robestone.jaro.piecerules;

import java.util.ArrayList;
import java.util.List;

import com.robestone.jaro.Action;
import com.robestone.jaro.Grid;
import com.robestone.jaro.JaroModel;
import com.robestone.jaro.Piece;

public class SpiderRules extends EatableRules {

	public static final String SPIDER_TYPE_ID = "spider";
	public static final String SPIDER_BIG_STATE = "spider_big";
	public static final String SPIDER_SMALL_STATE = "spider_small";
	
	public SpiderRules() {
		super(SPIDER_TYPE_ID);
		setBlockingStateId(SPIDER_BIG_STATE);
		setUseStateForSpriteId(true);
	}
	public List<Action> getTriggeredActions(Action userAction, JaroModel model) {
		List<Action> actions = super.getTriggeredActions(userAction, model);
		if (actions == null) {
			actions = new ArrayList<Action>();
		}
		
		Grid grid = model.getGrid();
		// get spiders, but if there are none, don't bother
		List<Piece> spiders = grid.getPiecesWithState(SPIDER_TYPE_ID, SPIDER_BIG_STATE);
		if (spiders.isEmpty()) {
			return actions;
		}

		// get all empty or apple bushes - if there are any, then we're done
		List<Piece> emptyBushes = grid.getPiecesWithState(BushRules.BUSH_TYPE_ID, BushRules.BUSH_EMPTY_STATE);
		if (!emptyBushes.isEmpty()) {
			return actions;
		}
		List<Piece> appleBushes = grid.getPiecesWithState(BushRules.BUSH_TYPE_ID, BushRules.BUSH_WITH_APPLES_STATE);
		if (!appleBushes.isEmpty()) {
			return actions;
		}
		
		// get all bird bushes - if there are none, then that's odd, but it means
		// it's a level with spiders, and no bushes?
		List<Piece> birdBushes = grid.getPiecesWithState(BushRules.BUSH_TYPE_ID, BushRules.BUSH_WITH_BIRD_STATE);
		if (birdBushes.isEmpty()) {
			return actions;
		}
		
		// ---- removing this behavior for consistency with other rules
		// look at each closed bush, and if jaro is standing on one, then we're done
		// -- this is because we don't scare the spiders until we step off the bush
//		List<Piece> jaroPieces = grid.getPieces(model.getJaroColumn(), model.getJaroRow());
//		for (Piece b: birdBushes) {
//			if (jaroPieces.contains(b)) {
//				return actions;
//			}
//		}
		
		// this is it!  create the actions
		for (Piece p: spiders) {
			actions.add(new Action(p, SPIDER_SMALL_STATE));
		}
		return actions;
	}
}
