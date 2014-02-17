package com.robestone.jaro.levels;

import java.io.IOException;
import java.io.InputStream;

public class Utils {

	public static String toString(InputStream in) {
		StringBuilder buf = new StringBuilder();
		int r = 0;
		try {
			while ((r = in.read()) >= 0) {
				buf.append((char) r);
			}
			in.close();
		} catch (IOException e) {
			return null;
		}
		String s = buf.toString();
		return s;
	}

}
