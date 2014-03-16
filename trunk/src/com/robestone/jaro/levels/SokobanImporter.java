package com.robestone.jaro.levels;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.robestone.jaro.JaroFileAssets;
import com.robestone.jaro.LevelPersisterMock;
import com.robestone.jaro.android.DbResources;
import com.robestone.jaro.levels.SokobanLevelParserHelper.FileEntryIndex;

/**
 
 <div id="startLevel">
	<l 
	id="349" number="4" // can be used to generate the stages without knowing in advance 
	total_levels="50" name="soko 04" cid="4" 
	cname="aenigma" solution=""  
	author="Brian Kent"
	 best="Leszek,102,323,183,40!Apriori,102,340,233,40!renatoantoni...,102,570,112,40!anonymous,102,0,,40!MaxKlaxx,102,673,89,40!bojidar,102,913,32,40!JotaCartas,102,114,185,40!Rose,103,174,233,40!BoBell,103,228,233,40!Ballyconneely,103,332,65,40!Srunge,103,451,235,40!richard.h,103,577,10,40!" best_page="1" creation_date="1317972535" tmark="ab33e49fb984b08b1ce2795321641709" status="0" comments="0">
	 <r>19v</r><r>19v</r><r>7v,5w,7v</r><r>6v,2w,3f,2w,6v</r><r>4v,3w,5f,3w,4v</r><r>3v,2w,3f,w,f,w,3f,2w,3v</r><r>2v,2w,11f,2w,2v</r><r>2v,w,2f,w,2f,3a,f,w,3f,w,2v</r><r>2v,w,5f,a,f,a,5f,w,2v</r><r>2v,w,3f,w,f,3a,2f,w,2f,w,2v</r><r>2v,2w,11f,2w,2v</r><r>3v,2w,3f,w,f,w,3f,2w,3v</r><r>4v,3w,5f,3w,4v</r><r>6v,2w,3f,2w,6v</r><r>7v,5w,7v</r><r>19v</r><r>19v</r>
	 <am>141,142,143,160,162,179,180,181</am>
	 <b>84,86,137,146,176,185,236,238</b>
	 <mv>161</mv>
	</l></div>
</div>
 
 */
public class SokobanImporter {
	
	// C:\Users\Jacob\eclipse-workspace-silver\jaro\res\raw
	
	public static void main(String[] args) throws Exception {
		new SokobanImporter().
		createRankedIndex()
//		createStatisticsFile()
//		createDbFromDownloadedFiles()
//		downloadAllLevels()
//		downloadAllLevels(473, 473)
//		createStatisticsFile(2838, 2838)
		;
	}
	private void downloadAllLevels() throws Exception {
		downloadAllLevels(1, 18000, "all-collections");
	}
	private void downloadAllLevels(int start, int end, String folderName) throws Exception {
		// 14254
		String rootDir = "C:\\Users\\Jacob\\eclipse-workspace-silver\\jaro\\sokoban-downloads\\" + folderName;
		for (int i = start; i <= end; i++) {
			System.out.println("Level Id: " + i);
			saveLevel(rootDir, i);
		}
	}
	
	
	private static class PopularitySorter implements Comparator<FileEntryIndex> {
		@Override
		public int compare(FileEntryIndex o1, FileEntryIndex o2) {
			return o2.completed - o1.completed;
		}
	}
	private void addRowAndColumn(List<FileEntryIndex> infos, String rootDir) {
		JaroAssets assets = new JaroFileAssets(rootDir);
		DbResources resources = new DbResources(assets, new LevelPersisterMock());
		for (FileEntryIndex info: infos) {
			for (Level level: resources.getLevels(info.stage)) {
				if (level.getLevelKey().equals(info.level)) {
					info.rows = level.getRows();
					info.cols = level.getCols();
					break;
				}
			}
		}
	}
	private List<FileEntryIndex> parseStatisticsFile(String path) throws Exception {
		List<FileEntryIndex> infos = new ArrayList<FileEntryIndex>();
		String data = Utils.toString(new FileInputStream(path));
		String[] lines = data.split("\n");
		for (String line: lines) {
			String[] values = line.split("\\|");
			FileEntryIndex info = new FileEntryIndex();
			int i = 0;
			info.stage = values[i++];
			info.level = values[i++];
			info.moves = Integer.parseInt(values[i++]);
			info.completed = Integer.parseInt(values[i++]);
			info.turtles = Integer.parseInt(values[i++]);
			info.floorTiles = Integer.parseInt(values[i++]);
			// some haven't ever been run, and this will mess up the sorting
			if (info.completed > 0) {
				infos.add(info);
			}
		}
		return infos;
	}
	private void outputEntries(List<FileEntryIndex> infos) {
		for (FileEntryIndex info: infos) {
			System.out.println(info.stage + "|" + 
						info.level + "|" + 
//						info.rows + "|" + 
//						info.cols + "|" + 
						"moves=" + info.moves + "," + 
						"completed=" + info.completed + "," + 
						"turtles=" + info.turtles + "," + 
						"floorTiles=" + info.floorTiles + "," + 
						"rank=" + (new BigDecimal(info.rank).toPlainString())
						);
		}
	}
	
