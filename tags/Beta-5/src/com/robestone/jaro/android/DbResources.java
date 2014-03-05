package com.robestone.jaro.android;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.robestone.jaro.Grid;
import com.robestone.jaro.levels.JaroAssets;
import com.robestone.jaro.levels.Level;
import com.robestone.jaro.levels.SokobanLevelParserHelper;
import com.robestone.jaro.levels.Stage;

public class DbResources extends JaroAndroidResources {

	public static final String JAROBAN_GAME_TYPE = "Jaroban";
	public static final String JARO_ASSETS_DIR = "stage-data/" + JAROBAN_GAME_TYPE;
	public static final String INDEX_FILE = JARO_ASSETS_DIR + "/index.txt";
	public static final String DB_FILE = JARO_ASSETS_DIR + "/db.txt";
	public static final String DATA_TYPE = "db";

	private SokobanLevelParserHelper parser = new SokobanLevelParserHelper(false);
	private JaroAssets assets;

	public DbResources(JaroAssets assets) {
		this.assets = assets;
	}

	@Override
	List<Level> doGetLevels(String stageKey) {
		InputStream in = assets.open(INDEX_FILE);
		return parseLevels(stageKey, in);
	}
	
	@Override
	List<Stage> doGetStages() {
		InputStream in = assets.open(INDEX_FILE);
		return parseStages(in);
	}

	@Override
	public Grid getGrid(Level level) {
		try {
			return doGetGrid(level);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	private Grid doGetGrid(Level level) throws IOException {
		String gs = getGridString(level);
		return parser.parseGrid(gs, level);
	}
	public String getGridString(Level level) throws IOException {
		InputStream in = assets.open(DB_FILE);
		in = new BufferedInputStream(in);

		// TODO there should be a better way to do this - but it's all in memory already, so this isn't too bad
		Stage found = null;
		for (Stage stage: getStages()) {
			if (stage.getStageKey().equals(level.getStageKey())) {
				found = stage;
				break;
			}
		}
		
		// skip to the right stage
		int stageIndex = found.getStageIndex();
		int index = level.getLevelIndex() + stageIndex;
		for (int i = 0; i < index; i++) {
			while (in.read() != '\n') {
				// do nothing
			}
		}
		
		// read in the line
		StringBuilder buf = new StringBuilder();
		int r;
		while ((r = in.read()) != '\n') { 
			buf.append((char) r);
		}
		
		in.close();
		return buf.toString();
	}
	
	public List<Level> parseLevels(String stageKey, InputStream in) {
		List<Level> levels = new ArrayList<Level>();
		
		boolean inStage = false;
		BufferedReader buf = new BufferedReader(new InputStreamReader(in));
		String line;
		try {
			while ((line = buf.readLine()) != null) {
				line = line.trim();
				
				if (inStage) {
					if (line.length() == 0) {
						break;
					}
					// soko_01:15:10
					int pos = line.indexOf(':');
					String levelKey = line.substring(0, pos);
					int pos2 = line.indexOf(':', pos + 1);
					String colString = line.substring(pos + 1, pos2);
					String rowString = line.substring(pos2 + 1);
					
					int col = Integer.parseInt(colString);
					int row = Integer.parseInt(rowString);
					String caption = cleanName(levelKey);
					Level level = new Level(levelKey, caption, stageKey, DATA_TYPE);
					level.setLevelIndex(levels.size());
					level.setCols(col);
					level.setRows(row);
					levels.add(level);
				} else {
					if (line.length() == 0) {
						continue;
					}
					int pos = line.indexOf(':');
					if (pos < 0) {
						// then it's a stage - so see if it's the right one
						if (stageKey.equals(line)) {
							inStage = true;
						}
					}
				}
			}
			in.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return levels;
	}

	/*
aenigma
soko_01:15:10
soko_02:17:13
soko_50:10:11

Alberto
level_01:8:13
	
	*/
	/**
	 * First pass - just suck the file in, and throw it away - can get
	 * this more efficient once we get it to work.
	 */
	public List<Stage> parseStages(InputStream in) {
		List<Stage> stages = new ArrayList<Stage>();
		
		int stageIndex = 0;
		BufferedReader buf = new BufferedReader(new InputStreamReader(in));
		String line;
		try {
			while ((line = buf.readLine()) != null) {
				line = line.trim();
				if (line.length() == 0) {
					continue;
				}
				int pos = line.indexOf(':');
				if (pos < 0) {
					// then it's a stage
					String caption = cleanName(line);
					Stage s = new Stage(line, caption);
					s.setStageIndex(stageIndex);
					stages.add(s);
				} else {
					stageIndex++;
				}
			}
			in.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return stages;
	}
	

}
