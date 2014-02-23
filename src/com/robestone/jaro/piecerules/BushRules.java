package com.robestone.jaro.piecerules;

import java.util.List;

import com.robestone.jaro.Action;
import com.robestone.jaro.JaroModel;
import com.robestone.jaro.Piece;

public class BushRules extends PieceRulesAdapter {

	public static final String BUSH_TYPE_ID = "bush";
	public static final String BUSH_EMPTY_STATE = "bush_empty";
	public static final String BUSH_WITH_BIRD_STATE = "bush_with_bird";
	
	public BushRules() {
		super(BUSH_TYPE_ID);
		setBlockingStateId(BUSH_WITH_BIRD_STATE);
		setUseStateForSpriteId(true);
	}
	@Override
	public List<Action> getTriggeredActions(Action userAction, JaroModel model) {
		Piece bush = getJaroStandingOn(model, BUSH_TYPE_ID);
		if (bush != null && BUSH_EMPTY_STATE.equals(bush.getState())) {
			Action a = new Action(bush, BUSH_WITH_BIRD_STATE);
			return a.toList();
		}
		return null;
	}
	
	@Override
	public boolean isOkayToEndLevel(JaroModel model) {
		int countEmptyBushes = model.getGrid().countPieces(BUSH_TYPE_ID, BUSH_EMPTY_STATE);
		return countEmptyBushes == 0;
	}

}