	private void createStatisticsFile() throws Exception {
		createStatisticsFile(1, 18000);
	}
	private void createStatisticsFile(int start, int end) throws Exception {
		SokobanLevelParserHelper helper = new SokobanLevelParserHelper(true);
		String rootDir = "C:\\Users\\Jacob\\eclipse-workspace-silver\\jaro\\sokoban-downloads\\all-collections\\stage-data\\Jaroban";
		FileOutputStream out = new FileOutputStream(rootDir + "/statistics.txt");
		for (int i = start; i <= end; i++) {
			try {
				System.out.println("Level Id: " + i);
				FileEntryIndex info = downloadLevelStats(i);
				addGridInformationFromFile(info, helper, rootDir);
				
				if (info.invalidLevel) {
					// this will mark it as so unpopular, that it won't get picked
					info.completed = 0;
					info.moves = 1000;
					info.turtles = 100;
				}
				
				System.out.println("stage=" + info.stage);
				System.out.println("level=" + info.level);
				System.out.println("moves=" + info.moves);
				System.out.println("completed=" + info.completed);
				System.out.println("floorTiles=" + info.floorTiles);
				
				String line = 
						info.stage + "|" + 
						info.level + "|" + 
//						info.rows + "|" + 
//						info.cols + "|" + 
						info.moves + "|" + 
						info.completed + "|" + 
						info.turtles + "|" + 
						info.floorTiles;
					out.write(line.getBytes());
					out.write('\n');
					out.flush();
			} catch (Exception e) {
				// we're okay with this - probably wasn't a level there - but need to be careful as sometimes it's a parse error
				System.out.println(e.getMessage());
			}
		}
		out.close();
	}
	/**
	 * I want to somehow indicate the "bad" mazes I found like #2838 - already in position, so technically the game is won  
	 */
	private void addGridInformationFromFile(FileEntryIndex info, SokobanLevelParserHelper helper, String rootDir) throws Exception {
		// get the raw file
		String stageDirName =  rootDir + "/" + info.stage;
		File stageDir = new File(stageDirName);
		for (File level: stageDir.listFiles()) {
			String name = level.getName();
			int pos = name.indexOf(".");
			name = name.substring(pos + 1);
			if (name.equals(info.level + ".txt")) {
				FileInputStream in = new FileInputStream(level);
				String data = Utils.toString(in);
				List<List<String>> tokens = helper.toTokensForSplitData(data);
				// keep track of this so we can see if this is an "illegal" game - by my own standards (we know there's at least one level like this)
				int uncoveredDestinationsCount = 0;
				for (List<String> row: tokens) {
					for (String cell: row) {
						if ("aJ".indexOf(cell) >= 0) {
							uncoveredDestinationsCount++;
						}
						if ("f^v<>AV{}ajJ".indexOf(cell) >= 0) {
							info.floorTiles++;
						}
						if ("^v<>AV{}".indexOf(cell) >= 0) {
							info.turtles++;
						}
					}
				}
				if (uncoveredDestinationsCount == 0) {
					info.invalidLevel = true;
				}
				info.rows = tokens.size();
				info.cols = tokens.get(0).size();
				break;
			}
		}
	}
	
	// http://www.game-sokoban.com/index.php?mode=level&lid=349
	private String baseUrl = "http://www.game-sokoban.com/index.php?mode=level&lid=";

