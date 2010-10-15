package com.robestone.gamebase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ActionList implements Action, Iterable<Action> {

	private List<Action> actions = new ArrayList<Action>();
	
	public void add(Action action) {
		actions.add(action);
	}
	
	@Override
	public boolean run() {
		for (Action a: actions) {
			boolean oneRan = a.run();
			if (!oneRan) {
				return false;
			}
		}
		return true;
	}
	public ActionList trimToNull() {
		if (actions.isEmpty()) {
			return null;
		}
		return this;
	}
	public List<Action> getActions() {
		return actions;
	}
	@Override
	public Iterator<Action> iterator() {
		return actions.iterator();
	}
}
