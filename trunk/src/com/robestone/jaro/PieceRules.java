package com.robestone.jaro;

import java.util.List;

/**
 * Encapsulates (as much as possible) whatever there is to know about one particular type of piece.
 * In order to create new rules for Jaro, simply extend this class, and register it (TODO - how?).
 * @author jacob
 * 
 * TODO prefer to have one consistent object to pass in that has
 * - current/last action
 * - game (grid)
 * - game state info
 * - jaro himself
 * 
 * TODO things to add to the piece rule
 * - rendering rules - i.e. what is the "key" for given states
 * --- simple rendering rules, plus allow to override the rule with it's own behavior.  i.e. add itself to sprite map
 * - what is the key type id
 * - 
 */
public interface PieceRules {

	String getTypeId();
	String getSpriteId(Piece piece, boolean isLandscape);
	
	/**
	 * For this piece type only - does it consider the level complete?
	 * Other piece types may have a reason the level is not complete.
	 */
	boolean isOkayToEndLevel(JaroModel model);
	
	/**
	 * For this piece type only - does it consider this move legal?
	 * Other piece types may have a reason the move is not legal.
	 */
	boolean isLegal(Action proposedAction, JaroModel model);
	
	/**
	 * @param userAction This was the original action that caused the trigger of
	 * 			additional actions.  It will not be used most of the time.
	 */
	List<Action> getTriggeredActions(Action userAction, JaroModel model);
	
	/**
	 * Method called to help create a Grid from an html level definition file.
	 * @return null if it can't parse it
	 * @param parseKey from the HTML file that defines a level
	 */
	Piece parsePiece(String parseKey);
}
