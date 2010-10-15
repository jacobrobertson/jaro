package com.robestone.gamebase;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the idea that pieces with different states have different ways to render.
 * @author jacob
 */
public class SpriteMapper {
	
	private class Match {
//		Object pieceKey;
		Object typeId;
		Object state;
		String spriteId;
		String spriteIdLandscape;
		
		public Match(Object pieceKey, Object typeId, Object state,
				String spriteId, String spriteIdLandscape) {
//			this.pieceKey = pieceKey;
			this.typeId = typeId;
			this.state = state;
			this.spriteId = spriteId;
			this.spriteIdLandscape = spriteIdLandscape;
		}

		public int matches(Piece p) {
			int match = 0;
//			if (pieceKey != null) {
//				if (!pieceKey.equals(p.getKey())) {
//					return 0;
//				} else {
//					match++;
//				}
//			}
			if (typeId != null) {
				if (!typeId.equals(p.getType()) && !typeId.equals(p.getSubType())) {
					return 0;
				} else {
					match++;
				}
			}
			if (state != null) {
				if (!state.equals(p.getState())) {
					return 0;
				} else {
					match++;
				}
			}
			return match;
		}
	}
	
	private List<Match> matches = new ArrayList<Match>();
	
	public String getSpriteKey(Piece piece, boolean landscape) {
		int bestScore = 0;
		Match bestMatch = null;
		for (Match match: matches) {
			int score = match.matches(piece);
			if (score > bestScore) {
				bestScore = score;
				bestMatch = match;
			}
		}
		if (bestMatch != null) {
			if (landscape) {
				return bestMatch.spriteIdLandscape;
			} else {
				return bestMatch.spriteId;
			}
		} else {
			throw new IllegalArgumentException("Could not find a matching sprite for " + piece.getType() + "/" + piece.getState());
		}
	}
	// TODO get rid of piece key - this is extra code that's unused
	private void addMatch(Object pieceKey, Object typeId, Object state,
			String spriteId, String spriteIdLandscape) {
		if (spriteIdLandscape == null) {
			spriteIdLandscape = spriteId;
		}
		Match m = new Match(pieceKey, typeId, state, spriteId, spriteIdLandscape);
		matches.add(m);
	}
//	public void addMatchForKey(Object pieceKey, String spriteId, String spriteIdLandscape) {
//		addMatch(pieceKey, null, null, spriteId, spriteIdLandscape);
//	}
//	public void addMatchForKey(Object pieceKey, String spriteId) {
//		addMatch(pieceKey, null, null, spriteId, null);
//	}
	public void addMatchForType(Object typeId, String spriteId) {
		addMatch(null, typeId, null, spriteId, null);
	}
	public void addMatchForType(Object typeId, String spriteId, String spriteIdLandscape) {
		addMatch(null, typeId, null, spriteId, spriteIdLandscape);
	}
	public void addMatchForTypeAndState(Object typeId, Object state, String spriteId) {
		addMatch(null, typeId, state, spriteId, null);
	}

}
