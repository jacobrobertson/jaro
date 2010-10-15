package com.robestone.gamebase;

import java.util.ArrayList;
import java.util.List;

public class GameController {

	private List<Actor> gameFlowActors;
	private List<ActionListener> actionListeners;
	private GestureInterpretor interpretor;
	private Gesture currentGesture;
	private ActionRules actionChecker;
	private Game game;

	public GameController(List<Actor> gameFlowActors,
			List<ActionListener> actionListeners,
			GestureInterpretor interpretor) {
		this.gameFlowActors = gameFlowActors;
		this.actionListeners = actionListeners;
		this.interpretor = interpretor;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	public void gesture(GesturePart gesture) {
		Gesture g = new Gesture(gesture);
		gesture(g);
	}
	public void gesture(Gesture gesture) {
		if (currentGesture == null) {
			currentGesture = gesture;
		} else {
			currentGesture.add(gesture);
		}
		GestureInterpretorResult result = interpretor.getActionForGesture(currentGesture, game);
		if (result.isComplete()) {
			currentGesture = null;
		}
		Action action = result.getAction();
		if (action != null) {
			action(action);
		}
	}
	
	public boolean action(Action action) {
		if (actionChecker != null) {
			boolean legal = actionChecker.isLegal(action, game);
			if (!legal) {
				return false;
			}
		}
		
		// TODO use return type to send different message, etc
		List<Action> actions;
		if (action instanceof ActionList) {
			ActionList list = (ActionList) action;
			actions = list.getActions();
		} else {
			actions = new ArrayList<Action>();
			actions.add(action);
		}
		
		for (Action one: actions) {
			one.run();
			for (ActionListener listener: actionListeners) {
				listener.actionCompleted(one);
			}
		}
		
		
		for (Actor gameFlowActor: gameFlowActors) {
			Action gameFlowAction = gameFlowActor.getProposedAction(game);
			if (gameFlowAction != null) {
				gameFlowAction.run();
				// TODO not sure I want this here - should have a different listener?
				for (ActionListener listener: actionListeners) {
					fireAction(gameFlowAction, listener);
				}
			}
		}
		
		return true;
	}
	private void fireAction(Action action, ActionListener listener) {
		if (action instanceof ActionList) {
			ActionList list = (ActionList) action;
			for (Action a: list) {
				listener.actionCompleted(a);
			}
		} else {
			listener.actionCompleted(action);
		}
	}
	public List<ActionListener> getActionListeners() {
		return actionListeners;
	}
	
	public List<Actor> getGameFlowActors() {
		return gameFlowActors;
	}

	public ActionRules getActionChecker() {
		return actionChecker;
	}

	public void setActionChecker(ActionRules actionChecker) {
		this.actionChecker = actionChecker;
	}
	
}
