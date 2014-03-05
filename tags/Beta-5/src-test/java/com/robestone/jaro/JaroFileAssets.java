package com.robestone.jaro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;

import com.robestone.jaro.levels.JaroAssets;

public class JaroFileAssets implements JaroAssets {

	private File baseDir;
	
	public JaroFileAssets(String baseDir) {
		this.baseDir = new File(baseDir);
	}

	@Override
	public String[] list(String path) {
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return !name.equals(".svn");
			}
		};
		return new File(baseDir, path).list(filter);
	}

	@Override
	public InputStream open(String path) {
		try {
			return new FileInputStream(new File(baseDir, path));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
