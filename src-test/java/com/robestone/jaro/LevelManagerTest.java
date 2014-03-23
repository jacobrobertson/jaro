package com.robestone.jaro;

import java.util.List;

import junit.framework.TestCase;

import com.robestone.jaro.android.HtmlResources;
import com.robestone.jaro.levels.JaroResources;
import com.robestone.jaro.levels.Level;
import com.robestone.jaro.levels.LevelManager;
import com.robestone.jaro.levels.Stage;

public class LevelManagerTest extends TestCase {

	public void testFlow() {
		
		// loading a blank level manager, we see we go straight to stage 1, level 1
		JaroResources resources = new HtmlResources(new JaroFileAssets("src-test/resources"), new LevelPersisterMock());
		JaroGame game = new JaroGame(new JaroModel(resources), new JaroView(), new JaroController(SoundPlayerMock.INSTANCE), new LevelPersisterMock(), resources);
		LevelManager levelManager = game.getModel().getLevelManager();
		
		Level level = levelManager.getCurrentLevel();
		assertNotNull(level);
		assertEquals("004.test-level-1.html", level.getLevelKey());
		
		// mark that level as passed
		levelManager.passCurrentLevel();
		
		// see we advance correctly
		// advance within a stage
		// advance between stages
		level = levelManager.getCurrentLevel();
		assertEquals("001.spider-level1.html", level.getLevelKey());
		
		// persist, destroy, bring back up again

		// show we're still on the correct level
		
		// advance and show we can complete all stages and levels
		// -- need some sort of indicator that we're done so we know what to do next
		
	}
	
	public void testParseStages() {
		JaroResources resources = new HtmlResources(new JaroFileAssets("src-test/resources"), new LevelPersisterMock());
		
		assertEquals("Bugs and Bushes", resources.getStage(0).getCaption());
		assertEquals("001.Bugs_and_Bushes", resources.getStage(0).getStageKey());
		assertEquals("001.bb-1.html", resources.getLevel(resources.getStage(0).getStageKey(), 0).getLevelKey());
		
		assertEquals(3, resources.getStagesCount());
		assertEquals(4, resources.getLevelsCount(resources.getStage(0).getStageKey()));
		
		Level level = resources.getLevel(resources.getStage(0).getStageKey(), 3);
		Grid grid = resources.getGrid(level);
		Piece p = grid.getPiece(3, 1); // cave_top_left
		assertEquals("cave", p.getType());
		assertEquals(null, p.getState());
		assertEquals("cave_top_left", p.getSubType());
		
		Piece j = grid.getPiece(4, 2);
		assertEquals("jaro", j.getType());
		
		Piece n = grid.getPiece(4, 3);
		assertNull(n);
		
		// smoke test to parse turtles
		Level levelT = resources.getLevel(resources.getStage(2).getStageKey(), 0);
		Grid gridT = resources.getGrid(levelT);
		assertNotNull(gridT);
	}
	public void testParseMultiplePiecesOnOneSquare() {
		JaroResources resources = new HtmlResources(new JaroFileAssets("src-test/resources"), new LevelPersisterMock());
		Level level = resources.getLevel(resources.getStage(2).getStageKey(), 1);
		assertEquals("002.snakoban2.html", level.getLevelKey());
		Grid grid = resources.getGrid(level);
		List<Piece> pieces = grid.getPieces(4, 5);
		assertEquals(2, pieces.size());
	}
	
	public void testUnlockedBehavior() {
		JaroResources resources = new HtmlResources(new JaroFileAssets("src-test/resources"), new LevelPersisterMock());
		JaroGame game = new JaroGame(new JaroModel(resources), new JaroView(), new JaroController(SoundPlayerMock.INSTANCE), new LevelPersisterMock(), resources);
		LevelManager levelManager = game.getModel().getLevelManager();

		Level l1_1 = levelManager.getLevel("001.bb-1.html");
		Level l1_2 = levelManager.getLevel("002.bb-2.html");
		Level l1_3 = levelManager.getLevel("003.bb-3.html");
		Level l1_4 = levelManager.getLevel("004.test-level-1.html");
		Level l2_1 = levelManager.getLevel("001.spider-level1.html");

		assertTrue(l1_1.isUnlocked());
		assertFalse(l1_1.isPassed());

		assertFalse(l1_2.isUnlocked());
		assertFalse(l1_2.isPassed());

		Stage s1 = resources.getStage(0);
		assertTrue(s1.isUnlocked());
		assertFalse(s1.isWorkedOn());
		assertFalse(s1.isPassed());

		Stage s2 = resources.getStage(1);
		assertFalse(s2.isUnlocked());
		assertFalse(s2.isWorkedOn());
		assertFalse(s2.isPassed());
		
		levelManager.setCurrentLevel(l1_1);
		levelManager.passCurrentLevel();
		assertTrue(l1_1.isPassed());

		assertTrue(s1.isWorkedOn());
		assertFalse(s1.isPassed());
		assertFalse(s2.isWorkedOn());
		assertFalse(s2.isUnlocked());
		
		assertFalse(l2_1.isUnlocked());
		levelManager.setCurrentLevel(l1_2);
		levelManager.passCurrentLevel();
		assertFalse(l2_1.isUnlocked());
		assertFalse(s2.isUnlocked());
		levelManager.setCurrentLevel(l1_3);
		levelManager.passCurrentLevel();
		assertTrue(s2.isUnlocked());
		assertTrue(l2_1.isUnlocked());
		
		levelManager.setCurrentLevel(l1_4);
		levelManager.passCurrentLevel();
		assertTrue(s1.isPassed());
	}
	
}
