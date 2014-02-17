package com.robestone.jaro.android;

import com.robestone.jaro.JaroController;
import com.robestone.jaro.JaroGame;
import com.robestone.jaro.JaroModel;

public class JaroAndroidGame extends JaroGame {

	private JaroAndroidResources jaroResources;
	private boolean active;
	
	public JaroAndroidGame(JaroActivity activity, JaroAndroidResources resources) {
		super(new JaroModel(resources), new JaroAndroidView(activity),
				new JaroController(), new JaroPreferences(activity), resources);
		this.jaroResources = resources;
	}
	public JaroAndroidResources getJaroResources() {
		return jaroResources;
	}
	public JaroAndroidView getView() {
		return (JaroAndroidView) super.getView();
	}
	public boolean isActive() {
		return active;
	}
	public void setActive() {
		this.active = true;
	}
	
}
