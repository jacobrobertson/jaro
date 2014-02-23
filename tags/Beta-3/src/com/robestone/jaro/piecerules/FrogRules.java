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
		int actionsCount = actions.size();
		
		// if we are going to eat a frog, then trigger the turtles, and check the mist too
		if (actionsCount > 0) {
			List<Piece> stubbornTurtlePieces = model.getGrid().getPieces(StubbornTurtleRules.TURTLE_TYPE_ID, StubbornTurtleRules.TURTLESTUBBORN_SUB_TYPE, null);
			for (Piece turtle: stubbornTurtlePieces) {
				Object oldState = turtle.getState();
				Object newState = StubbornTurtleRules.rotateState(oldState);
				actions.add(new Action(turtle, newState));
			}
		
			// if the last frog is about to be eaten, then trigger the mist
			int frogsCount = model.getGrid().countPieces(FROG_TYPE_ID, null);
			if (frogsCount == 1) {
				List<Piece> mistPieces = model.getGrid().getPiecesWithState(MistRules.MIST_TYPE_ID, MistRules.MIST_TYPE_ID);
				for (Piece mist: mistPieces) {
					actions.add(new Action(mist, MistRules.MIST_WEAK));
				}
			}
		}
		return actions;
	}
}
