package com.robestone.jaro.piecerules;

import com.robestone.jaro.Grid;
import com.robestone.jaro.JaroModel;
import com.robestone.jaro.Piece;

public class SnakeRules extends PieceRulesAdapter {

	public static final String SNAKE_TYPE_ID = "snake";
	
	public SnakeRules() {
		super(SNAKE_TYPE_ID);
	}
	@Override
	public boolean isOkayToEndLevel(JaroModel model) {
		Grid grid = model.getGrid();
		int cols = grid.getColumns();
		int rows = grid.getRows();
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				Piece snake = grid.getPieceByType(i, j, SNAKE_TYPE_ID);
				if (snake != null) {
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
