package com.robestone.jaro;

import java.io.InputStream;
import java.util.List;

import junit.framework.TestCase;

import com.robestone.jaro.levels.InputStreamBuilder;
import com.robestone.jaro.levels.Level;
import com.robestone.jaro.levels.LevelManager;
import com.robestone.jaro.levels.LevelParser;
import com.robestone.jaro.levels.LevelPersister;
import com.robestone.jaro.levels.Stage;

public class LevelManagerTest extends TestCase implements InputStreamBuilder, LevelPersister {

	private String currentLevel = "test-level-1";
	
	public void testFlow() {
		
		// loading a blank level manager, we see we go straight to stage 1, level 1
		JaroGame game = new JaroGame(new JaroModel(), new JaroView(), new JaroController(), this, this);
		LevelManager levelManager = game.getModel().getLevelManager();
		
		Level level = levelManager.getCurrentLevel();
		assertNotNull(level);
		assertEquals("test-level-1", level.getLevelKey());
		
		// mark that level as passed
		levelManager.passCurrentLevel();
		
		// see we advance correctly
		// advance within a stage
		// advance between stages
		level = levelManager.getCurrentLevel();
		assertEquals("spider-level1", level.getLevelKey());
		
		// persist, destroy, bring back up again

		// show we're still on the correct level
		
		// advance and show we can complete all stages and levels
		// -- need some sort of indicator that we're done so we know what to do next
		
	}
	
	public void testParseStages() {
		LevelParser lp = new LevelParser(new JaroController().getPieceRules());
		LevelManager lm = new LevelManager(this, lp, this);
		List<Stage> stages = lm.getStages();
		
		assertEquals("Bugs and Bushes", stages.get(0).getCaption());
		assertEquals("bb", stages.get(0).getStageKey());
		assertEquals("bb-1", stages.get(0).getLevels().get(0).getLevelKey());
		
		assertEquals(3, stages.size());
		assertEquals(2, stages.get(1).getLevels().size());
		
		Level level = stages.get(0).getLevels().get(3);
		Grid grid = lm.getGrid(level);
		Piece p = grid.getPiece(3, 1); // cave_top_left
		assertEquals("cave", p.getType());
		assertEquals(null, p.getState());
		assertEquals("cave_top_left", p.getSubType());
		
		Piece j = grid.getPiece(4, 2);
		assertEquals("jaro", j.getType());
		
		Piece n = grid.getPiece(4, 3);
		assertNull(n);
		
		// smoke test to parse turtles
		Level levelT = stages.get(2).getLevels().get(0);
		Grid gridT = lm.getGrid(levelT);
		assertNotNull(gridT);
	}
	public void testParseMultiplePiecesOnOneSquare() {
		LevelParser lp = new LevelParser(new JaroController().getPieceRules());
		LevelManager lm = new LevelManager(this, lp, this);
		List<Stage> stages = lm.getStages();
		
		Level level = stages.get(2).getLevels().get(1);
		Grid grid = lm.getGrid(level);
		List<Piece> pieces = grid.getPieces(4, 5);
		assertEquals(2, pieces.size());
	}
	@Override
	public InputStream buildLevelInputStream(String levelKey) {
		return ClassLoader.getSystemResourceAsStream(levelKey + ".html");
	}
	@Override
	public InputStream buildStagesInputStream() {
		return ClassLoader.getSystemResourceAsStream("jaro-test-stages.txt");
	}
	@Override
	public String getCurrentLevel() {
		return currentLevel;
	}
	@Override
	public boolean isLevelUnlocked(String levelKey) {
		return false;
	}
	@Override
	public void setCurrentLevel(String levelKey) {
		currentLevel = levelKey;
	}

	@Override
	public void setLevelUnlocked(String levelKey) {
		
	}
	
}