	/**
	 * We have to assume the db index are already created, but that's not quite right...
	 * @throws Exception
	 */
	private void createRankedIndex() throws Exception {
		String rootDir = "C:\\Users\\Jacob\\eclipse-workspace-silver\\jaro\\sokoban-downloads\\all-collections";
		String assetsDir = rootDir + "/" + DbResources.JARO_ASSETS_DIR;
		
		List<FileEntryIndex> infos = parseStatisticsFile(assetsDir + "/statistics.txt");
		addRowAndColumn(infos, rootDir);
		
		// filter out all grids that are too large
		int maxCols = 13;
		int maxRows = 17;
		infos = removeLargeGrids(infos, maxCols, maxRows);
		
		// get the list of the top 2000 most popular (so I can have twice what the other android game has)
		Collections.sort(infos, new PopularitySorter());
		infos = infos.subList(0, 2000);
		
		// determine the ranking of each collection now
		// these will already be sorted by collection rank, and the levels in each will be sorted by assigned number
		infos = sortByRanks(infos);

		SokobanLevelParserHelper helper = new SokobanLevelParserHelper(true);
		helper.setCompressing(true, maxCols, maxRows);

		// add the file data - will need before we do the compression
		helper.addFileData(infos, assetsDir);

		// after we get the data, then rename the stages
		createNewStages(infos);

		// TEMP - ensure all info is filled in with data - it's skipping some...
		for (FileEntryIndex info: infos) {
			if (info.data == null) {
				throw new IllegalArgumentException("No Data: " + info.stage + "/" + info.level);
			}
		}
		
		// output the index and db - using this new ordering
		helper.compressFiles(assetsDir, infos, "ranked-");
	}
	private class Collection {
		String stage;
		float minRank;
		float maxRank;
		List<FileEntryIndex> infos = new ArrayList<FileEntryIndex>();
	}
	private List<FileEntryIndex> sortByRanks(List<FileEntryIndex> infos) {
//		IndexRankComparator comp = new IndexRankComparator();
		TurtleCountComparator comp = new TurtleCountComparator();

		Map<String, Collection> colMap = new HashMap<String, Collection>();
		// for each (non-empty) collection, assign a ranking
		for (FileEntryIndex info: infos) {
			// floor tiles / moves
			comp.assignRank(info);
			
			Collection col = colMap.get(info.stage);
			if (col == null) {
				col = new Collection();
				col.stage = info.stage;
				col.minRank = info.rank;
				col.maxRank = info.rank;
				colMap.put(col.stage, col);
			} else {
				if (col.minRank > info.rank) {
					col.minRank = info.rank;
				}
				if (col.maxRank < info.rank) {
					col.maxRank = info.rank;
				}
			}
			col.infos.add(info);
		}
		
		// output just so I can look at it
		// this doesn't actually affect the collections (at this time, but it might later)
		Collections.sort(infos, comp);
		outputEntries(infos);
		
		/*
		// order the collections by overlapping ranks
		// for example 0-5, 2-6, 2-7, 3-7, etc
		// simple algorithm is that we sort collections, first by min, then by max
		List<Collection> cols = new ArrayList<Collection>(colMap.values());
		Collections.sort(cols, new CollectionRankComparator());
		
		System.out.println(">> Collections");
		infos = new ArrayList<FileEntryIndex>();
		for (Collection col: cols) {
			Collections.sort(col.infos, comp);
			infos.addAll(col.infos);
			System.out.println(col.stage + "|" + (new BigDecimal(col.minRank).toPlainString()) + " > " + (new BigDecimal(col.maxRank).toPlainString()));
		}
		
		// TODO actually return the collections - but I wanted to create a whole new way of doing
		*/
		return infos;
	}
	/**
	 * This overwrites existing stage/level info.
	 */
	private void createNewStages(List<FileEntryIndex> infos) {
		int levelsPerStage = 10; // not sure what a good number is, but we picked 2000 levels
		int currentCount = 0;
		int stageNumber = 1;
		for (FileEntryIndex info: infos) {
			info.level = info.stage + ": " + info.level;
			info.level = Utils.toCleanName(info.level);
			info.stage = "Jaroban Stage " + stageNumber;
			
			// now's an okay time to clean up the names
			info.level = info.level.replace("  ", " ");
			
			currentCount++;
			if (currentCount == levelsPerStage) {
				stageNumber++;
				currentCount = 0;
			}
		}
	}
	private class CollectionRankComparator implements Comparator<Collection> {
		@Override
		public int compare(Collection o1, Collection o2) {
			float diff = o2.minRank - o1.minRank;
			if (diff < 0) {
				return -1;
			} else if (diff > 0) {
				return 1;
			}
			diff = o2.maxRank - o1.maxRank;
			if (diff < 0) {
				return -1;
			} else if (diff > 0) {
				return 1;
			}
			return 0;
		}
	}
	private class TurtleCountComparator extends IndexRankComparator {
		@Override
		public void assignRank(FileEntryIndex info) {
			info.rank = info.moves * (10000000000f);
			info.rank += (info.turtles * 100000);
			info.rank += info.floorTiles;
			info.rank = -info.rank;
		}
	}
	private class IndexRankComparator implements Comparator<FileEntryIndex> {
		@Override
		public int compare(FileEntryIndex o1, FileEntryIndex o2) {
			if (o1.rank < o2.rank) {
				return 1;
			} else if (o1.rank > o2.rank) {
				return -1;
			}
			return 0;
		}
		public void assignRank(FileEntryIndex info) {
			info.rank = ((float) info.floorTiles) / ((float) info.moves);
		}
	}
	private List<FileEntryIndex> removeLargeGrids(List<FileEntryIndex> infos, int maxCols, int maxRows) {
		List<FileEntryIndex> newInfos = new ArrayList<FileEntryIndex>();
		for (FileEntryIndex info: infos) {
			if (info.cols <= maxCols && info.rows <= maxRows) {
				newInfos.add(info);
			}
		}
		return newInfos;
	}
	private void createDbFromDownloadedFiles() throws Exception {
		SokobanLevelParserHelper helper = new SokobanLevelParserHelper(true);
		helper.setCompressing(true, -1, -1);
		String dir = "C:\\Users\\Jacob\\eclipse-workspace-silver\\jaro\\sokoban-downloads\\all-collections\\" + DbResources.JARO_ASSETS_DIR;
		helper.compressFiles(dir, dir);
	}
	private void createPopularDbFromScratch() throws Exception {
		List<FileEntryIndex> infos = getAllPopularLevels();
		String dir = "C:\\Users\\Jacob\\eclipse-workspace-silver\\jaro\\sokoban-downloads\\favorites";
		for (FileEntryIndex info: infos) {
			saveLevel(dir, info.id);
		}
		// create the db
		SokobanLevelParserHelper helper = new SokobanLevelParserHelper(true);
		helper.setCompressing(true, 13, 17);
		helper.compressFiles(dir, dir);
	}
	
