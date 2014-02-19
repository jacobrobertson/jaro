package com.robestone.jaro.levels;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.robestone.jaro.Grid;
import com.robestone.jaro.Piece;
import com.robestone.jaro.piecerules.JaroPieceRules;

public class SokobanLevelParserHelper {

	private Map<String, String> fileNameMap = new HashMap<String, String>();
	private boolean isDataSplitByNewline;
	
	public SokobanLevelParserHelper(boolean isDataSplitByNewline) {
		addFileNameMappings();
		this.isDataSplitByNewline = isDataSplitByNewline;
	}
	
	public Grid parseGrid(String data, Level level) {
		List<List<String>> tokens;
		if (isDataSplitByNewline) {
			tokens = toTokensForSplitData(data);
		} else {
			tokens = toTokensForSingleLineData(level, data);
		}
		int rows = tokens.size();
		int cols = tokens.get(0).size();
		Grid grid = new Grid(cols, rows);
		
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				String token = tokens.get(r).get(c);
				if (token.equals("w")) {
					String key = getPositionKey(c, r, tokens);
					Piece piece = JaroPieceRules.parsePiece(key);
					grid.addPiece(piece, c, r);
				} else if ("^v<>AV{}".contains(token)) {
					buildTurtle(token, grid, c, r);
				} else if (token.equals("f")) {
					// floor - same as void
					// this could be changed later - jaro doesn't currently have a concept of a non-picture tile
				} else if (token.equals("a")) {
					Piece piece = JaroPieceRules.parsePiece("turtlenest");
					grid.addPiece(piece, c, r);
				} else if ("_".equals(token)) {
					// do we have a blank piece??
				} else if ("j".equals(token.toLowerCase())) {
					if ("J".equals(token)) {
						Piece piece = JaroPieceRules.parsePiece("turtlenest");
						grid.addPiece(piece, c, r);
					}
					String key = "jaro";
					Piece piece = JaroPieceRules.parsePiece(key);
					grid.addPiece(piece, c, r);
				} else {
					throw new IllegalArgumentException("Unknown piece: " + token);
				}
			}
		}
		grid.initIds();
		return grid;
	}
	private void buildTurtle(String key, Grid grid, int x, int y) {
		String turtles = "^v<>";
		int pos = turtles.indexOf(key);
		boolean addNest = (pos < 0);
		if (addNest) {
			String onBoxes = "AV{}";
			pos = onBoxes.indexOf(key);
		}
		String dir = null;
		switch (pos) {
		case 0:
			dir = "up";
			break;
		case 1:
			dir = "down";
			break;
		case 2:
			dir = "left";
			break;
		case 3:
			dir = "right";
			break;
		default:
			throw new IllegalArgumentException(key);
		}
		if (addNest) {
			Piece piece = JaroPieceRules.parsePiece("turtlenest");
			grid.addPiece(piece, x, y);
		}
		String file = "turtle_" + dir;
		Piece piece = JaroPieceRules.parsePiece(file);
		grid.addPiece(piece, x, y);
	}
	private String getPositionKey(int x, int y, List<List<String>> tokens) {
		StringBuilder buf = new StringBuilder();
		
		int rows = tokens.size();
		int cols = tokens.get(0).size();
		buf.append(getPositionKey(x, y, -1, -1, cols, rows, tokens));
		buf.append(getPositionKey(x, y, 0, -1, cols, rows, tokens));
		buf.append(getPositionKey(x, y, 1, -1, cols, rows, tokens));
		buf.append("_");
		buf.append(getPositionKey(x, y, -1, 0, cols, rows, tokens));
		buf.append("x");
		buf.append(getPositionKey(x, y, 1, 0, cols, rows, tokens));
		buf.append("_");
		buf.append(getPositionKey(x, y, -1, 1, cols, rows, tokens));
		buf.append(getPositionKey(x, y, 0, 1, cols, rows, tokens));
		buf.append(getPositionKey(x, y, 1, 1, cols, rows, tokens));
		
		String key = buf.toString();
		String actualKey = fileNameMap.get(key);

//		System.out.println("[" + x + "," + y + "] " + key + " => " + actualKey + " > " + tokens.get(y));

		if (actualKey == null) {
			throw new IllegalArgumentException(key);
		}
		
		return actualKey;
	}
	private String getPositionKey(int x, int y, int dx, int dy, int cols, int rows, List<List<String>> tokens) {
		x += dx;
		y += dy;
		
		if (x < 0 || x >= cols) {
			return "o";
		}
		if (y < 0 || y >= rows) {
			return "o";
		}
		
		String token = tokens.get(y).get(x);
		if ("w".equals(token)) {
			return "x";
		} else {
			return "o";
		}
	}
	public List<List<String>> toTokensForSingleLineData(Level level, String data) {
		List<String> allTokens = toTokensRow(level, data);
		List<List<String>> tokens = new ArrayList<List<String>>();
		int cols = level.getCols();
		int rows = level.getRows();
		int pos = 0;
		for (int r = 0; r < rows; r++) {
			List<String> row = new ArrayList<String>();
			for (int c = 0; c < cols; c++) {
				row.add(allTokens.get(pos++));
			}
			tokens.add(row);
		}
		return tokens;
	}
	private String getTurtle(String seedKey, String token, int count) {
		int pos = count % seedKey.length();
		int c = seedKey.charAt(pos);
		c = (c % 4);
		String turtles = "^v<>";
		String onBoxes = "AV{}";
		String ttype;
		if (token.equals("t")) {
			ttype = turtles;
		} else if (token.equals("T")) {
			ttype = onBoxes;
		} else {
			throw new IllegalArgumentException();
		}
		return ttype.substring(c, c + 1);
	}
	
	public List<List<String>> toTokensForSplitData(String data) {
		data = data.trim();
		List<List<String>> tokens = new ArrayList<List<String>>();
		
		int cols = -1;
		String[] rows = data.split("\n");
		for (String row: rows) {
			row = row.trim();
			List<String> rowTokens = toTokensRow(null, row);
			int testCols = rowTokens.size();
			if (cols < 0) {
				cols = testCols;
			} else if (cols != testCols) {
				throw new IllegalArgumentException("Rows not equal width: \n"
						+ "bad row: " + row + "\n" 
						+ "grid:" + tokens);
			}
			tokens.add(rowTokens);
		}
		
		return tokens;
	}
	public List<String> toTokensRow(Level level, String row) {
		List<String> tokens = new ArrayList<String>();
		String seedKey = null;
		if (level != null) {
			seedKey = level.getStageKey() + ":" + level.getLevelKey();
		}
		
		// 3_wf>f2aw3af2w4_
		// also - we will consider T and t - have to account for a predictable ordering of turtles position
		int turtleCount = 0;
		int count = 0;
		for (int i = 0; i < row.length(); i++) {
			char c = row.charAt(i);
			if (Character.isDigit(c)) {
				count = count * 10 + Character.digit(c, 10);
			} else {
				if (count == 0) {
					count = 1;
				}
				String token = String.valueOf(c);
				for (int j = 0; j < count; j++) {
					String t = token;
					if (token.equalsIgnoreCase("t")) {
						t = getTurtle(seedKey, t, turtleCount);
						turtleCount++;
					}
					tokens.add(t);
				}
				count = 0;
			}
		}
		
		return tokens;
	}
	private void addFileNameMapping(String f, String t) {
		int pos = f.indexOf('*');
		if (pos >= 0) {
			String fo = f.replaceFirst("\\*", "o");
			addFileNameMapping(fo, t);
			String fx = f.replaceFirst("\\*", "x");
			addFileNameMapping(fx, t);
		} else {
			fileNameMap.put(f, t);
		}
		
	}
	private void addFileNameMappings() {
		
		addFileNameMapping("*o*_oxo_*o*", "boulder");

		addFileNameMapping("*o*_oxx_*o*", "cave_wall_left_bump");
		addFileNameMapping("*o*_oxo_*x*", "cave_wall_top_bump");
		addFileNameMapping("*o*_oxx_*xo", "cave_top_left_l");
		addFileNameMapping("*o*_oxx_*xx", "cave_bottom_right_bump");
		addFileNameMapping("*o*_xxo_*o*", "cave_wall_right_bump");
		addFileNameMapping("*o*_xxo_ox*", "cave_top_right_l");
		addFileNameMapping("*o*_xxo_xx*", "cave_bottom_left_bump");
		addFileNameMapping("*o*_xxx_*o*", "cave_wall_horizontal");
		addFileNameMapping("*o*_xxx_oxo", "cave_wall_t_bottom");
		addFileNameMapping("*o*_xxx_oxx", "cave_bottom_bl_corner");
		addFileNameMapping("*o*_xxx_xxo", "cave_bottom_br_corner");
		addFileNameMapping("*o*_xxx_xxx", "cave_bottom");
		addFileNameMapping("*x*_oxo_*o*", "cave_wall_bottom_bump");
		addFileNameMapping("*x*_oxo_*x*", "cave_wall_vertical");
		addFileNameMapping("*xo_oxx_*o*", "cave_bottom_left_l");
		addFileNameMapping("*xo_oxx_*xo", "cave_wall_t_right");
		addFileNameMapping("*xo_oxx_*xx", "cave_right_tr_corner");
		addFileNameMapping("ox*_xxo_*o*", "cave_bottom_right_l");
		addFileNameMapping("ox*_xxo_ox*", "cave_wall_t_left");
		addFileNameMapping("ox*_xxo_xx*", "cave_left_tl_corner");
		addFileNameMapping("oxo_xxx_*o*", "cave_wall_t_top");
		addFileNameMapping("oxo_xxx_oxo", "cave_t_center");
		addFileNameMapping("oxo_xxx_oxx", "cave_tl_br");
		addFileNameMapping("oxx_xxx_oxo", "cave_tl_tr");
		addFileNameMapping("xxo_xxx_oxo", "cave_tl_tl");
		addFileNameMapping("oxo_xxx_xxo", "cave_tl_bl");
		addFileNameMapping("oxo_xxx_xxx", "cave_t_bottom");
		addFileNameMapping("*xx_oxx_*o*", "cave_top_right_bump");
		addFileNameMapping("*xx_oxx_*xo", "cave_right_br_corner");
		addFileNameMapping("*xx_oxx_*xx", "cave_right");
		addFileNameMapping("oxx_xxx_*o*", "cave_top_tl_corner");
		addFileNameMapping("oxx_xxx_oxx", "cave_t_left");
		addFileNameMapping("oxx_xxx_xxo", "cave_diag_b");
		addFileNameMapping("oxx_xxx_xxx", "cave_bottom_right");
		addFileNameMapping("xx*_xxo_*o*", "cave_top_left_bump");
		addFileNameMapping("xx*_xxo_ox*", "cave_left_bl_corner");
		addFileNameMapping("xx*_xxo_xx*", "cave_left");
		addFileNameMapping("xxo_xxx_*o*", "cave_top_tr_corner");
		addFileNameMapping("xxo_xxx_oxx", "cave_diag_a");
		addFileNameMapping("xxo_xxx_xxo", "cave_t_right");
		addFileNameMapping("xxo_xxx_xxx", "cave_bottom_left");
		addFileNameMapping("xxx_xxx_*o*", "cave_top");
		addFileNameMapping("xxx_xxx_oxo", "cave_t_top");
		addFileNameMapping("xxx_xxx_oxx", "cave_top_right");
		addFileNameMapping("xxx_xxx_xxo", "cave_top_left");
		addFileNameMapping("xxx_xxx_xxx", "cave_solid");
	}
	void printFileNameMapping() throws Exception {
		File dir = new File("C:\\Users\\Jacob\\eclipse-workspace-silver\\jaro\\res\\drawable");
		File[] files = dir.listFiles();
		for (File file: files) {
			if (file.getName().startsWith("w_")) {
				File match = findMatchingFile(file, files);
				System.out.println("\t\taddFileNameMapping(\"" + file.getName() + "\", \"" + match.getName() + "\");");
			}
		}
	}
	private File findMatchingFile(File file, File[] files) throws Exception {
		for (File test: files) {
			if (test != file && test.isFile() && !test.getName().startsWith("w_") && file.length() == test.length()) {
				boolean equals = equals(test, file);
				if (equals) {
					return test;
				}
			}
		}
		throw new IllegalArgumentException(file.getName());
	}
	private boolean equals(File f1, File f2) throws Exception {
		InputStream in1 = new FileInputStream(f1);
		InputStream in2 = new FileInputStream(f2);
		try {
			while (true) {
				int i1 = in1.read();
				int i2 = in2.read();
				if (i1 != i2) {
					return false;
				}
				if (i1 < 0 || i2 < 0) {
					return i1 == i2;
				}
			}
		} finally {
			in1.close();
			in2.close();
		}
	}
	
	private class FileEntryIndex {
		String stage;
		String level;
		int cols;
		int rows;
		String data;
	}
	
	/**
	 * Create one master file with the following format
	 * 
	 * Index lines look like this:
	 * Stage
	 * Level1:columns:position\n
	 * Level2:columns:position\n
	 * \n
	 * 
	 * An extra newline will be between the index and the data
	 * 
	 * Level lines are
	 * data\n
	 * 
	 * Simplify {}AV, etc to just b and B (removing the turtle-facing concept)
	 * Remove all newlines within a grid
	 * 
	 * 	wf>wfwawfw^fw4_
		w4fwAw4f5w
		w2f3wa3w2fw3fw
		w2fa}aja{a6fw
		w2f3wa3w2fw3fw
	 */
	public void compressFiles() throws Exception {
		List<FileEntryIndex> indexes = new ArrayList<FileEntryIndex>();
		File dir = new File("C:\\Users\\Jacob\\eclipse-workspace-silver\\jaro\\parsed-sokoban\\levels\\s_006.Jaro-ban (10,000 Levels)");
		File[] stages = dir.listFiles();
		for (File stage: stages) {
			File[] levels = stage.listFiles();
			for (File level: levels) {
				FileEntryIndex index = compressFile(level);
				indexes.add(index);
			}
		}
		
		// output
		File dbFile = new File("C:\\Users\\Jacob\\eclipse-workspace-silver\\jaro\\parsed-sokoban\\levels\\db.txt");
		File indexFile = new File("C:\\Users\\Jacob\\eclipse-workspace-silver\\jaro\\parsed-sokoban\\levels\\index.txt");
//		FileOutputStream out = new FileOutputStream(compressed);
		FileWriter dbWriter = new FileWriter(dbFile);
		FileWriter indexWriter = new FileWriter(indexFile);
		String lastStage = null;
		for (FileEntryIndex index: indexes) {
			if (lastStage == null) {
				indexWriter.write(index.stage);
				indexWriter.write('\n');
			} else if (!lastStage.equals(index.stage)) {
				indexWriter.write('\n');
				indexWriter.write(index.stage);
				indexWriter.write('\n');
			}
			indexWriter.write(index.level);
			indexWriter.write(':');
			indexWriter.write(String.valueOf(index.cols));
			indexWriter.write(':');
			indexWriter.write(String.valueOf(index.rows));
			indexWriter.write('\n');
			
			lastStage = index.stage;
//			 * Stage:Level:columns:rows:position\n
		}
		for (FileEntryIndex index: indexes) {
			dbWriter.write(index.data);
			dbWriter.write('\n');
		}
		dbWriter.close();
		indexWriter.close();
	}
	
	/**
	 * File name will be
	 * 	C:\Users\Jacob\eclipse-workspace-silver\jaro\parsed-sokoban\levels\s_006.Jaro-ban (10,000 Levels)\aenigma\l_001.soko_01.txt
	 * s_NUM.Jaro-ban (10,000 Levels)\SUBSTAGE\1_NUM.NAME_NAME.txt
	 */
	private Pattern fileNamePattern = Pattern.compile(".*/s_[0-9]+\\..*/(.*)/l_[0-9]+\\.(.*)\\.txt");
	public FileEntryIndex compressFile(File file) throws Exception {
		FileEntryIndex index = new FileEntryIndex();
		// data in "old" format
		FileInputStream in = new FileInputStream(file);
		String data = Utils.toString(in);

		List<List<String>> tokens = toTokensForSplitData(data);
		index.cols = tokens.get(0).size();
		index.rows = tokens.size();
				
		data = data.replaceAll("[\\^<>v]", "t");
		data = data.replaceAll("[A{}V]", "T");
		data = compressDuplicateCells(data);

		index.data = data;
		
		String fileName = file.getAbsolutePath();
		fileName = fileName.replace('\\', '/');
		Matcher m = fileNamePattern.matcher(fileName);
		if (m.matches()) {
			index.stage = m.group(1);
			index.level = m.group(2);
		} else {
			throw new IllegalArgumentException(fileName + " doesn't match pattern");
		}
		
		return index;
	}
	private String compressDuplicateCells(String data) {
		List<List<String>> tokens = toTokensForSplitData(data);
		
		List<String> toCompress = new ArrayList<String>();
		for (List<String> row: tokens) {
			toCompress.addAll(row);
		}
		
		String newData = SokobanImporter.toOutputRow(toCompress);
		return newData;
	}
}
