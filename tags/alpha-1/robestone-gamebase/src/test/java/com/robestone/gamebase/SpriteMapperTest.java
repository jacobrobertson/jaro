package com.robestone.gamebase;

import junit.framework.TestCase;

public class SpriteMapperTest extends TestCase {

	/**
	 * Shows the order the maps are added shouldn't matter
	 */
	public void testOrder() {
		String spriteTested = "sprite_tested";
		String spriteAny = "sprite_any";
		
		String pieceId = "pieceId1";
		
		String stateTested = "tested";
		String stateBlank = "blank";
		
		SpriteMapper map = new SpriteMapper();
		
		// because of this order, it will check the more general rule first
		// but I still want it to choose the second rule when it's the best match
		map.addMatchForType(pieceId, spriteAny);
		map.addMatchForTypeAndState(pieceId, stateTested, spriteTested);

		Piece p = new PieceImpl(0, pieceId);
		p.setState(stateBlank);
		
		String foundBlank = map.getSpriteKey(p, false);
		assertEquals(spriteAny, foundBlank);
		
		p.setState(stateTested);
		String foundTested = map.getSpriteKey(p, false);
		assertEquals(spriteTested, foundTested);
	}
	
}
