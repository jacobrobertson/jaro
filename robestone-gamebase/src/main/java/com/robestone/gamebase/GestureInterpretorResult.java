package com.robestone.gamebase;


public class GestureInterpretorResult {

	private Action action;
	private boolean complete;
	
	public GestureInterpretorResult(Action action, boolean complete) {
		this.action = action;
		this.complete = complete;
	}
	public Action getAction() {
		return action;
	}
	public boolean isComplete() {
		return complete;
	}
	
}
