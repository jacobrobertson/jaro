package com.robestone.jaro.piecerules;

import java.util.ArrayList;
import java.util.List;

import com.robestone.jaro.PieceRules;

/**
 * Add your rules here.
 * @author jacob
 */
public class Registry {

	public static List<PieceRules> getPieceRules() {
		List<PieceRules> pieceRules = new ArrayList<PieceRules>();
		pieceRules.add(new CaveRules());
		pieceRules.add(new JaroRules());
		pieceRules.add(new BugRules());
		pieceRules.add(new BoulderRules());
		pieceRules.add(new BushRules());
		pieceRules.add(new SpiderRules());
		pieceRules.add(new MistRules());
		pieceRules.add(new FrogRules());
		pieceRules.add(new TurtleRules());
		pieceRules.add(new TurtleHoleRules());
		pieceRules.add(new TurtleNestRules());
		return pieceRules;
	}
	
}
