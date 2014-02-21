package com.robestone.jaro;

import java.util.LinkedList;

import com.robestone.jaro.levels.JaroResources;
import com.robestone.jaro.levels.Level;
import com.robestone.jaro.levels.LevelManager;
import com.robestone.jaro.piecerules.JaroRules;

public class JaroModel {

	private int maxMovesToSave = 250;
	
	private LinkedList<Grid> grids;
	private LevelManager levelManager;
	private JaroResources jaroResources;
	
	private int jaroX = -1;
	private int jaroY = -1;
	
	public JaroModel(JaroResources jaroResources) {
		this.jaroResources = jaroResources;
	}
	
	public Grid getGrid() {
		return grids.getLast();
	}
	public void setLevelGrid(Grid grid) {
		this.grids = new LinkedList<Grid>();
		this.grids.add(grid);
		saveJaroPosition();
	}
	public void passedLevel() {
		Level level = levelManager.passCurrentLevel();
		setLevel(level);
	}
	public void setLevel(String levelKey) {
		Level level = levelManager.getLevel(levelKey);
		setLevel(level);
	}
	public void setLevel(Level level) {
		levelManager.setCurrentLevel(level);
		Grid grid = jaroResources.getGrid(level);
		setLevelGrid(grid);	
	}
	public Piece getJaro() {
		// if we are at the initial creation, we don't expect jaro there yet
		if (jaroX < 0) {
			return null;
		}
		Piece jaro = getGrid().getPieceByType(jaroX, jaroY, JaroRules.JARO_TYPE_ID);
		if (jaro == null) {
			/// ---- TODO REMOVE
			/*
			throw new IllegalStateException("Could not find Jaro @ (" + jaroX + "," + jaroY + "):" + String.valueOf(getGrid()));
			*/
			/// ----
		}
		return jaro;
	}
	public int getJaroColumn() {
		return jaroX;
	}
	public int getJaroRow() {
		return jaroY;
	}
	
	/**
	 * Just switch back to the previous grid if possible.
	 */
	public void undo() {
		if (grids.size() > 1) {
			grids.removeLast();
			saveJaroPosition();
		}
	}
	public void rollbackAllMoves() {
		while (grids.size() > 1) {
			grids.removeLast();
		}
		saveJaroPosition();
	}
	public void saveJaroPosition() {
		Grid grid = getGrid();
		
		int cols = grid.getColumns();
		int rows = grid.getRows();
		for (int x = 0; x < cols; x++) {
			for (int y = 0; y < rows; y++) {
				Piece p = grid.getPieceByType(x, y, JaroRules.JARO_TYPE_ID);
				if (p != null) {
					this.jaroX = x;
					this.jaroY = y;
					return;
				}
			}
		}
		throw new IllegalArgumentException("Could not find Jaro:" + grid.toString());
	}
	/**
	 * Creates a new grid for the undo chain.
	 */
	public void cloneCurrent() {
		// remove a lot, but always leave the first
		Grid first = grids.removeFirst();
		while (grids.size() > maxMovesToSave) {
			// remove the second
			grids.removeFirst();
		}
		grids.addFirst(first);
		
		// copy the last
		Grid current = getGrid();
		Grid saved = current.makeHistoricalCopy();
		grids.add(saved);
	}
	public LevelManager getLevelManager() {
		return levelManager;
	}
	public void setLevelManager(LevelManager levelManager) {
		this.levelManager = levelManager;
	}
	
}
