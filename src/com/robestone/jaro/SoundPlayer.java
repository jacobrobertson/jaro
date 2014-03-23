package com.robestone.jaro;

import com.robestone.jaro.levels.Level;

public interface SoundPlayer {

	void levelPassed(Level level);
	void levelStarted(Level level);
	void levelStopped(Level level);
	
	void action(Level level, PieceRules actionRule, Action action, boolean isIllegalAction);
	
	void onGameHidden();
	void onGameDisplayed();
}
