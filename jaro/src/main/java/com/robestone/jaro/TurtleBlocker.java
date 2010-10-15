package com.robestone.jaro;

import com.robestone.gamebase.Action;
import com.robestone.gamebase.ActionRules;
import com.robestone.gamebase.Area;
import com.robestone.gamebase.Game;
import com.robestone.gamebase.Grid;
import com.robestone.gamebase.Mover;
import com.robestone.gamebase.Piece;

public class TurtleBlocker implements ActionRules {

	@Override
	public boolean isLegal(Action action, Game game) {
		if (!(action instanceof Mover)) {
			return true;
		}
		Mover mover = (Mover) action;
		
		// see if we're trying to move onto a turtle
		Area to = mover.getTo();
		if (to.isEmpty()) {
			return true;
		}
		Piece turtle = to.get(0);
		if (!JaroConstants.turtle.equals(turtle.getType())) {
			return true;
		}
		
		// check the grid and see if we can move the turtle over or not
		Grid grid = ((JaroGame) game).getGrid();
		Area from = mover.getFrom();
		Area otherSide = getOtherSide(from, to, grid);
		
		// if there's nothing there, we can't push it
		if (otherSide == null) {
			return false;
		}
		
		// allow when nothing there
		if (otherSide.isEmpty()) {
			return true;
		}
		
		// otherwise, only if other side is an emtpy hole
		Piece top = otherSide.get(0);
		return JaroConstants.hole.equals(top.getType())
			&& JaroConstants.hole_empty.equals(top.getState());
		
	}
	public static Area getOtherSide(Area jaro, Area turtle, Grid grid) {
		int tx = grid.getColumn(turtle);
		int ty = grid.getRow(turtle);
		
		int xd = tx - grid.getColumn(jaro);
		int yd = ty - grid.getRow(jaro);
		
		// what is on the other side of the turtle?
		int x2 = tx + xd;
		int y2 = ty + yd;
		Area otherSide = grid.getArea(x2, y2);
		return otherSide;
	}

}
