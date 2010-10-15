package com.robestone.jaro;

import java.util.ArrayList;
import java.util.List;

import com.robestone.gamebase.ActionListener;
import com.robestone.gamebase.ActionRulesList;
import com.robestone.gamebase.Actor;
import com.robestone.gamebase.Area;
import com.robestone.gamebase.AreaImpl;
import com.robestone.gamebase.Blocker;
import com.robestone.gamebase.GameController;
import com.robestone.gamebase.GestureInterpretor;
import com.robestone.gamebase.Grid;
import com.robestone.gamebase.Piece;
import com.robestone.gamebase.PieceImpl;
import com.robestone.gamebase.SpriteMapper;

public class JaroGameBuilder {

	private Grid grid;
	private Piece jaro;
	private JaroGameState state;
	
	public JaroGameBuilder(Grid grid, Piece jaro, JaroGameState state) {
		this.grid = grid;
		this.jaro = jaro;
		this.state = state;
	}
	public static GestureInterpretor buildGestureInterpretor() {
		return new BasicGestureInterpretor();
	}

	private static ActionRulesList ActionRulesList;
	private static ActionRulesList buildActionRulesList() {
		if (ActionRulesList == null) {
			ActionRulesList = new ActionRulesList();
			ActionRulesList.add(buildBlocker());
			ActionRulesList.add(new TurtleBlocker());
		}
		return ActionRulesList;
	}
	
	public GameController buildGameController() {
		List<ActionListener> actionListeners = new ArrayList<ActionListener>();
		GameController controller = new GameController(
				buildGameFlowActors(), actionListeners, buildGestureInterpretor());
		
		controller.setActionChecker(buildActionRulesList());
		controller.getActionListeners().add(new JaroTracker(state));
		
		return controller;
	}
	
	private static List<Actor> gameFlowActors;
	public static List<Actor> buildGameFlowActors() {
		if (gameFlowActors == null) {
			gameFlowActors = new ArrayList<Actor>();
			gameFlowActors.add(new BushSwitcher());
			gameFlowActors.add(new BugEater());
			gameFlowActors.add(new FrogEater());
			gameFlowActors.add(new SpiderScarer());
			gameFlowActors.add(new TurtlePusher());
		}
		return gameFlowActors;
	}
	
	private static Area buttonsArea = buildButtonsArea();
	private static Area buildButtonsArea() {
		Area buttonsArea = new AreaImpl("buttons", "buttons");
		buttonsArea.getPieces().add(new PieceImpl(1, JaroConstants.up));
		buttonsArea.getPieces().add(new PieceImpl(2, JaroConstants.down));
		buttonsArea.getPieces().add(new PieceImpl(3, JaroConstants.left));
		buttonsArea.getPieces().add(new PieceImpl(4, JaroConstants.right));
		return buttonsArea;
	}
	public static List<Area> buildAreas() {
		List<Area> areas = new ArrayList<Area>();
		Area stomachArea = new AreaImpl(JaroConstants.stomach, JaroConstants.stomach);
		areas.add(stomachArea);
		areas.add(buttonsArea);
		return areas;
	}
	
