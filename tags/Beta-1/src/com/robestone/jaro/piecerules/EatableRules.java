package com.robestone.jaro.piecerules;

import java.util.List;

import com.robestone.jaro.Action;
import com.robestone.jaro.JaroModel;
import com.robestone.jaro.Piece;

public class EatableRules extends PieceRulesAdapter {

	public EatableRules(String typeId) {
		super(typeId);
	}
	@Override
	public List<Action> getTriggeredActions(Action userAction, JaroModel model) {
		Piece eatable = getJaroStandingOn(model, getTypeId());
		if (eatable != null) {
			int x = model.getJaroColumn();
			int y = model.getJaroRow();
			Action action = new Action(eatable, x, y, true);
			return action.toList();
		}
		return null;
	}
	@Override
	public boolean isOkayToEndLevel(JaroModel model) {
		int countEatable = model.getGrid().countPieces(getTypeId(), null);
		return countEatable == 0;
	}
}
