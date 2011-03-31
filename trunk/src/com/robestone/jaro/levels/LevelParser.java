package com.robestone.jaro.levels;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.robestone.jaro.Grid;
import com.robestone.jaro.Piece;
import com.robestone.jaro.PieceRules;

public class LevelParser {

	private Pattern linePattern = Pattern.compile("<td>(.*?)</td>");
	private Pattern imgPattern = Pattern.compile("/drawable/(.*?)\\.png");
	private Pattern filePattern = Pattern.compile("<table class=\"level.*?\">(.*?)</table>");
	
	private List<PieceRules> pieceRules;
	
	public LevelParser(List<PieceRules> pieceRules) {
		this.pieceRules = pieceRules;
	}

	public Grid parseGrid(InputStream in) {
		String html = toHtml(in);
		Grid grid = parseGrid(html);
		return grid;
	}
	
	private String toHtml(InputStream in) {
		String s = toString(in);
		s = s.replaceAll("[\\n\\r]", "");
		Matcher m = filePattern.matcher(s);
		if (m.find()) {
			s = m.group(1);
		}
		s = s.replaceAll("<td/>", "<td></td>");
		s = s.replaceAll("<td />", "<td></td>");
		return s;
	}
	
	private Grid parseGrid(String htmlData) {
		int cols = getCols(htmlData);
		Matcher m = linePattern.matcher(htmlData);
		List<List<Piece>> list = new ArrayList<List<Piece>>();
		while (m.find()) {
			String img = m.group(1);
			Matcher im = imgPattern.matcher(img);
			// this is matching images in the TD
			// if there is no match, it means no image, but there's still a cell there
			List<Piece> pieces = new ArrayList<Piece>();
			while (im.find()) {
				String id = im.group(1);
				id = cleanKey(id);
				// add piece with correct id and state
				Piece p = parsePiece(id);
				pieces.add(p);
			}
			list.add(pieces);
		}
		
		int rows = list.size() / cols;
		
		Grid grid = new Grid(cols, rows);

		int index = 0;
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < cols; x++) {
				List<Piece> next = list.get(index);
				for (Piece p: next) {
					grid.addPiece(p, x, y);
				}
				index++;
			}
		}
		grid.initIds();
		
		return grid;
	}
	private Piece parsePiece(String parseKey) {
		for (PieceRules rules: pieceRules) {
			Piece p = rules.parsePiece(parseKey);
			if (p != null) {
				return p;
			}
		}
		throw new IllegalArgumentException("Cannot parse piece: " + parseKey);
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
	/**
	 * We assume that stages are defined in a file.  This is because
	 * we want to promote a kind of "drop-in" mentality for adding levels
	 * and stages, and reordering them.  This will be a lot easier if it
	 * requires no programming.  
	 * @author jacob
	 */
	public List<Stage> parseStages(InputStream in) {
		String s = toString(in);
		String[] lines = s.split("[\n\r]");
		List<Stage> stages = new ArrayList<Stage>();
		Stage currentStage = null;
		for (String line: lines) {
			if (line.trim().length() == 0) {
				continue;
			}
			if (!line.startsWith(" ")) {
				line = line.trim();
				String[] keys = line.split(":");
				currentStage = new Stage(keys[0].trim(), keys[1].trim(), new ArrayList<Level>());
				stages.add(currentStage);
			} else {
				line = line.trim();
				Level level = new Level(line, currentStage.getStageKey());
				currentStage.getLevels().add(level);
			}
		}
		return stages;
	}
	private String toString(InputStream in) {
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
		return s;
	}

}
