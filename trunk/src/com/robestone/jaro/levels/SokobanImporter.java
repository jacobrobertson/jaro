package com.robestone.jaro.levels;

import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		int start = 1;
		int end = 20000;
		for (int i = start; i < end; i++) {
			try {
				System.out.println("Level Id: " + i);
				new SokobanImporter().saveLevel(i);
			} catch (Exception e) {
				
			}
		}
	}
	
	// http://www.game-sokoban.com/index.php?mode=level&lid=349
	private String baseUrl = "http://www.game-sokoban.com/index.php?mode=level&lid=";
	
	public void saveLevel(int id) throws Exception {
		String page = downloadPage(id);
		toCleanFile(page);
		// save to file
	}
	
	public String downloadPage(int id) throws Exception {
		String levelUrl = baseUrl + id;
		URLConnection con = new URL(levelUrl).openConnection();
		InputStream in = con.getInputStream();
		return Utils.toString(in);
	}
	
	public void toCleanFile(String page) throws Exception {
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
			String name = getAttribute("name", page);
			String levelNumber = getAttribute("number", page);
			
			System.out.println("==================[" + cname + ":" + name + ":" + levelNumber + "]====================");
			for (List<String> tokens: gridTokens) {
				System.out.println(tokens);
			}

			output(gridTokens, cname, name, levelNumber);
			
		} else {
			throw new IllegalArgumentException("Could not parse page");
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
	private void output(List<List<String>> grid, String cname, String name, String levelNumber) throws Exception {
		File levelsDir = new File("C:\\Users\\Jacob\\eclipse-workspace-silver\\jaro\\res\\raw\\levels\\jarobanimports");
		cname = cname.replace(' ', '_');
		name = name.replace(' ', '_');
		File cnameDir = new File(levelsDir, cname);
		cnameDir.mkdirs();
		
		while (levelNumber.length() < 3) {
			levelNumber = "0" + levelNumber;
		}
		
		File file = new File(cnameDir, "l_" + levelNumber + "." + name + ".txt");
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
		Pattern p = Pattern.compile("<l\\s+.*?\\s" + name + "=\"([\\w\\s]+)\"");
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
	public List<String> rowToTokens(String row) {
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
	public void popularityParser() throws Exception {
		Pattern pattern = Pattern.compile("popular_item_.\" width=\"[0-9]+\" src=\"./pics/thumb_([0-9]+).gif");
		// there are holes in the catalog
		for (int i = 0; i < 142; i++) {
			// download the catalog - it might be blank
			String url = "http://www.game-sokoban.com/index.php?mode=catalog&cid=" + i;
			URLConnection con = new URL(url).openConnection();
			InputStream in = con.getInputStream();
			String page = Utils.toString(in);
			Matcher m = pattern.matcher(page);
			while (m.find()) {
				String id = m.group(1);
				System.out.println(id);
			}
		}
	}
	
}
