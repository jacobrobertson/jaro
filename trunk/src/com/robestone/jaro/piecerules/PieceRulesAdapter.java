package com.robestone.jaro.piecerules;

import java.util.List;

import com.robestone.jaro.Action;
import com.robestone.jaro.JaroModel;
import com.robestone.jaro.Piece;
import com.robestone.jaro.PieceRules;

/**
 * Implements {@link PieceRules} with simple implementations of most operations.
 * @author jacob
 */
public class PieceRulesAdapter implements PieceRules {

	private String typeId;
	
	private boolean useStateForSpriteId;
	private boolean useBlockingRule;
	private Object blockingStateId;
	
	public PieceRulesAdapter(String typeId) {
		this.typeId = typeId;
	}
	public void setUseStateForSpriteId(boolean useStateForSpriteId) {
		this.useStateForSpriteId = useStateForSpriteId;
	}
	protected void setBlockingStateId(Object blockingStateId) {
		this.blockingStateId = blockingStateId;
		useBlockingRule = true;
	}
	@Override
	public String getTypeId() {
		return typeId;
	}
	@Override
	public final String getSpriteId(Piece piece, boolean isLandscape) {
		if (!typeId.equals(piece.getType())) {
			return null;
		}
		return doGetSpriteId(piece, isLandscape);
	}
	protected String doGetSpriteId(Piece piece, boolean isLandscape) {
		if (useStateForSpriteId) {
			return piece.getState().toString();
 		} else {
 			return typeId.toString();
 		}
	}

	@Override
	public List<Action> getTriggeredActions(Action userAction, JaroModel model) {
		return null;
	}
	
	@Override
	public boolean isOkayToEndLevel(JaroModel model) {
		return true;
	}
	
	@Override
	public boolean isLegal(Action proposedAction, JaroModel model) {
		if (useBlockingRule) {
			// are we trying to move onto a blocker type?
			int x = proposedAction.getToX();
			int y = proposedAction.getToY();
			Piece check = model.getGrid().getPiece(x, y);
			if (check == null) {
				return true;
			}
			String type = check.getType();
			Object state = check.getState();
			// check on type matches
			if (this.typeId.equals(type)) {
				if (blockingStateId == null) {
					// we don't care about state
					return false;
				} else if (blockingStateId.equals(state)) {
					// both state and type match
					return false;
				}
			}
		}
		return true;
	}
	protected Piece getJaroStandingOn(JaroModel model, String type) {
		int x = model.getJaroColumn();
		int y = model.getJaroRow();
		return model.getGrid().getPieceByType(x, y, type);
	}
	/**
	 * Assumes that we're passing an image name in like "turtle_down.png".  In this
	 * example, the type will be "turtle" and the state will be "turtle_down".
	 */
	@Override
	public Piece parsePiece(String parseKey) {
		int dot = parseKey.indexOf('.');
		if (dot < 0) {
			dot = parseKey.length();
		}
		int underscore = parseKey.indexOf('_');
		if (underscore < 0) {
			underscore = dot;
		}
		String type = parseKey.substring(0, underscore);
		if (isParseTypeForPiece(type)) {
			String state = parseKey.substring(0, dot);
			return buildPiece(type, state);
		} else {
			return null;
		}
	}
	protected boolean isParseTypeForPiece(String parseType) {
		return parseType.equals(getTypeId());
	}
	
	/**
	 * Subclasses might just override this instead of {@link #parsePiece(String)}
	 * to (for example) use the state as the subtype instead.
	 */
	protected Piece buildPiece(String type, String state) {
		return new Piece(type, null, state);
	}

}
