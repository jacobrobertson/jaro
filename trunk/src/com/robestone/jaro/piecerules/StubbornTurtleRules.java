package com.robestone.jaro.piecerules;

import com.robestone.jaro.Action;
import com.robestone.jaro.Grid;
import com.robestone.jaro.JaroModel;
import com.robestone.jaro.Piece;
import com.robestone.jaro.SpriteMapper;

/**
 * Builds on the turtle rules, but only allows you to push the turtle in the direction it's facing.
 * @author Jacob
 */
public class StubbornTurtleRules extends TurtleRules {

	private static final String TURTLESTUBBORN_PARSE_KEY = "turtlestubborn";
	public static final String TURTLESTUBBORN_SUB_TYPE = "stubborn";
	private static final String TURTLESTUBBORN_STATE_LEFT = "turtlestubborn_left";
	private static final String TURTLESTUBBORN_STATE_RIGHT = "turtlestubborn_right";
	private static final String TURTLESTUBBORN_STATE_UP = "turtlestubborn_up";
	private static final String TURTLESTUBBORN_STATE_DOWN = "turtlestubborn_down";
	
	public StubbornTurtleRules() {
		super();
	}

	@Override
	public boolean isLegal(Action proposedAction, JaroModel model) {

		// don't bother if it's not even for turtles
		if (!isActionForTurtleMove(proposedAction, model)) {
			return true;
		}
		
		// we can check super first, because stubborn turtles are turtles too!
		if (!super.isLegal(proposedAction, model)) {
			return false;
		}
		
		// just check whether this is a stubborn turtle
		int toX = proposedAction.getToX();
		int toY = proposedAction.getToY();
		Grid grid = model.getGrid();
		Piece turtle = grid.getPiece(toX, toY);
		String subType = turtle.getSubType();
		if (!TURTLESTUBBORN_SUB_TYPE.equals(subType)) {
			// not a relevant situation for us
			return true;
		}
		
		// at this point, we know we're trying to push a stubborn turtle, so we can skip all that logic, and just check the direction

		int fromX = proposedAction.getFromX();
		int fromY = proposedAction.getFromY();
		
		// determine the direction
		Object state = turtle.getState();
		int xd = fromX - toX;
		if (xd == 1) {
			return TURTLESTUBBORN_STATE_LEFT.equals(state);
		} else if (xd == -1) {
			return TURTLESTUBBORN_STATE_RIGHT.equals(state);
		}
		int yd = fromY - toY;
		if (yd == 1) {
			return TURTLESTUBBORN_STATE_UP.equals(state);
		} else if (yd == -1) {
			return TURTLESTUBBORN_STATE_DOWN.equals(state);
		}
		
		return true;
	}
	
	protected boolean isParseTypeForPiece(String parseType) {
		return parseType.equals(TURTLESTUBBORN_PARSE_KEY);
	}
	
	protected Piece buildPiece(String type, String state) {
		return new Piece(TURTLE_TYPE_ID, TURTLESTUBBORN_SUB_TYPE, state);
	}

	public static Object rotateState(Object state) {
		if (TURTLESTUBBORN_STATE_LEFT.equals(state)) {
			return TURTLESTUBBORN_STATE_UP;
		} else if (TURTLESTUBBORN_STATE_UP.equals(state)) {
			return TURTLESTUBBORN_STATE_RIGHT;
		} else if (TURTLESTUBBORN_STATE_RIGHT.equals(state)) {
			return TURTLESTUBBORN_STATE_DOWN;
		} else if (TURTLESTUBBORN_STATE_DOWN.equals(state)) {
			return TURTLESTUBBORN_STATE_LEFT;
		} else {
			throw new IllegalArgumentException(String.valueOf(state));
		}
	}
	
	protected SpriteMapper buildSpriteMapper() {
		SpriteMapper map = new SpriteMapper();
		map.addMatch(TURTLE_TYPE_ID, TURTLESTUBBORN_SUB_TYPE, TURTLESTUBBORN_STATE_LEFT, TURTLESTUBBORN_STATE_LEFT, TURTLESTUBBORN_STATE_DOWN);
		map.addMatch(TURTLE_TYPE_ID, TURTLESTUBBORN_SUB_TYPE, TURTLESTUBBORN_STATE_RIGHT, TURTLESTUBBORN_STATE_RIGHT, TURTLESTUBBORN_STATE_UP);
		map.addMatch(TURTLE_TYPE_ID, TURTLESTUBBORN_SUB_TYPE, TURTLESTUBBORN_STATE_UP, TURTLESTUBBORN_STATE_UP, TURTLESTUBBORN_STATE_LEFT);
		map.addMatch(TURTLE_TYPE_ID, TURTLESTUBBORN_SUB_TYPE, TURTLESTUBBORN_STATE_DOWN, TURTLESTUBBORN_STATE_DOWN, TURTLESTUBBORN_STATE_RIGHT);
		return map;
	}

}