	private static Blocker Blocker;
	public static Blocker buildBlocker() {
		if (Blocker == null) {
			Blocker blocker = new Blocker();
			blocker.addBlocker(JaroConstants.cave);
			blocker.addBlocker(JaroConstants.boulderId);
			blocker.addBlocker(JaroConstants.bushId, JaroConstants.bushClosed);
			blocker.addBlocker(JaroConstants.hole, JaroConstants.hole_empty);
			blocker.addBlocker(JaroConstants.spider, JaroConstants.spider_big);
			
			blocker.addBlocker(new MistBlocker());
			
			Blocker = blocker;
		}
		
		return Blocker;
	}
	private int getX(int i, boolean landscape) {
		return getX(i, landscape, grid.getColumns(), grid.getRows());
	}
	public static int getX(int i, boolean landscape, int columns, int rows) {
		int x;
		if (landscape) {
			x = i / rows;
		} else {
			x = i % columns;
		}
		return x;
	}
	private int getY(int i, boolean landscape) {
		return getY(i, landscape, grid.getColumns(), grid.getRows());
	}
	public static int getY(int i, boolean landscape, int columns, int rows) {
		int y;
		if (landscape) {
			y = rows - i % rows - 1;
		} else {
			y = i / columns;
		}
		return y;
	}
	public void addFromData(String d, boolean landscape) {
		int linearPosition = 0;
		for (int i = 0; i < d.length(); i++) {
			int x = getX(linearPosition, landscape);
			int y = getY(linearPosition, landscape);
			
			char c = d.charAt(i);
			if (c == '|') {
				int pos = d.indexOf('|', i + 1);
				String sub;
				if (pos < 0) {
					sub = d.substring(i + 1);
					i = d.length();
				} else {
					sub = d.substring(i + 1, pos);
					i = pos - 1;
				}
				addPieceFromKey(sub, x, y);
			} else {
				addPiece(c, x, y);
			}
			linearPosition++;
		}
	}
	private void addPiece(char c, int x, int y) {
		switch (c)	{
		case 'j': grid.addPiece(jaro, x, y); break;
		case 'n': addPieceWithState(x, y, JaroConstants.bushId, JaroConstants.bushOpen); break;
		case 'o': addPiece(x, y, JaroConstants.boulderId); break;
		case 'z': addPiece(x, y, JaroConstants.bugId); break;
		case '#': addPiece(x, y, JaroConstants.mist); break;
		case 'f': addPiece(x, y, JaroConstants.frog); break;
		case 's': addPieceWithState(x, y, JaroConstants.spider, JaroConstants.spider_big); break;
		
		case 'x': addCave(x, y, JaroConstants.cave_solid); break;
		case '_': addCave(x, y, JaroConstants.cave_bottom); break;
		case '-': addCave(x, y, JaroConstants.cave_top); break;
		case '[': addCave(x, y, JaroConstants.cave_left); break;
		case ']': addCave(x, y, JaroConstants.cave_right); break;
		case '.': addCave(x, y, JaroConstants.cave_bottom_left_bump); break;
		case '\'': addCave(x, y, JaroConstants.cave_top_left_bump); break;
		case ',': addCave(x, y, JaroConstants.cave_bottom_right_bump); break;
		case '`': addCave(x, y, JaroConstants.cave_top_right_bump); break;
		
		case ':': addCave(x, y, JaroConstants.cave_bottom_left); break;
		case '/': addCave(x, y, JaroConstants.cave_top_left); break;
		case ';': addCave(x, y, JaroConstants.cave_bottom_right); break;
		case '~': addCave(x, y, JaroConstants.cave_top_right); break;

		case 'L': addCave(x, y, JaroConstants.cave_bottom_left_L); break;
		case 'r': addCave(x, y, JaroConstants.cave_top_left_L); break;
		case 'J': addCave(x, y, JaroConstants.cave_bottom_right_L); break;
		case '7': addCave(x, y, JaroConstants.cave_top_right_L); break;
		
		case '<': addCave(x, y, JaroConstants.cave_wall_left_bump); break;
		case '>': addCave(x, y, JaroConstants.cave_wall_right_bump); break;
		case '^': addCave(x, y, JaroConstants.cave_wall_top_bump); break;
		case 'v': addCave(x, y, JaroConstants.cave_wall_bottom_bump); break;

		case '}': addCave(x, y, JaroConstants.cave_wall_vertical); break;
		case '=': addCave(x, y, JaroConstants.cave_wall_horizontal); break;

		case 't': addCave(x, y, JaroConstants.cave_t_right); break;
		case '%': addCave(x, y, JaroConstants.cave_t_bottom); break;
		case 'T': addCave(x, y, JaroConstants.cave_t_top); break;
		case '?': addCave(x, y, JaroConstants.cave_t_left); break;
		case '+': addCave(x, y, JaroConstants.cave_t_center); break;
		
		case ' ': break;
		
		default: throw new IllegalArgumentException("Unknown piece: " + c);
		}
	}
	private void addPieceFromKey(String key, int x, int y) {
		if (key.length() == 0) {
			// skip
		} else if (key.length() == 1) {
			addPiece(key.charAt(0), x, y);
		} else if (key.startsWith("cave_")) {
			addCave(x, y, key);
		} else if (key.equals("jaro")) {
			addPiece('j', x, y);
		} else if (key.startsWith("spider_")) {
			addPieceWithState(x, y, JaroConstants.spider, key);
		} else if (key.startsWith("bush_")) {
			addPieceWithState(x, y, JaroConstants.bushId, key);
		} else if (key.startsWith("turtle_")) {
			addPieceWithState(x, y, JaroConstants.turtle, key);
		} else if (key.equals("hole")) {
			addPieceWithState(x, y, JaroConstants.hole, JaroConstants.hole_empty);
		} else {
			addPiece(x, y, key);
		}
	}
	private int nextId = 5;
	private int nextId() {
		return nextId++;
	}
	public void addPiece(int x, int y, String id) {
		grid.addPiece(new PieceImpl(nextId(), id), x, y);
	}
	public void addPieceWithState(int x, int y, String type, String state) {
		Piece piece = new PieceImpl(nextId(), type);
		piece.setState(state);
		grid.addPiece(piece, x, y);
	}
	public void addCave(int x, int y, String subType) {
		grid.addPiece(new PieceImpl(nextId(), JaroConstants.cave, subType), x, y);
	}

