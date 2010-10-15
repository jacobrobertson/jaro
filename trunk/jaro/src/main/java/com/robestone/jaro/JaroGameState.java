package com.robestone.jaro;

import com.robestone.gamebase.Area;
import com.robestone.gamebase.GameState;

public class JaroGameState extends GameState {

	private Area previousJaroPosition;

	public Area getPreviousJaroPosition() {
		return previousJaroPosition;
	}

	public void setPreviousJaroPosition(Area previousJaroPosition) {
		this.previousJaroPosition = previousJaroPosition;
	}
	
}
