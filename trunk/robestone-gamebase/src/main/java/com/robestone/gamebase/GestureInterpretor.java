package com.robestone.gamebase;

public interface GestureInterpretor {

	GestureInterpretorResult getActionForGesture(Gesture gesture, Game game);
	
}