	private void saveLevel(String outputDir, int id) throws Exception {
		String page = downloadPage(id);
		if (page != null) {
			toCleanFile(outputDir, page);
		}
	}
	
	private String downloadPage(int id) throws Exception {
		String levelUrl = baseUrl + id;
		try {
			URLConnection con = new URL(levelUrl).openConnection();
			InputStream in = con.getInputStream();
			String page = Utils.toString(in);
			if ("Page not found :(".equals(page)) {
				return null;
			}
			return page;
		} catch (IOException ioe) {
			return null;
		}
	}
	
	private void toCleanFile(String outputDir, String page) throws Exception {
		// <r>20v</r><r>20v</r><r>4v,7w,9v</r><r>4v,w,5f,w,9v</r><r>3v,2w,5f,5w,5v</r><r>3v,w,5f,2w,3f,2w,4v</r><r>3v,w,3f,w,a,2w,4f,2w,3v</r><r>3v,w,3f,w,2a,6f,w,3v</r><r>3v,w,3f,3a,2w,4f,w,3v</r><r>3v,4w,2a,f,w,5f,w,3v</r><r>6v,2w,a,f,w,5f,w,3v</r><r>7v,5w,3f,2w,3v</r><r>11v,w,3f,w,4v</r><r>11v,5w,4v</r><r>20v</r>
		String patternString = ".*comments=\"[0-9]*\">(.*?)<am>.*";
		Pattern outerPattern = Pattern.compile(patternString);
		Matcher m = outerPattern.matcher(page);
		boolean find = m.find();
		
		if (find) {
			String data = m.group();
			String lineString = "<r>(.*?)</r>";
			Pattern linePattern = Pattern.compile(lineString);
			m = linePattern.matcher(data);
			List<List<String>> gridTokens = new ArrayList<List<String>>();
			// I have to create tokens first, so I can go back and clean then up with the other data
			while (m.find()) {
				List<String> tokens = rowToTokens(m.group(1));
//				System.out.println(tokens);
				gridTokens.add(tokens);
			}
			
			// add other data overlayed
			/*
			spot to put the boxes (not sure why they're repeated here)
			<am>141,142,143,160,162,179,180,181</am>
			
			boxes
	 		<b>84,86,137,146,176,185,236,238</b>
	 		
	 		jaro sits here
	 		<mv>161</mv>
			*/
			int rows = gridTokens.size();
			int cols = gridTokens.get(0).size();
			List<Point> am = toPoints("am", data, rows, cols);
			for (Point p: am) {
				// validate
//				System.out.println("am:" + p.x + "," + p.y);
				String token = gridTokens.get(p.y).get(p.x);
				if (!token.equals("a")) {
					throw new IllegalArgumentException();
				}
			}
			
			List<Point> boxes = toPoints("b", data, rows, cols);
			for (Point p: boxes) {
//				System.out.println("b:" + p.x + "," + p.y);
				// either it's on a panel, or on a floor
				String token = gridTokens.get(p.y).get(p.x);
				String turtle = getTurtle(token);
				gridTokens.get(p.y).set(p.x, turtle);
			}
			List<Point> jaro = toPoints("mv", data, rows, cols);
			for (Point p: jaro) {
//				System.out.println("j:" + p.x + "," + p.y);
				String token = gridTokens.get(p.y).get(p.x);
				if (token.equals("f")) {
					gridTokens.get(p.y).set(p.x, "j");
				} else if (token.equals("a")) {
					gridTokens.get(p.y).set(p.x, "J");
				} else {
					throw new IllegalArgumentException();
				}
			}
			
			cleanGridSpace(gridTokens);
			
			
			// get the name and number
			String cname = getAttribute("cname", page);
			
			// this is a special weird case, so just skip it
			if ("".equals(cname)) {
				return;
			}
			String name = getAttribute("name", page);
			String levelNumber = getAttribute("number", page);
			
			System.out.println("==================[" + cname + ":" + name + ":" + levelNumber + "]====================");
			for (List<String> tokens: gridTokens) {
				System.out.println(tokens);
			}

			output(outputDir, gridTokens, cname, name, levelNumber);
			
		} else {
			throw new IllegalArgumentException("Could not parse page into " + outputDir + ": " + page);
		}
	}
	private void cleanGridSpace(List<List<String>> gridTokens) {
		while (true) {
			boolean cleaned = cleanRow(gridTokens, 0);
			if (!cleaned) {
				break;
			}
		}
		while (true) {
			int last = gridTokens.size() - 1;
			boolean cleaned = cleanRow(gridTokens, last);
			if (!cleaned) {
				break;
			}
		}
		while (true) {
			boolean cleaned = cleanColumn(gridTokens, 0);
			if (!cleaned) {
				break;
			}
		}
		while (true) {
			int last = gridTokens.get(0).size() - 1;
			boolean cleaned = cleanColumn(gridTokens, last);
			if (!cleaned) {
				break;
			}
		}
	}
	private boolean cleanColumn(List<List<String>> gridTokens, int num) {
		boolean allBlank = true;
		for (List<String> row: gridTokens) {
			String t = row.get(num);
			if (!"_".equals(t)) {
				allBlank = false;
				break;
			}
		}
		if (allBlank) {
			for (List<String> row: gridTokens) {
				row.remove(num);
			}
			return true;
		} else {
			return false;
		}
	}
	private boolean cleanRow(List<List<String>> gridTokens, int num) {
		// check the row
		List<String> row = gridTokens.get(num);
		boolean allBlank = true;
		for (String t: row) {
			if (!"_".equals(t)) {
				allBlank = false;
				break;
			}
		}
		if (allBlank) {
			gridTokens.remove(num);
			return true;
		} else {
			return false;
		}
	}
	private String getTurtle(String token) {
		/*
		Make turtles aim in random directions - shouldn't be all facing same way
		*/
		String turtles = "^v<>";
		String onBoxes = "AV{}";
		int random = new Random().nextInt(4);
		String ttype;
		if (token.equals("f")) {
			ttype = turtles;
		} else if (token.equals("a")) {
			ttype = onBoxes;
		} else {
			throw new IllegalArgumentException();
		}
		return ttype.substring(random, random + 1);
	}
	private void output(String outputDir, List<List<String>> grid, String cname, String name, String levelNumber) throws Exception {
		File levelsDir = new File(outputDir);
		cname = Utils.toCleanName(cname);
		name = Utils.toCleanName(name);
		File cnameDir = new File(levelsDir, cname);
		cnameDir.mkdirs();
		
		while (levelNumber.length() < 3) {
			levelNumber = "0" + levelNumber;
		}
		
		File file = new File(cnameDir, levelNumber + "." + name + ".txt");
		FileOutputStream out = new FileOutputStream(file);
		PrintWriter writer = new PrintWriter(out);
		for (List<String> row: grid) {
			String r = toOutputRow(row);
			writer.write(r);
			writer.write("\n");
		}
		writer.close();
	}
	public static String toOutputRow(List<String> row) {
		StringBuilder buf = new StringBuilder();
		StringBuilder one = new StringBuilder();
		
		String last = null;
		for (String t: row) {
			if (t.equals(last) || one.length() == 0) {
				one.append(t);
			} else {
				appendTokens(buf, one);
				one = new StringBuilder(t);
			}
			last = t;
		}
		appendTokens(buf, one);
		
		return buf.toString();
	}
	private static void appendTokens(StringBuilder buf, StringBuilder tokens) {
		String count;
		if (tokens.length() == 1) {
			count = "";
		} else {
			count = String.valueOf(tokens.length());
		}
		buf.append(count);
		buf.append(tokens.charAt(0));
	}
	private String getAttribute(String name, String page) {
		Pattern p = Pattern.compile("<l\\s+.*?\\s" + name + "=\"(.*?)\"");
		Matcher m = p.matcher(page);
		if (m.find()) {
			return m.group(1);
		} else {
			throw new IllegalArgumentException();
		}
	}
	private List<Point> toPoints(String tag, String data, int rows, int cols) {
		List<Point> points = new ArrayList<Point>();
		
		Pattern pattern = Pattern.compile("<" + tag + ">(.*?)</" + tag + ">");
		Matcher m = pattern.matcher(data);

		if (m.find()) {
			String nums = m.group(1);
			String[] split = nums.split(",");
			for (String numString: split) {
				int num = Integer.parseInt(numString);
				int x = (num % cols);
				int y = (num / cols);
				points.add(new Point(x, y));
			}
		}
		
		return points;
	}
	private List<String> rowToTokens(String row) {
		List<String> tokens = new ArrayList<String>();
		// 4v,7w,9v
		// <td><img src="http://jacobrobertson.com/jaro/drawable/boulder.png" /></td>
		row = row.replace('v', '_');
		String[] split = row.split(",");
		for (String one: split) {
			int num;
			String token;
			if (one.length() == 1) {
				num = 1;
				token = one;
			} else {
				// could be 20v
				int index = one.length() - 1;
				String numString = one.substring(0, index);
				token = one.substring(index);
				num = Integer.parseInt(numString);
			}
			for (int i = 0; i < num; i++) {
				tokens.add(token);
			}
		}
		
		return tokens;
	}

