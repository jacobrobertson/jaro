package com.robestone.jaro.piecerules;

import com.robestone.jaro.Grid;
import com.robestone.jaro.JaroModel;
import com.robestone.jaro.Piece;

public class TurtleNestRules extends PieceRulesAdapter {

	public static final String TURTLE_NEST_ID = "turtlenest";
	
	public TurtleNestRules() {
		super(TURTLE_NEST_ID);
	}
	@Override
	public boolean isOkayToEndLevel(JaroModel model) {
		Grid grid = model.getGrid();
		int cols = grid.getColumns();
		int rows = grid.getRows();
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				Piece nest = grid.getPieceByType(i, j, TURTLE_NEST_ID);
				if (nest != null) {
					Piece turtle = grid.getPieceByType(i, j, TurtleRules.TURTLE_TYPE_ID);
					if (turtle == null) {
						return false;
					}
				}
			}
		}
		return true;
	}
}
