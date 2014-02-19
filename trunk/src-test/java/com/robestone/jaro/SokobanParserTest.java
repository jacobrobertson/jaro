package com.robestone.jaro;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import junit.framework.TestCase;

import com.robestone.jaro.levels.Level;
import com.robestone.jaro.levels.SokobanLevelParserHelper;
import com.robestone.jaro.piecerules.JaroPieceRules;

public class SokobanParserTest extends TestCase {

	private List<PieceRules> rulesList = JaroPieceRules.getPieceRules();
	
	public void testToTokens() {
		doTestToTokens(
				"2_10v5f_Ja", 
				"[[_, _, v, v, v, v, v, v, v, v, v, v, f, f, f, f, f, _, J, a]]");
		doTestToTokens(
				"2_10v5f_Jaaaa\n" + 
				"23j", 
				"[[_, _, v, v, v, v, v, v, v, v, v, v, f, f, f, f, f, _, J, a, a, a, a], " +
				"[j, j, j, j, j, j, j, j, j, j, j, j, j, j, j, j, j, j, j, j, j, j, j]]");
	}
	public void doTestToTokens(String file, String expect) {
		SokobanLevelParserHelper parser = new SokobanLevelParserHelper(true);
		List<List<String>> tokens = parser.toTokensForSplitData(file);
		String got = tokens.toString();
		assertEquals(expect, got);
	}
	public void testParseGrid() {
		doSmokeTestParseGrid("^", 1, 1);
		doSmokeTestParseGrid("_", 1, 1);
		doSmokeTestParseGrid("w", 1, 1);
		doSmokeTestParseGrid("f", 1, 1);
		doSmokeTestParseGrid("a", 1, 1);
		doSmokeTestParseGrid("{", 1, 1);
		doSmokeTestParseGrid("j", 1, 1);
		doSmokeTestParseGrid("J", 1, 1);
		doSmokeTestParseGrid( 
				"2_4w2_\n" + 
				"3w2fw2_\n" + 
				"wfa^a3w\n" + 
				"wf<j<2fw\n" + 
				"2wa<a2fw\n" + 
				"_w2f4w\n" + 
				"_2w2fw2_\n" + 
				"2_w2fw2_\n" + 
				"2_w2fw2_\n" + 
				"2_4w2_", 8, 10);
		doSmokeTestParseGrid(
			"18w\n" + 
			"w5fwf5a5w\n" + 
			"w2f2>fwfa3wa5w\n" + 
			"w2f>2f2wa3wa5w\n" + 
			"2w<4wfaf2wa5w\n" + 
			"w2fw9a5w\n" + 
			"w2fwafwfavfwv5w\n" + 
			"w2fwa3wavfwfw2f2w\n" + 
			"2w>wa3wa3wfw<f2w\n" + 
			"w2fw5afw2fw2f2w\n" + 
			"w2fw2f2w2fwf2w2f2w\n" + 
			"w2f4w9f2w\n" + 
			"2w<^2f^f2^>f2<>^2w\n" + 
			"w2fvf2^f<f>^vf>f2w\n" + 
			"w2f^2f>f<5fvfjw\n" + 
			"2w5f5w5fw\n" + 
			"18w", 18, 17);
	}
	public void testParseGrid2() {
		doSmokeTestParseGrid(
				"12w\n" + 
				"8wfj2w\n" + 
				"5w4f3w\n" + 
				"4wf2>f4w\n" + 
				"4wfwf2w2fw\n" + 
				"4wfwfA3fw\n" + 
				"4wf<fawafw\n" + 
				"2w4fafwf2w\n" + 
				"wf2<f>vaw>2w\n" + 
				"wfw2fw2faf2w\n" + 
				"wfwfa3f4w\n" + 
				"wf}fafaf4w\n" + 
				"wfwafwf5w\n" + 
				"wf><fw>5w\n" + 
				"wfw2faf5w\n" + 
				"wf2wa7w\n" + 
				"w4f7w\n" + 
				"12w", 12, 18);
	}
	public void testParseGrid4() {
		doSmokeTestParseGrid(
				"2_8w\r\n" + 
				"3w6fw\r\n" + 
				"wf<vawa>fw\r\n" + 
				"w2fava^afw\r\n" + 
				"wf<fa>a>2w\r\n" + 
				"wf>f>v<afw\r\n" + 
				"wf<vawf^fw\r\n" + 
				"2w6afw\r\n" + 
				"2_2wj4fw\r\n" + 
				"3_7w\r\n" + 
				"", 10, 10);
	}
	public void testParseGrid3() throws Exception {
		doTestParseFile("bugs_soko.txt", 4, 4);
	}
	public void testParseGrid5() throws Exception {
		doTestParseFile("soko_ban_1.txt", 4, 5);
	}
	public void doTestParseFile(String fileName, int col, int row) throws Exception {
		// res/raw/bugs_soko.txt
		File file = new File("src-test/resources/" + fileName);
		StringBuilder buf = new StringBuilder();
		FileInputStream in = new FileInputStream(file);
		int r;
		while ((r = in.read()) >= 0) {
			buf.append((char) r);
		}
		in.close();
		doSmokeTestParseGrid(buf.toString(), col, row);
	}
	public void testParseAllGrids() {
		String smax = "111111111";
		int imax = Integer.parseInt(smax, 2);
		for (int i = 0; i < imax; i++) {
			String bs = Integer.toBinaryString(i);
			while (bs.length() < 9) {
				bs = "0" + bs;
			}
			bs = bs.replaceAll("1", "w");
			bs = bs.replaceAll("0", "_");
			String grid = bs.substring(0, 3) + "\n" + bs.charAt(3) + "w" + bs.charAt(5) + "\n" + bs.substring(6);
			doSmokeTestParseGrid(grid, 3, 3);
		}
	}
	