	/*
<div>Most popular levels in this collection</div>
						 <a href="index.php?mode=level&amp;lid=12405"><img id="popular_item_1" width="120" src="./pics/thumb_12405.gif"title="&laquo;level  05&raquo;
 completed 69 times"></a><a href="index.php?mode=level&amp;lid=12402"><img id="popular_item_2" width="120" src="./pics/thumb_12402.gif"title="&laquo;level  02&raquo;
 completed 59 times"></a><a href="index.php?mode=level&amp;lid=12403"><img id="popular_item_3" width="120" src="./pics/thumb_12403.gif"title="&laquo;level  03&raquo;
 completed 59 times"></a><a href="index.php?mode=level&amp;lid=12408"><img id="popular_item_4" width="120" src="./pics/thumb_12408.gif"title="&laquo;level  08&raquo;
 completed 58 times"></a><a href="index.php?mode=level&amp;lid=12401"><img id="popular_item_5" width="120" src="./pics/thumb_12401.gif"title="&laquo;level  01&raquo;
 completed 51 times"></a><a href="index.php?mode=level&amp;lid=12407"><img id="popular_item_6" width="120" src="./pics/thumb_12407.gif"title="&laquo;level  07&raquo;
 completed 51 times"></a>
</div>

 	*/
	private List<FileEntryIndex> getAllPopularLevels() throws Exception {
		List<FileEntryIndex> infos = new ArrayList<FileEntryIndex>();
		Pattern pattern = Pattern.compile(
				"popular_item_.\" width=\"[0-9]+\" src=\"./pics/thumb_([0-9]+).gif" +
				"\\s*\"title=\"&laquo;.*?&raquo;\\s*" + 
				" completed ([0-9]+) time"
				);
		
		// there are holes in the catalog
		for (int i = 0; i < 150; i++) {
			System.out.println("Download catalog: " + i);
			// download the catalog - it might be blank
			String url = "http://www.game-sokoban.com/index.php?mode=catalog&cid=" + i;
			URLConnection con = new URL(url).openConnection();
			InputStream in = con.getInputStream();
			String page = Utils.toString(in);
			Matcher m = pattern.matcher(page);
			while (m.find()) {
				FileEntryIndex info = new FileEntryIndex();
				String id = m.group(1);
				info.id = Integer.parseInt(id);
				String times = m.group(2);
				info.completed = Integer.parseInt(times);
				infos.add(info);
			}
		}
		return infos;
	}
	
