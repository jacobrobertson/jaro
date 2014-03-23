package com.robestone.jaro;

import java.util.List;

import com.robestone.jaro.levels.Level;
import com.robestone.jaro.piecerules.JaroPieceRules;

public class JaroController {
	
	private JaroView view;
	private SoundPlayer soundPlayer;
	private JaroModel model;
	
	private List<PieceRules> pieceRules = JaroPieceRules.getPieceRules();
	private boolean isAcceptingMoves;

	public JaroController(SoundPlayer soundPlayer) {
		this.soundPlayer = soundPlayer;
	}
	
	/**
	 * Called by the view to indicate that any intermediate menus, etc are ready to go.
	 */
	public void startAcceptingMoves() {
		this.isAcceptingMoves = true;
		// TODO this is not at all right, but I don't have an API for when to start/stop the sound
		soundPlayer.levelStarted(null);
	}
	public void undoLastMove() {
		model.undo();
	}
	public void startLevelOver() {
		model.rollbackAllMoves();
	}
	public boolean move(Direction direction) {
		// bottleneck right here so we only process one move at a time
		synchronized (this) {
			if (!isAcceptingMoves) {
				return false;
			}
		}
		int toX = model.getJaroColumn() + direction.getXDirection();
		int toY = model.getJaroRow() + direction.getYDireection();
		
		Action action = new Action(model.getJaro(), model.getJaroColumn(), model.getJaroRow(), toX, toY);
		
		Level level = model.getLevelManager().getCurrentLevel();
		boolean isLegal = isLegalForGrid(action);
		if (!isLegal) {
			// TODO put a sound here - but this should probably never come up - cave walls is what prevents us from going out of grid
			return false;
		}
		
		// check legality
		for (PieceRules rules: pieceRules) {
			isLegal = rules.isLegal(action, model);
			if (!isLegal) {
				soundPlayer.action(level, rules, action, true);
				return false;
			}
		}
		
		// before running all the actions, save the state (for undos)
		model.cloneCurrent();

		// perform the user action
		action.run(model);
		model.saveJaroPosition(); // TODO I'm not thrilled by this way of keeping track of jaro...
		
		// get the "triggered" actions from the rules
		boolean rulesFound = true;
		while (rulesFound) {
			rulesFound = false;
			for (PieceRules rules: pieceRules) {
				List<Action> triggeredActions = rules.getTriggeredActions(action, model);
				if (triggeredActions != null) {
					for (Action triggeredAction: triggeredActions) {
						triggeredAction.run(model);
						model.saveJaroPosition();
						soundPlayer.action(level, rules, triggeredAction, false);
						rulesFound = true;
					}
				}
			}
		}

		boolean levelEnded = true;
		for (PieceRules rules: pieceRules) {
			if (!rules.isOkayToEndLevel(model)) {
				levelEnded = false;
				break;
			}
		}
		if (levelEnded) {
			isAcceptingMoves = false;
			// inform the other layers
			Level currentLevel = model.getLevelManager().getCurrentLevel();
			soundPlayer.levelPassed(currentLevel);
			view.passedLevel();
			model.passedLevel();
		}
		
		return true;
	}
	/**
	 * Checks grid boundaries.
	 */
	private boolean isLegalForGrid(Action action) {
		int x = action.getToX();
		int y = action.getToY();
		if (x < 0 || y < 0) {
			return false;
		}
		int maxX = model.getGrid().getColumns();
		if (x >= maxX) {
			return false;
		}
		int maxY = model.getGrid().getRows();
		if (y >= maxY) {
			return false;
		}
		return true;
	}
	
	/**
	 * Called by view when a level is chosen from a menu.
	 */
	public void selectLevel(Level level) {
		model.setLevel(level);
		view.levelSelected();
		soundPlayer.levelPassed(level);
	}

	public void setView(JaroView view) {
		this.view = view;
	}
	public void setModel(JaroModel model) {
		this.model = model;
	}
	
	/**
	 * Made public because the rules encompass some rendering information as well.
	 */
	public List<PieceRules> getPieceRules() {
		return pieceRules;
	}
	public void onGameHidden() {
		soundPlayer.onGameHidden();
	}
	public void onGameDisplayed() {
		soundPlayer.onGameDisplayed();
	}
}
