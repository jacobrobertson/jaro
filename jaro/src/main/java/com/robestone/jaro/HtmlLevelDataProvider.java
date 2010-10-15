package com.robestone.jaro;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlLevelDataProvider implements LevelDataProvider {

	private List<LevelData> data = new ArrayList<LevelData>();
	private Pattern filePattern = Pattern.compile("<table class=\"level \\d+\">(.*?)</table>");
	private Pattern linePattern = Pattern.compile("<td>(.*?)</td>");
	private Pattern imgPattern = Pattern.compile("/drawable/(.*?)\\.png");
	
	public void load(InputStream in) {
		load(getHtmlData(in));
	}
	private static String getHtmlData(InputStream in) {
		StringBuilder buf = new StringBuilder();
		int r = 0;
		try {
			while ((r = in.read()) >= 0) {
				buf.append((char) r);
			}
		} catch (IOException e) {
			return null;
		}
		String s = buf.toString();
		s = s.replaceAll("<td/>", "<td></td>");
		return s;
	}
	public HtmlLevelDataProvider() {
	}
	public void load(String htmlData) {
		data.addAll(parse(htmlData));
	}
	
	public List<LevelData> parse(String htmlData) {
		List<LevelData> data = new ArrayList<LevelData>();
		
		htmlData = htmlData.replaceAll("[\\n\\r]", "");
		Matcher m = filePattern.matcher(htmlData);
		while (m.find()) {
			LevelData d = parseOne(m.group(1));
			data.add(d);
		}
		
		return data;
	}
	
	public LevelData parseOne(String htmlData) {
		int cols = getCols(htmlData);
		int totalCells = 0;
		StringBuilder buf = new StringBuilder();
		
		Matcher m = linePattern.matcher(htmlData);
		while (m.find()) {
			String img = m.group(1);
			Matcher im = imgPattern.matcher(img);
			// this is matching images in the TD
			// if there is no match, it means no image, but there's still a cell there
			if (im.find()) {
				String id = im.group(1);
				id = cleanKey(id);
				buf.append("|");
				buf.append(id);
			} else {
				buf.append("| ");
			}
			totalCells++;
		}
		int rows = totalCells / cols;
		return new LevelData(buf.toString(), cols, rows);
	}
	private String cleanKey(String k) {
		char c = k.charAt(k.length() - 1);
		if (Character.isDigit(c)) {
			k = k.substring(0, k.length() - 1);
		}
		return k;
	}
	private int getCols(String htmlData) {
		int cols = 0;
		int pos = htmlData.indexOf("<tr>");
		int pos2 = htmlData.indexOf("</tr>");
		String sub = htmlData.substring(pos, pos2);
		while (pos >= 0) {
			pos = sub.indexOf("<td>", pos + 1);
			cols++;
		}
		return cols - 1;
	}
	
	@Override
	public int getLastLevelIndex() {
		return data.size() - 1;
	}

	@Override
	public LevelData getLevelData(int levelIndex) {
		return data.get(levelIndex);
	}
}
