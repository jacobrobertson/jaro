package com.robestone.jaro;

import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;

import com.robestone.jaro.example.ExampleGame;

import junit.framework.TestCase;

public class HtmlLevelDataProviderTest extends TestCase {

	public void test() throws Exception {
		InputStream in = new FileInputStream("C:\\Users\\jacob\\workspace\\jaro-android\\res\\raw\\levels1.html");
		HtmlLevelDataProvider provider = new HtmlLevelDataProvider();
		provider.load(in);
		in = new FileInputStream("C:\\Users\\jacob\\workspace-helios\\jaro-android\\res\\raw\\levels2.html");
		provider.load(in);
		LevelData d1 = provider.getLevelData(0);
		
		String[] split = d1.getData().split("\\|");
		assertEquals("cave_solid", split[12]);
		
		doTest(provider, 0, false);
		doTest(provider, 0, true);
		doTest(provider, 1, false);
		doTest(provider, 1, true);
		doTest(provider, 2, false);
		doTest(provider, 2, true);
	}
	private void doTest(HtmlLevelDataProvider provider, int index, boolean landscape) {
		new ExampleGame(provider.getLevelData(index), landscape);
	}
	public void testSmoke() throws IOException {
		doTestSmoke(1);
		doTestSmoke(2);
		doTestSmoke(3);
		doTestSmoke(4);
		doTestSmoke(5);
	}
	private void doTestSmoke(int i) throws IOException {
		InputStream in = new FileInputStream("C:\\Users\\jacob\\workspace-helios\\jaro-android\\res\\raw\\levels" + i + ".html");
		HtmlLevelDataProvider provider = new HtmlLevelDataProvider();
		provider.load(in);
		
		doTest(provider, 0, false);
		doTest(provider, 0, true);
	}
	
}
