package com.robestone.jaro;

import com.robestone.jaro.levels.Level;

public class SoundPlayerMock implements SoundPlayer {

	public static final SoundPlayerMock INSTANCE = new SoundPlayerMock();
	
	@Override
	public void levelPassed(Level level) {
		// TODO Auto-generated method stub

	}

	@Override
	public void levelStarted(Level level) {
		// TODO Auto-generated method stub

	}

	@Override
	public void levelStopped(Level level) {
		// TODO Auto-generated method stub

	}

	@Override
	public void action(Level level, PieceRules actionRule, Action action,
			boolean isIllegalAction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameDisplayed() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onGameHidden() {
		// TODO Auto-generated method stub
		
	}
}
