package com.robestone.jaro.android;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;

import com.robestone.jaro.levels.JaroAssets;

/**
 * Simply wraps the assets manager
 * @author Jacob
 */
public class JaroAndroidAssets implements JaroAssets {

	private AssetManager assets;
	
	public JaroAndroidAssets(AssetManager assets) {
		this.assets = assets;
	}

	@Override
	public InputStream open(String path) {
		try {
			return assets.open(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String[] list(String path) {
		try {
			return assets.list(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
