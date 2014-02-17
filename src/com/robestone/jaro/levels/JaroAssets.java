package com.robestone.jaro.levels;

import java.io.InputStream;

/**
 * This interface is necessary to perform simple unit testing without an android emulator.
 * @author Jacob
 */
public interface JaroAssets {

	InputStream open(String path);
	String[] list(String path);
	
}
