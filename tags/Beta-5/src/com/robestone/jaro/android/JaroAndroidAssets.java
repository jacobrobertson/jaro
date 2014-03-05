package com.robestone.jaro.android;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.content.res.AssetManager;

import com.robestone.jaro.levels.JaroAssets;

/**
 * Simply wraps the assets manager
 * @author Jacob
 */
public class JaroAndroidAssets implements JaroAssets {

	private AssetManager assets;
	// TODO I don't really want to do this, but there are serious performance issues, and I'm just working around this for now
	private Map<String, String[]> cachedPaths = new HashMap<String, String[]>();
	
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
		String[] list = cachedPaths.get(path);
		if (list == null) {
			try {
				list = assets.list(path);
				Arrays.sort(list);
				cachedPaths.put(path, list);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return list;
	}

}
