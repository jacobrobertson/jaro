package com.robestone.jaro;

import java.util.List;

public class LevelPasserPreventer implements PieceRules {

	@Override
	public String getTypeId() {
		return "tester";
	}

	@Override
	public String getSpriteId(Piece piece, boolean isLandscape) {
		return null;
	}

	@Override
	public boolean isOkayToEndLevel(JaroModel model) {
		return false;
	}

	@Override
	public boolean isLegal(Action proposedAction, JaroModel model) {
		return true;
	}

	@Override
	public List<Action> getTriggeredActions(Action proposedAction,
			JaroModel model) {
		return null;
	}

	@Override
	public Piece parsePiece(String parseKey) {
		return null;
	}

}
