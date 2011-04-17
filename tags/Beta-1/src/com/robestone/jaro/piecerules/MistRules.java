package com.robestone.jaro.piecerules;

import com.robestone.jaro.Piece;

public class MistRules extends PieceRulesAdapter {

	public static final String MIST_TYPE_ID = "mist";
	public static final String MIST_WEAK = "mist_weak";
	
	public MistRules() {
		super(MIST_TYPE_ID);
		setBlockingStateId(MIST_TYPE_ID);
		setUseStateForSpriteId(true);
	}
	
	@Override
	protected Piece buildPiece(String type, String state) {
		return new Piece(type, null, type);
	}
	
}
