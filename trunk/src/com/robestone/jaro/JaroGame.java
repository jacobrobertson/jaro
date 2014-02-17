package com.robestone.jaro;

import com.robestone.jaro.levels.JaroResources;
import com.robestone.jaro.levels.Level;
import com.robestone.jaro.levels.LevelManager;
import com.robestone.jaro.levels.LevelPersister;

public class JaroGame {

	private JaroView view;
	private JaroModel model;
	private JaroController controller;
	
	public JaroGame(JaroModel model, JaroView view, JaroController controller, LevelPersister levelPersister, JaroResources jaroResources) {
		this.model = model;
		this.view = view;
		this.controller = controller;
		view.setController(controller);
		view.setModel(model);
		controller.setView(view);
		controller.setModel(model);
		
		LevelManager levelManager = new LevelManager(levelPersister, jaroResources);
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