	private static SpriteMapper SpriteMapper;
	public static SpriteMapper buildSpriteMapper() {
		if (SpriteMapper == null) {
			SpriteMapper map = new SpriteMapper();
			
			map.addMatchForTypeAndState(JaroConstants.jaroId, JaroConstants.green_power, "jaro_green");
			map.addMatchForType(JaroConstants.jaroId, "jaro");
			map.addMatchForType(JaroConstants.bugId, "bug");
			map.addMatchForType(JaroConstants.frog, "frog");
			map.addMatchForType(JaroConstants.mist, "green_mist");
			map.addMatchForType(JaroConstants.boulderId, "boulder");
			map.addMatchForTypeAndState(JaroConstants.bushId, JaroConstants.bushOpen, "bush_open");
			map.addMatchForTypeAndState(JaroConstants.bushId, JaroConstants.bushClosed, "bush_closed");
	
			map.addMatchForTypeAndState(JaroConstants.hole, JaroConstants.hole_empty, "hole");
			map.addMatchForTypeAndState(JaroConstants.hole, JaroConstants.hole_full, "hole_full");
	
			map.addMatchForTypeAndState(JaroConstants.spider, JaroConstants.spider_big, "spider_big");
			map.addMatchForTypeAndState(JaroConstants.spider, JaroConstants.spider_small, "spider_small");
	
			map.addMatchForTypeAndState(JaroConstants.turtle, JaroConstants.turtle_down, "turtle_down");
			map.addMatchForTypeAndState(JaroConstants.turtle, JaroConstants.turtle_up, "turtle_up");
			map.addMatchForTypeAndState(JaroConstants.turtle, JaroConstants.turtle_right, "turtle_right");
			map.addMatchForTypeAndState(JaroConstants.turtle, JaroConstants.turtle_left, "turtle_left");
	
			map.addMatchForType(JaroConstants.cave_bottom, "cave_bottom", "cave_right");
			map.addMatchForType(JaroConstants.cave_left, "cave_left", "cave_bottom");
			map.addMatchForType(JaroConstants.cave_right, "cave_right", "cave_top");
			map.addMatchForType(JaroConstants.cave_top, "cave_top", "cave_left");
			map.addMatchForType(JaroConstants.cave_solid, "cave_solid");
			
			map.addMatchForType(JaroConstants.cave_t_bottom, "cave_t_bottom", "cave_t_right");
			map.addMatchForType(JaroConstants.cave_t_left, "cave_t_left", "cave_t_bottom");
			map.addMatchForType(JaroConstants.cave_t_right, "cave_t_right", "cave_t_top");
			map.addMatchForType(JaroConstants.cave_t_top, "cave_t_top", "cave_t_left");
			map.addMatchForType(JaroConstants.cave_t_center, "cave_t_center");
	
			map.addMatchForType(JaroConstants.cave_wall_t_bottom, "cave_wall_t_bottom", "cave_wall_t_right");
			map.addMatchForType(JaroConstants.cave_wall_t_left, "cave_wall_t_left", "cave_wall_t_bottom");
			map.addMatchForType(JaroConstants.cave_wall_t_right, "cave_wall_t_right", "cave_wall_t_top");
			map.addMatchForType(JaroConstants.cave_wall_t_top, "cave_wall_t_top", "cave_wall_t_left");
	
			map.addMatchForType(JaroConstants.cave_bottom_left, "cave_bottom_left", "cave_bottom_right");
			map.addMatchForType(JaroConstants.cave_bottom_right, "cave_bottom_right", "cave_top_right");
			map.addMatchForType(JaroConstants.cave_top_left, "cave_top_left", "cave_bottom_left");
			map.addMatchForType(JaroConstants.cave_top_right, "cave_top_right", "cave_top_left");
			map.addMatchForType(JaroConstants.cave_bottom_left_bump, "cave_bottom_left_bump", "cave_bottom_right_bump");
			map.addMatchForType(JaroConstants.cave_bottom_right_bump, "cave_bottom_right_bump", "cave_top_right_bump");
			map.addMatchForType(JaroConstants.cave_top_left_bump, "cave_top_left_bump", "cave_bottom_left_bump");
			map.addMatchForType(JaroConstants.cave_top_right_bump, "cave_top_right_bump", "cave_top_left_bump");
	
			map.addMatchForType(JaroConstants.cave_bottom_left_L, "cave_bottom_left_l", "cave_bottom_right_l");
			map.addMatchForType(JaroConstants.cave_bottom_right_L, "cave_bottom_right_l", "cave_top_right_l");
			map.addMatchForType(JaroConstants.cave_top_left_L, "cave_top_left_l", "cave_bottom_left_l");
			map.addMatchForType(JaroConstants.cave_top_right_L, "cave_top_right_l", "cave_top_left_l");
	
			map.addMatchForType(JaroConstants.cave_wall_right_bump, "cave_wall_right_bump", "cave_wall_top_bump");
			map.addMatchForType(JaroConstants.cave_wall_left_bump, "cave_wall_left_bump", "cave_wall_bottom_bump");
			map.addMatchForType(JaroConstants.cave_wall_top_bump, "cave_wall_top_bump", "cave_wall_left_bump");
			map.addMatchForType(JaroConstants.cave_wall_bottom_bump, "cave_wall_bottom_bump", "cave_wall_right_bump");
	
			map.addMatchForType(JaroConstants.cave_wall_vertical, "cave_wall_vertical", "cave_wall_horizontal");
			map.addMatchForType(JaroConstants.cave_wall_horizontal, "cave_wall_horizontal", "cave_wall_vertical");
			SpriteMapper = map;
		}
		return SpriteMapper;
	}
}
