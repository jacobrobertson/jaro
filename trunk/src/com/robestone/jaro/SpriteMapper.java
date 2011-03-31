package com.robestone.jaro;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the idea that pieces with different states/orientations have different ways to render.
 * 
 * @author jacob
 */
public class SpriteMapper {
	
	private class Match {
		String type;
		String subType;
		Object state;
		String spriteId;
		String spriteIdLandscape;
		public Match(String type, String subType, Object state,
				String spriteId, String spriteIdLandscape) {
			this.type = type;
			this.subType = subType;
			this.state = state;
			this.spriteId = spriteId;
			this.spriteIdLandscape = spriteIdLandscape;
		}
		
		public boolean matches(Piece piece) {
			if (!type.equals(piece.getType())) {
				return false;
			}
			if (subType != null && !subType.equals(piece.getSubType())) {
				return false;
			}
			if (state != null && !state.equals(piece.getState())) {
				return false;
			}
			return true;
		}
	}
	
	private List<Match> matches = new ArrayList<Match>();
	
	public String getSpriteId(Piece piece, boolean landscape) {
		for (Match match: matches) {
			boolean matches = match.matches(piece);
			if (matches) {
				if (landscape) {
					return match.spriteIdLandscape;
				} else {
					return match.spriteId;
				}
			}
		}
		throw new IllegalArgumentException("Could not find a matching sprite for " + piece.getType() + "/" +  piece.getSubType() + "/" + piece.getState());
	}
	public void addMatch(String type, String subType, String spriteId, String spriteIdLandscape) {
		addMatch(type, subType, null, spriteId, spriteIdLandscape);
	}
	public void addMatch(String type, String subType, Object state, String spriteId, String spriteIdLandscape) {
		if (spriteIdLandscape == null) {
			spriteIdLandscape = spriteId;
		}
		Match m = new Match(type, subType, state, spriteId, spriteIdLandscape);
		matches.add(m);
	}

}
