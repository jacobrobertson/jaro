package com.robestone.jaro.piecerules;

import java.util.List;

import com.robestone.jaro.Action;
import com.robestone.jaro.Grid;
import com.robestone.jaro.JaroModel;
import com.robestone.jaro.Piece;
import com.robestone.jaro.SpriteMapper;

public class TurtleRules extends PieceRulesAdapter {

	public static final String TURTLE_TYPE_ID = "turtle";
	
	private static final String TURTLE_STATE_LEFT = "turtle_left";
	private static final String TURTLE_STATE_RIGHT = "turtle_right";
	private static final String TURTLE_STATE_UP = "turtle_up";
	private static final String TURTLE_STATE_DOWN = "turtle_down";

	
	private SpriteMapper map;
	
	public TurtleRules() {
		super(TURTLE_TYPE_ID);
		setUseStateForSpriteId(true);
		map = buildSpriteMapper();
	}
	@Override
	protected String doGetSpriteId(Piece piece, boolean isLandscape) {
		return map.getSpriteId(piece, isLandscape);
	}
	public List<Action> getTriggeredActions(Action userAction, JaroModel model) {
		// is jaro standing on a turtle?
		Piece turtle = getJaroStandingOn(model, TURTLE_TYPE_ID);
		if (turtle == null) {
			return null;
		}
		// if it was legal, then we assume we're gonna push it
		Action action = getAction(turtle, userAction);
		return action.toList();
	}
	protected boolean isActionForTurtleMove(Action proposedAction, JaroModel model) {
		// we only care about pushing
		if (!proposedAction.isMovePiece()) {
			return false;
		}
		
		int toX = proposedAction.getToX();
		int toY = proposedAction.getToY();
		
		Grid grid = model.getGrid();
		// see if we're trying to move onto a turtle
		Piece maybeTurtle = grid.getPiece(toX, toY);
		if (maybeTurtle == null) {
			return false;
		}
		if (!TURTLE_TYPE_ID.equals(maybeTurtle.getType())) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean isLegal(Action proposedAction, JaroModel model) {
		if (!isActionForTurtleMove(proposedAction, model)) {
			return true;
		}
		
		// are we trying to push it off the screen?
		// --- we don't care - we'll always have cave walls there
		
		// check the grid and see if we can move the turtle over or not
		Grid grid = model.getGrid();
		Piece otherSide = getOtherSide(proposedAction, grid);
		
		// if there's nothing there, we can push it
		if (otherSide == null) {
			return true;
		}
		
		// we know there's something there, so it's only legal if other side is a hole
		boolean isTurtleHole = TurtleHoleRules.TURTLE_HOLE_TYPE_ID.equals(otherSide.getType());
		if (isTurtleHole) {
			return true;
		}
		boolean isTurtleNest = TurtleNestRules.TURTLE_NEST_ID.equals(otherSide.getType());
		if (isTurtleNest) {
			return true;
		}
		
		return false;
	}
	private Piece getOtherSide(Action userAction, Grid grid) {
		int turtleX = userAction.getToX();
		int turtleY = userAction.getToY();
		int jaroPreviousX = userAction.getFromX();
		int jaroPreviousY = userAction.getFromY();
		int xd = turtleX - jaroPreviousX;
		int yd = turtleY - jaroPreviousY;
		
		// what is on the other side of the turtle?
		int x2 = turtleX + xd;
		int y2 = turtleY + yd;
		Piece otherSide = grid.getPiece(x2, y2);
		return otherSide;
	}
	private Action getAction(Piece turtle, Action userAction) {
		int turtleX = userAction.getToX();
		int turtleY = userAction.getToY();
		int jaroPreviousX = userAction.getFromX();
		int jaroPreviousY = userAction.getFromY();
		int xd = turtleX - jaroPreviousX;
		int yd = turtleY - jaroPreviousY;
		
		// what is on the other side of the turtle?
		int x2 = turtleX + xd;
		int y2 = turtleY + yd;
		return new Action(turtle, turtleX, turtleY, x2, y2);
	}
	protected SpriteMapper buildSpriteMapper() {
		SpriteMapper map = new SpriteMapper();
		map.addMatch(TURTLE_TYPE_ID, null, TURTLE_STATE_LEFT, TURTLE_STATE_LEFT, TURTLE_STATE_DOWN);
		map.addMatch(TURTLE_TYPE_ID, null, TURTLE_STATE_RIGHT, TURTLE_STATE_RIGHT, TURTLE_STATE_UP);
		map.addMatch(TURTLE_TYPE_ID, null, TURTLE_STATE_UP, TURTLE_STATE_UP, TURTLE_STATE_LEFT);
		map.addMatch(TURTLE_TYPE_ID, null, TURTLE_STATE_DOWN, TURTLE_STATE_DOWN, TURTLE_STATE_RIGHT);
		return map;
	}

}
