package com.robestone.jaro;

import com.robestone.gamebase.Action;
import com.robestone.gamebase.Area;
import com.robestone.gamebase.Game;
import com.robestone.gamebase.Gesture;
import com.robestone.gamebase.GestureInterpretor;
import com.robestone.gamebase.GestureInterpretorResult;
import com.robestone.gamebase.GesturePart;
import com.robestone.gamebase.Grid;
import com.robestone.gamebase.Mover;
import com.robestone.gamebase.Piece;

/**
 * Default impl - pretty simple for now, based on very few assumptions.
 * @author jacob
 */
public class BasicGestureInterpretor implements GestureInterpretor {

	 @Override
	public GestureInterpretorResult getActionForGesture(Gesture gesture, Game game) {
		GesturePart part = gesture.getGestureParts().get(0);
		Action action = null;
		
		JaroGame jg = (JaroGame) game;
		Grid grid = jg.getGrid();
		Piece jaro = jg.getJaroPiece();
		
		boolean isDirection = isDirectionButton(part);
		if (isDirection) {
			Object key = part.getPiece().getType();
			int xd = 0;
			int yd = 0;
			if (key.equals(JaroConstants.up)) {
				yd = -1;
			} else if (key.equals(JaroConstants.down)) {
				yd = 1;
			} else if (key.equals(JaroConstants.left)) {
				xd = -1;
			} else if (key.equals(JaroConstants.right)) {
				xd = 1;
			}
			Area jaroArea = grid.getAreaContaining(jaro);
			int x = grid.getColumn(jaroArea) + xd;
			int y = grid.getRow(jaroArea) + yd;
			
			if (x < grid.getColumns() && x >= 0 && y < grid.getRows() && y >= 0) {
				Area moveTo = grid.getArea(x, y);
				action = Mover.moveOne(jaroArea, moveTo);
			}
		} else {
			Area moveTo = part.getArea();
			
			// is this a click on a spot next to the acting piece?
			Area moveFrom = grid.getAreaContaining(jaro);
			int xd = Math.abs(grid.getColumn(moveTo) - grid.getColumn(moveFrom));
			int yd = Math.abs(grid.getRow(moveTo) - grid.getRow(moveFrom));
			
			if (xd + yd == 1) {
				action = Mover.moveOne(moveFrom, moveTo);
			}
		}
			
		GestureInterpretorResult result = new GestureInterpretorResult(action, true);
		return result;
	}
	private boolean isDirectionButton(GesturePart part) {
		if (part.getPiece() == null) {
			return false;
		}
		return
			part.getPiece().getType().toString().startsWith(JaroConstants.directionButton_prefix);
	}

}
