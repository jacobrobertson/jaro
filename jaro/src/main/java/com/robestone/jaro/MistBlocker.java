package com.robestone.jaro;

import com.robestone.gamebase.BlockerHelper;
import com.robestone.gamebase.Piece;

public class MistBlocker implements BlockerHelper {

	@Override
	public boolean isLegal(Piece toCheck, Piece sprite) {
		// is this checking the mist?
		if (!JaroConstants.mist.equals(toCheck.getType())) {
			return true;
		}
		
		// only green jaro can pass!
		return JaroConstants.green_power.equals(sprite.getState());
	}

}
