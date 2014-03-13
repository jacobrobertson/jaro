package com.robestone.jaro.piecerules;

import java.util.List;

import com.robestone.jaro.Action;
import com.robestone.jaro.JaroModel;
import com.robestone.jaro.Piece;

public class BushRules extends PieceRulesAdapter {

	public static final String BUSH_TYPE_ID = "bush";
	public static final String BUSH_EMPTY_STATE = "bush_empty";
	public static final String BUSH_WITH_BIRD_STATE = "bush_with_bird";
	public static final String BUSH_WITH_APPLES_STATE = "bush_with_apples";
	
	public BushRules() {
		super(BUSH_TYPE_ID);
		setBlockingStateId(BUSH_WITH_BIRD_STATE);
		setUseStateForSpriteId(true);
	}
	@Override
	public List<Action> getTriggeredActions(Action userAction, JaroModel model) {
		Piece bush = getJaroStandingOn(model, BUSH_TYPE_ID);
		if (bush != null && BUSH_EMPTY_STATE.equals(bush.getState())) {
			return new Action(bush, BUSH_WITH_BIRD_STATE).toList();
		}
		bush = model.getGrid().getPiece(userAction.getFromX(), userAction.getFromY());
		if (bush != null && BUSH_WITH_APPLES_STATE.equals(bush.getState())) {
			return new Action(bush, BUSH_EMPTY_STATE).toList();
		}
		return null;
	}
	
	@Override
	public boolean isOkayToEndLevel(JaroModel model) {
		List<Piece> bushes = model.getGrid().getPieces(BUSH_TYPE_ID, null, null);
		for (Piece bush: bushes) {
			if (!BUSH_WITH_BIRD_STATE.equals(bush.getState())) {
				return false;
			}
		}
		return true;
	}

}
