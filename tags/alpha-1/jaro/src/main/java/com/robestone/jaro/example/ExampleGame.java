package com.robestone.jaro.example;

import java.util.List;

import com.robestone.gamebase.Area;
import com.robestone.gamebase.GameController;
import com.robestone.gamebase.Gesture;
import com.robestone.gamebase.GesturePart;
import com.robestone.gamebase.Grid;
import com.robestone.gamebase.GridImpl;
import com.robestone.gamebase.PieceImpl;
import com.robestone.jaro.JaroConstants;
import com.robestone.jaro.JaroGame;
import com.robestone.jaro.JaroGameBuilder;
import com.robestone.jaro.JaroGameState;
import com.robestone.jaro.LevelData;

public class ExampleGame extends JaroGame {

//	private static Piece jaro = new PieceImpl(0, JaroConstants.jaroId);
	
	private GameController controller;
	private JaroGameBuilder builder;

	public ExampleGame(LevelData levelData, boolean landscape) {
		super(new PieceImpl(0, JaroConstants.jaroId));
		// create game state
		List<Area> areas = JaroGameBuilder.buildAreas();
		
		int cols;
		int rows;
		if (landscape) {
			cols = levelData.getRows();
			rows = levelData.getColumns();
		} else {
			cols = levelData.getColumns();
			rows = levelData.getRows();
		}
		grid = new GridImpl(cols, rows, areas, JaroConstants.square);

		state = new JaroGameState();
		builder = new JaroGameBuilder(grid, getJaroPiece(), state);

		builder.addFromData(levelData.getData(), landscape);
		
		controller = builder.buildGameController();
		controller.setGame(this);
	}
	
	
	public GameController getController() {
		return controller;
	}

	public Grid getGrid() {
		return grid;
	}
	public JaroGameState getGameState() {
		return state;
	}
	static Gesture newGesture(Grid grid, int x, int y) {
		Area area = grid.getArea(x, y);
		GesturePart part = new GesturePart(null, area);
		Gesture gesture = new Gesture(part);
		return gesture;
	}
	Gesture newGesture(int x, int y) {
		return newGesture(grid, x, y);
	}

}
