package com.robestone.jaro.piecerules;

import java.util.ArrayList;
import java.util.List;

import com.robestone.jaro.Action;
import com.robestone.jaro.Grid;
import com.robestone.jaro.JaroModel;
import com.robestone.jaro.Piece;

public class TurtleHoleRules extends PieceRulesAdapter {

	public static final String TURTLE_HOLE_TYPE_ID = "turtlehole";
	public static final String TURTLE_HOLE_WITH_TURTLE_STATE = "turtlehole_with_turtle";
	public static final String TURTLE_HOLE_EMPTY_STATE = "turtlehole_empty";
	
	public TurtleHoleRules() {
		super(TURTLE_HOLE_TYPE_ID);
		setBlockingStateId(TURTLE_HOLE_EMPTY_STATE);
		setUseStateForSpriteId(true);
	}
	@Override
	public List<Action> getTriggeredActions(Action userAction, JaroModel model) {
		// look for each hole, and if there's a turtle on it, get rid of the turtle and change the hole state
		List<Action> actions = new ArrayList<Action>();
		Grid grid = model.getGrid();
		int cols = grid.getColumns();
		int rows = grid.getRows();
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				List<Piece> pieces = grid.getPieces(i, j);
				if (pieces != null && pieces.size() == 2) {
					Piece maybeTurtle = pieces.get(0);
					Piece maybeHole = pieces.get(1);
					if (TurtleRules.TURTLE_TYPE_ID.equals(maybeTurtle.getType()) 
							&& isEmptyHole(maybeHole)) {
						actions.add(new Action(maybeHole, TURTLE_HOLE_WITH_TURTLE_STATE));
						actions.add(new Action(maybeTurtle, i, j, true));
					}
				}
			}
		}
		return actions;
	}
	public static boolean isEmptyHole(Piece p) {
		return TURTLE_HOLE_TYPE_ID.equals(p.getType()) &&
				TURTLE_HOLE_EMPTY_STATE.equals(p.getState());
	}
}
