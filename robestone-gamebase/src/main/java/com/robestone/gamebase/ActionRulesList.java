package com.robestone.gamebase;

import java.util.ArrayList;
import java.util.List;

public class ActionRulesList implements ActionRules {

	private List<ActionRules> rules = new ArrayList<ActionRules>();
	
	@Override
	public boolean isLegal(Action action, Game game) {
		for (ActionRules rule: rules) {
			if (!rule.isLegal(action, game)) {
				return false;
			}
		}
		return true;
	}
	
	public void add(ActionRules rules) {
		this.rules.add(rules);
	}

}
