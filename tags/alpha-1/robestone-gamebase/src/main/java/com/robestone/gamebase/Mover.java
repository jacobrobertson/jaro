package com.robestone.gamebase;

import java.util.List;


public class Mover implements Action {

	private Mover() {
	}
	
	public static Mover move(int pieces, Area from, Area to) {
		Mover mover = new Mover();
		mover.count = pieces;
		mover.from = from;
		mover.to = to;
		return mover;
	}
	public static Mover moveOne(Area from, Area to) {
		return move(1, from, to);
	}
	public static Mover moveOneFlipped(Area from, Area to) {
		return moveOneAtATimeFlipped(1, from, to);
	}
	public static Mover moveOneAtATimeFlipped(int pieces, Area from, Area to) {
		return moveOneAtATimeFlipped(pieces, from, to, false);
	}
	public static Mover moveOneAtATime(int pieces, Area from, Area to) {
		return moveOneAtATime(pieces, from, to, false, false);
	}
	public static Mover moveOneAtATime(int pieces, Area from, Area to, boolean allowFewer) {
		return moveOneAtATime(pieces, from, to, allowFewer, false);
	}
	public static Mover moveOneAtATimeFlipped(int pieces, Area from, Area to, boolean allowFewer) {
		return moveOneAtATime(pieces, from, to, allowFewer, true);
	}
	public static Mover moveOneAtATime(int pieces, Area from, Area to, boolean allowFewer, boolean flipped) {
		Mover mover = move(pieces, from, to);
		mover.allowFewer = true;
		mover.flip = flipped;
		mover.oneAtATime = true;
		return mover;
	}
	public static Mover moveAllOneAtATimeFlipped(Area from, Area to) {
		return moveOneAtATimeFlipped(Integer.MAX_VALUE, from, to, true);
	}
	public static Mover moveThisPiece(Piece piece, Area from, Area to) {
		Mover m = moveOne(from, to);
		m.pieceToMove = piece;
		return m;
	}
	
	private Piece pieceToMove;
	
	private int count;
	private Area from;
	private Area to;
	
	private boolean oneAtATime = false;
	private boolean flip = false;
	private boolean allowFewer = false;
	private boolean clearSelection = true;
	
	private boolean showOnlyTop = true;
	private boolean showNewFaceUp = true;
	private boolean hideCoveredPieces = true;
	private boolean showUncoveredPiece = true;
	
	@Override
	public boolean run() {
		int toMove = count;
		List<Piece> fromPieces = from.getPieces();
		if (toMove > fromPieces.size()) {
			if (!allowFewer) {
				return false;
			}
			toMove = fromPieces.size();
		}
		if (!to.isEmpty() && (hideCoveredPieces || showOnlyTop)) {
			for (Piece p: to) {
				p.setVisible(false);
			}
		}
		for (int i = 0; i < toMove; i++) {
			int insertAt;
			if (oneAtATime) {
				insertAt = 0;
			} else {
				insertAt = i;
			}
			Piece p;
			if (pieceToMove == null) {
				p = fromPieces.remove(0);
			} else {
				fromPieces.remove(pieceToMove);
				p = pieceToMove;
			}
			if (flip) {
				p.flip();
			}
			boolean isNewTop;
			if (oneAtATime) {
				isNewTop = (i == toMove - 1);
			} else {
				isNewTop = (i == 0);
			}
			if (p.isFaceUp() && showNewFaceUp) {
				if (isNewTop || !showOnlyTop) {
					p.setVisible(true);
				}
			}
			if (!isNewTop && showOnlyTop) {
				p.setVisible(false);
			}
			if (isNewTop && showOnlyTop) {
				p.setVisible(true);
			}
			to.getPieces().add(insertAt, p);
			if (clearSelection) {
				p.setSelected(false);
			}
		}
		if (showUncoveredPiece && !fromPieces.isEmpty()) {
			fromPieces.get(0).setVisible(true);
		}
		
		return true;
	}
	public int getCount() {
		return count;
	}
	public Area getFrom() {
		return from;
	}
	public Area getTo() {
		return to;
	}
	public boolean isOneAtATime() {
		return oneAtATime;
	}
	public boolean isFlip() {
		return flip;
	}

	public void setShowNewFaceUp(boolean showNewFaceUp) {
		this.showNewFaceUp = showNewFaceUp;
	}

	public void setShowOnlyTop(boolean showOnlyTop) {
		this.showOnlyTop = showOnlyTop;
	}

	public void setShowUncoveredPiece(boolean showUncoveredPiece) {
		this.showUncoveredPiece = showUncoveredPiece;
	}

	public void setHideCoveredPieces(boolean hideCoveredPieces) {
		this.hideCoveredPieces = hideCoveredPieces;
	}
}
