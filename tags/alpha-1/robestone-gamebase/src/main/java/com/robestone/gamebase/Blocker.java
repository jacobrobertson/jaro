package com.robestone.gamebase;

import java.util.ArrayList;
import java.util.List;


/**
 * Knows which piece types are "blockers"
 * @author jacob
 */
public class Blocker implements ActionRules {

	private class BlockerItem implements BlockerHelper {
		public Object state;
		public Object typeId;
		@Override
		public boolean isLegal(Piece toCheck, Piece sprite) {
			Object typeId = toCheck.getType();
			Object state = toCheck.getState();
			// check on type matches
			if (this.typeId != null && this.typeId.equals(typeId)) {
				if (this.state == null) {
					// we don't care about state
					return false;
				} else if (this.state.equals(state)) {
					// both state and type match
					return false;
				}
			}
			
			// match on "any type, this state" type matches
			if (this.typeId == null) {
				if (this.state != null && !this.state.equals(state)) {
					return false;
				}
			}
			return true;
		}
	}
	
	private List<BlockerHelper> blockers = new ArrayList<BlockerHelper>();
	
	@Override
	public boolean isLegal(Action action, Game game) {
		
		if (!(action instanceof Mover)) {
			return true;
		}
		Mover mover = (Mover) action;
		
		// are we trying to move onto a blocker type?
		Piece check = mover.getTo().getTopPiece();
		if (check == null) {
			return true;
		}
		
		Piece sprite = ((SpriteAwareGame) game).getMainSprite();
		for (BlockerHelper b: blockers) {
			if (!b.isLegal(check, sprite)) {
				return false;
			}
		}
		
		return true;
	}
	
	public void addBlocker(Object typeId) {
		addBlocker(typeId, null);
	}
	public void addBlocker(BlockerHelper helper) {
		blockers.add(helper);
	}
	public void addBlocker(Object typeId, Object state) {
		BlockerItem b = new BlockerItem();
		b.typeId = typeId;
		b.state = state;
		blockers.add(b);
	}

}
