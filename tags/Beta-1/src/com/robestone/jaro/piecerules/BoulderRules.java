package com.robestone.jaro.piecerules;

public class BoulderRules extends PieceRulesAdapter {

	public static final String BOULDER_TYPE_ID = "boulder";
	
	public BoulderRules() {
		super(BOULDER_TYPE_ID);
		setBlockingStateId(null);
	}
	
}