	public void doSmokeTestParseGrid(String data, int expectCols, int expectRows) {
		SokobanLevelParserHelper parser = new SokobanLevelParserHelper(true);
		Grid g = parser.parseGrid(data, null);
		int c = g.getColumns();
		int r = g.getRows();
		assertEquals(expectRows, r);
		assertEquals(expectCols, c);
		for (int i = 0; i < r; i++) {
			for (int j = 0; j < c; j++) {
				Piece p = g.getPiece(j, i);
				if (p != null) {
					String id = null;
					for (PieceRules rules: rulesList) {
						id = rules.getSpriteId(p, false);
						if (id != null) {
							break;
						}
					}
					assertNotNull(p.toString(), id);
				}
			}
		}
		outputGrid(g);
	}
	public void testParseSingleLineData() {
		String data = "2_10w3_w8fw3_wf2wf2w2fw_3w8fw_w3ftwawtwfw_wftwfaJa3f3w3ftwawt3f2w4faTafwtf2w2fwtwawt3f3w8f3w_w2f2wf2wfw3_w8fw3_10w2_";
		SokobanLevelParserHelper parser = new SokobanLevelParserHelper(false);
		Level level = new Level("foo", "bar", "sk", "html");
		level.setCols(14);
		level.setRows(12);
		Grid grid = parser.parseGrid(data, level);
		System.out.println(grid);
		assertNotNull(grid);
	}
	public static void outputGrid(Grid g) {
		int width = 40;
		int c = g.getColumns();
		int r = g.getRows();
		for (int i = 0; i < r; i++) {
			for (int j = 0; j < c; j++) {
				Piece p = g.getPiece(j, i);
				String space;
				if (p == null) {
					space = "______";
				} else {
					space = p.toString();
				}
				while (space.length() < width) {
					space = " " + space;
				}
				System.out.print(space);
			}
			System.out.println();
		}
		System.out.println("=============================================================");
	}

}
