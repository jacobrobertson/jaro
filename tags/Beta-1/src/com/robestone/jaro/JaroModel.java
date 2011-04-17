package com.robestone.jaro;

import java.util.LinkedList;

import com.robestone.jaro.levels.Level;
import com.robestone.jaro.levels.LevelManager;
import com.robestone.jaro.piecerules.JaroRules;

public class JaroModel {

	private int maxMovesToSave = 250;
	
	private LinkedList<Grid> grids;
	private LevelManager levelManager;
	
	private int jaroX;
	private int jaroY;
	
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
		Grid grid = levelManager.getGrid(level);
		setLevelGrid(grid);	
	}
    public Level findLevel(int levelIndex) {
    	return levelManager.findLevel(levelIndex);
    }
	public Piece getJaro() {
		return getGrid().getPiece(jaroX, jaroY);
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
		int w = getGrid().getColumns();
		int h = getGrid().getRows();
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Piece p = getGrid().getPiece(i, j);
				if (p != null && JaroRules.JARO_TYPE_ID.equals(p.getType())) {
					this.jaroX = i;
					this.jaroY = j;
					return;
				}
			}
		}
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
