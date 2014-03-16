package com.robestone.jaro.android;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.robestone.jaro.Grid;
import com.robestone.jaro.Piece;
import com.robestone.jaro.levels.JaroAssets;
import com.robestone.jaro.levels.Level;
import com.robestone.jaro.levels.LevelPersister;
import com.robestone.jaro.levels.Stage;
import com.robestone.jaro.levels.Utils;
import com.robestone.jaro.piecerules.JaroPieceRules;

public class HtmlResources extends JaroAndroidResources {

	public static final String JARO_ASSETS_DIR = "stage-data/" + JARO_GAME_TYPE;
	public static final String DATA_TYPE = "html";

	private Pattern linePattern = Pattern.compile("<td>(.*?)</td>");
	private Pattern imgPattern = Pattern.compile("/drawable.*?/(.*?)\\.png");
	private Pattern filePattern = Pattern.compile("<table class=\"level.*?\">(.*?)</table>");

	private JaroAssets assets;
	
	public HtmlResources(JaroAssets assets, LevelPersister levelPersister) {
		super(levelPersister);
		this.assets = assets;
	}

	@Override
	List<Stage> doGetStages() {
		String[] names = getSortedFileNames(JARO_ASSETS_DIR);
		List<Stage> stages = new ArrayList<Stage>();
		int count = 0;
		for (String name: names) {
			Stage s = parseStage(name);
			s.setStageIndex(count++);
			stages.add(s);
		}
		return stages;
	}
	private String[] getSortedFileNames(String path) {
		String[] fileNames = assets.list(path);
		return fileNames;
	}
	private static Stage parseStage(String fileName) {
		String caption = cleanName(fileName);
		Stage stage = new Stage(fileName, caption);
		return stage;
	}

	@Override
	List<Level> doGetLevels(String stageKey) {
		String[] names = getSortedFileNames(JARO_ASSETS_DIR + "/" + stageKey);
		List<Level> levels = new ArrayList<Level>();
		for (String name: names) {
			Level l = parseLevel(stageKey, name);
			levels.add(l);
		}
		return levels;
	}
	
	private static Level parseLevel(String stageKey, String levelKey) {
		String caption = cleanName(levelKey);
		int pos = caption.lastIndexOf('.');
		caption = caption.substring(0, pos);
		Level level = new Level(levelKey, caption, stageKey, DATA_TYPE);
		return level;
	}

	@Override
	public Grid getGrid(Level level) {
		InputStream in = assets.open(JARO_ASSETS_DIR + "/" + level.getStageKey() + "/" + level.getLevelKey());
		String data = Utils.toString(in);
		Grid grid = parseGrid(data, level);
		grid.initIds();
		return grid;
	}

	public Grid parseGrid(String data, Level level) {
		String html = cleanHtml(data);
		return parseGrid(html);
	}
	private String cleanHtml(String s) {
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
				Piece p = JaroPieceRules.parsePiece(id);
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
		
		return grid;
	}
	private int getCols(String htmlData) {
		int cols = 0;
		int pos = htmlData.indexOf("<tr>");
		int pos2 = htmlData.indexOf("</tr>");
		String sub = htmlData.substring(pos, pos2);
		pos = 1;
		while (pos >= 0) {
			pos = sub.indexOf("<td>", pos + 1);
			cols++;
		}
		return cols - 1;
	}
	
}
