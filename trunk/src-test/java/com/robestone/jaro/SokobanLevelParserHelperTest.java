package com.robestone.jaro;

import junit.framework.TestCase;

import com.robestone.jaro.levels.Utils;

public class SokobanLevelParserHelperTest extends TestCase {

	public void testCleanNames() {
		doTestCleanName("", "");
		doTestCleanName("aaa", "aaa");
		doTestCleanName(".", "[46]");
		doTestCleanName("?", "[63]");
		doTestCleanName("a?z", "a[63]z");
		doTestCleanName(".a?z/", "[46]a[63]z[47]");
	}
	
	private void doTestCleanName(String name, String expect) {
		String clean = Utils.toCleanName(name);
		assertEquals(expect, clean);
		
		String parsed = Utils.parseName(expect);
		assertEquals(name, parsed);
	}
}
