package com.robestone.jaro;

import java.io.File;
import java.io.FileInputStream;

import junit.framework.TestCase;

import com.robestone.jaro.android.HtmlResources;
import com.robestone.jaro.levels.JaroAssets;

public class HtmlParserTest extends TestCase {

	public void testParse1() throws Exception {
		File file = new File("src-test/resources/bugs_soko_html.html");
		doSmokeTestParse(file.getName(), file);
	}
	public void testParse() throws Exception {
		File dir = new File("res/raw");
		for (File file: dir.listFiles()) {
			if (file.getName().endsWith(".html")) {
				doSmokeTestParse(file.getName(), file);
			}
		}
	}
	private void doSmokeTestParse(String fileName, File file) throws Exception {
		StringBuilder buf = new StringBuilder();
		FileInputStream in = new FileInputStream(file);
		int r;
		while ((r = in.read()) >= 0) {
			buf.append((char) r);
		}
		in.close();
		JaroAssets assets = new JaroFileAssets("");
		HtmlResources parser = new HtmlResources(assets);
		Grid g = parser.parseGrid(buf.toString(), null);
		System.out.println(file);
		SokobanParserTest.outputGrid(g);
	}
	
}
