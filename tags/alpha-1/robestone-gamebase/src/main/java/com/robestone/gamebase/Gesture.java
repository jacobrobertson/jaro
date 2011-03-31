package com.robestone.gamebase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Gesture {

	private List<GesturePart> gestureParts;

	public Gesture(GesturePart... gestureParts) {
		this(new ArrayList<GesturePart>(Arrays.asList(gestureParts)));
	}
	public Gesture(List<GesturePart> gestureParts) {
		this.gestureParts = gestureParts;
	}

	public List<GesturePart> getGestureParts() {
		return gestureParts;
	}
	
	public void add(Gesture gesture) {
		this.gestureParts.addAll(gesture.gestureParts);
	}
	
}
