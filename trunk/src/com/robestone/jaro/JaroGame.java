package com.robestone.jaro;

import com.robestone.jaro.levels.InputStreamBuilder;
import com.robestone.jaro.levels.Level;
import com.robestone.jaro.levels.LevelManager;
import com.robestone.jaro.levels.LevelParser;
import com.robestone.jaro.levels.LevelPersister;

public class JaroGame {

	private JaroView view;
	private JaroModel model;
	private JaroController controller;
	
	public JaroGame(JaroModel model, JaroView view, JaroController controller, 
			InputStreamBuilder inputStreamBuilder, LevelPersister levelPersister) {
		this.model = model;
		this.view = view;
		this.controller = controller;
		view.setController(controller);
		view.setModel(model);
		controller.setView(view);
		controller.setModel(model);
		
		LevelParser parser = new LevelParser(controller.getPieceRules());
		LevelManager levelManager = new LevelManager(inputStreamBuilder, parser, levelPersister);
		model.setLevelManager(levelManager);
		
		Level currentLevel = levelManager.getCurrentLevel();
		model.setLevel(currentLevel);
	}
	public JaroView getView() {
		return view;
	}
	public JaroModel getModel() {
		return model;
	}
	public JaroController getController() {
		return controller;
	}
}
