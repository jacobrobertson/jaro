package com.robestone.jaro.levels;

import java.io.InputStream;

public interface InputStreamBuilder {

	InputStream buildStagesInputStream();
	InputStream buildLevelInputStream(String levelKey);
	
}
