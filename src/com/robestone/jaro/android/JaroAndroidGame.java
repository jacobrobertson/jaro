package com.robestone.jaro.android;

import com.robestone.jaro.JaroController;
import com.robestone.jaro.JaroGame;
import com.robestone.jaro.JaroModel;

public class JaroAndroidGame extends JaroGame {

	private JaroResources jaroResources;
	
	public JaroAndroidGame(JaroActivity activity, JaroResources resources) {
		super(new JaroModel(), new JaroAndroidView(activity),
				new JaroController(), resources, new AndroidLevelPersister(activity));
		this.jaroResources = resources;
	}
	public JaroResources getJaroResources() {
		return jaroResources;
	}
	public JaroAndroidView getView() {
		return (JaroAndroidView) super.getView();
	}
}
