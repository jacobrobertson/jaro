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
	public static String toCleanName(String name) {
		name = toCleanName(name, ".");
		name = toCleanName(name, "?");
		name = toCleanName(name, ":");
		name = toCleanName(name, "\\");
		name = toCleanName(name, "/");
		name = toCleanName(name, ">");
		name = toCleanName(name, "<");
		return name.trim();
	}
	public static String toCleanName(String name, String reg) {
		char c = reg.charAt(reg.length() - 1);
		int i = (int) c;
		return name.replace(reg, "[" + i + "]");
	}
	public static String parseName(String name) {
		StringBuilder buf = new StringBuilder();
		int num = 0;
		boolean counting = false;
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (counting) {
				if (c == ']') {
					buf.append((char) num);
					counting = false;
					num = 0;
				} else {
					num = num * 10;
					num = num + ((int) c - ('0'));
				}
			} else if (c == '[') {
				counting = true;
			} else if (c == '_') {
				buf.append(' ');
			} else {
				buf.append(c);
			}
		}
		return buf.toString();
	}

}