	private FileEntryIndex downloadLevelStats(int level) throws Exception {
		
		FileEntryIndex info = new FileEntryIndex();
		
		String url = "http://www.game-sokoban.com/index.php?mode=best&lid=" + level;
		
		info.completed = 0;
		info.moves = 0;
		
		Pattern p = Pattern.compile("class=\"result-cell1\">([0-9]+)</td>");
		// very first instance of this is what I want
		// but I also want to count them
		// class="result-cell1">59</td>
		
		// not sure about this - I wanted the average to fix a bug in the moves required,
		// but the bug is a bigger issue, because my rules for solving a level are different - you aren't required to make at least one move
		// however, doing an average isn't really all that bad anyways
		int numToAverage = 10;
		int totalMoves = 0;
		int numAveraged = 0;
		String data = download(url);
		Matcher m = p.matcher(data);
		while (m.find()) {
			info.completed++;
			numAveraged++;
			if (numAveraged <= numToAverage) {
				String num = m.group(1);
				int moves = Integer.parseInt(num);
				totalMoves += moves;
				info.moves = (totalMoves / numAveraged);
			}
		}
		
		// <a href="index.php?mode=catalog&amp;cid=4">aenigma</a> &#0187; <a href="index.php?mode=level&amp;lid=5004">soko 14</a>
		p = Pattern.compile("cid=[0-9]+\">(.*?)</a");
		m = p.matcher(data);
		if (!m.find()) {
			throw new IllegalArgumentException("No collection for: " + level);
		}
		info.stage = Utils.toCleanName(m.group(1));
		
		p = Pattern.compile("lid=" + level + "\">(\\w.*?)</a");
		m = p.matcher(data);
		if (!m.find()) {
			throw new IllegalArgumentException("No levelName for: " + level);
		}
		info.level = Utils.toCleanName(m.group(1));
		info.id = level;
		
		return info;
	}
	private String download(String url) throws IOException {
		int timeout = 1000;
		int tries = 100;
		int sleep = 500;
		for (int i = 0; i < tries; i++) {
			try {
				URLConnection con = new URL(url).openConnection();
				con.setConnectTimeout(timeout);
				InputStream in = con.getInputStream();
				String page = Utils.toString(in);
				return page;
			} catch (Exception e) {
				e.printStackTrace();
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		return null;
	}
	
}
/*

what is the right formula to determine the difficulty of a level?
- area, or maybe number of floor blocks
- number of moves

So, floor/moves, and the larger the number, the easier the level

then, how does popularity play in?  Just because it's easy doesn't mean it's good
- already added popularity by doing top X levels
- however, i treated all collections equally.  better way would have been to find top X levels regardless of what catalog
- So, I could go through every single level, and then simply take the top 500 or 1000 (becuase I've shrunk the database so much, I could do that) in terms of popularity

But how do I organize the levels?
- I want to reorganize them and sort them by difficulty, but in theory that's already been done
- I also think I want to redo the max size of the mazes - they're still pretty big.????
- So, probably instead of sorting collections alphabetically, sort them by difficulty


New ideas for ranking things
what do we have to work with?
- floor
- turtles
- moves
- popularity

Ideas
- rank it as follows
--- number of turtles
--- floor space
- Make completely new collections
-- name it simply "1 Turtle", "2 Turtles", etc...
-- name levels "collection: level name"
-- this idea would cause me to need to update the code for compression, or probably easier would be to copy files over to new names

- popularity ideas
--- we still want 2000 levels
--- but delete any collection that has fewer than (5) 
--- then optionally grab some more levels after that, to make up for it

*/