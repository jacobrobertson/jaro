package com.robestone.jaro.piecerules;

import java.util.ArrayList;
import java.util.List;

import com.robestone.jaro.Action;
import com.robestone.jaro.JaroModel;
import com.robestone.jaro.Piece;

public class FrogRules extends EatableRules {

	public static final String FROG_TYPE_ID = "frog";
	
	public FrogRules() {
		super(FROG_TYPE_ID);
	}
	public List<Action> getTriggeredActions(Action userAction, JaroModel model) {
		List<Action> actions = super.getTriggeredActions(userAction, model);
		if (actions == null) {
			actions = new ArrayList<Action>();
		}
		
		// if we ate the frog, then also do all the mists
		int actionsCount = actions.size();
		int frogsCount = model.getGrid().countPieces(FROG_TYPE_ID, null);
		int totalCount = frogsCount - actionsCount;
		if (totalCount == 0) {
			List<Piece> mistPieces = model.getGrid().getPieces(MistRules.MIST_TYPE_ID, MistRules.MIST_TYPE_ID);
			for (Piece mist: mistPieces) {
				actions.add(new Action(mist, MistRules.MIST_WEAK));
			}
		}
		return actions;
	}
}
