package com.robestone.jaro.android;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.robestone.jaro.levels.JaroAssets;
import com.robestone.jaro.levels.JaroResources;
import com.robestone.jaro.levels.Level;
import com.robestone.jaro.levels.LevelManager;
import com.robestone.jaro.levels.Stage;

public class JaroActivity extends Activity {

//	private boolean showAllLevels = true;
	
	private JaroAndroidGame game;

	public JaroAndroidGame getGame() {
		return game;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Object data = getLastNonConfigurationInstance();
		if (data instanceof JaroAndroidGame) {
			game = (JaroAndroidGame) data;
		} else {
			createGame();
		}
		if (game.isActive()) {
			setContentView(R.layout.main);
		} else {
			// need to check preferences
			// TODO might not be good to acquire like this?
			JaroPreferences prefs = new JaroPreferences(this);
			if (prefs.isEulaRead()) {
				showHome();
			} else {
				showAbout();
			}
		}
	}
	private void createGame() {
		JaroAssets assets = new JaroAndroidAssets(getAssets());
		JaroPreferences prefs = new JaroPreferences(this);
		JaroAndroidResources resources;
		String gameType = prefs.getGameType();
		if (HtmlResources.JARO_GAME_TYPE.equals(gameType)) {
			resources = new HtmlResources(assets);
		} else {
			resources = new DbResources(assets);
		}
		game = new JaroAndroidGame(this, resources);
	}
	void showAbout() {
		CharSequence aboutMessageText = getResources().getText(R.string.about);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(aboutMessageText);
		
		String closeAbout = getString(R.string.close_about);
		DialogInterface.OnClickListener closeAboutClick = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				JaroPreferences prefs = new JaroPreferences(JaroActivity.this);
		    	prefs.setEulaRead();
		    	showHome();
		    }
		};
		builder.setPositiveButton(closeAbout, closeAboutClick);

		AlertDialog dialog = builder.create();
		dialog.show();
	}
	void showHome() {
		// set up the home view
		setContentView(R.layout.home);
		View homeView = findViewById(R.id.home_image);
		homeView.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	startGame();
		    }
		});
	}
	void startGame() {
		setContentView(R.layout.main);
		game.setActive();
		game.getController().startAcceptingMoves();
	}

	@Override
	public void onBackPressed() {
		// TODO if on main menu, then quit
		//		should also handle game types

		// else, send the undo message
		game.getController().undoLastMove();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return game;
	}
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
		boolean okay = super.onPrepareOptionsMenu(menu);
		if (!okay) {
			return false;
		}
		final JaroPreferences prefs = new JaroPreferences(this);
		if (!prefs.isEulaRead()) {
			return false;
		}
		int id;
		if (game.isActive()) {
			id = R.menu.context_menu;
		} else {
			id = R.menu.context_menu_home;
		}
		menu.clear();
        getMenuInflater().inflate(id, menu);
        return true;
	}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.undo_button:
	        	game.getController().undoLastMove();
	        	return true;
	        case R.id.quit:
	        	finish();
	            return true;
	        case R.id.start_level_over:
	        	game.getController().startLevelOver();
	            return true;
	        case R.id.choose_level:
	        	startGame();
	        case R.id.choose_different_level:
	        	return showChooseStageMenu();
	        case R.id.choose_game_type:
	        	return showChooseGameMenu();
	        case R.id.zoom:
	        	game.getView().zoomIn();
	        	return true;
	        default:
	            return false;
        }
    }
    private boolean showChooseGameMenu() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String title = getString(R.string.choose_game);
    	builder.setTitle(title);
		final String[] gameTypes = {HtmlResources.JARO_GAME_TYPE, DbResources.JAROBAN_GAME_TYPE};
    	builder.setItems(gameTypes, new DialogInterface.OnClickListener() {
    	    public void onClick(DialogInterface dialog, int item) {
    	    	JaroPreferences prefs = new JaroPreferences(JaroActivity.this);
    	    	prefs.setGameType(gameTypes[item]);
    	    	createGame();
    	    	startGame();
    	    }
    	});
    	AlertDialog dialog = builder.create();
    	dialog.show();
    	return true;
    }
    private boolean showChooseStageMenu() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final JaroResources resources = game.getJaroResources();
		
		// build the captions for all unlocked stages
    	List<String> stageCaptions = new ArrayList<String>();
    	Stage stage1 = null;
		LevelManager levelManager = game.getModel().getLevelManager();
		boolean showAll = levelManager.isShowAllLevels();
		
		// TODO push as much logic into LevelManager - "getUnlockedStages" is a starting point 
    	for (Stage stage: game.getJaroResources().getStages()) {
    		if (stage1 == null) {
    			stage1 = stage;
    		}
    		boolean stageUnlocked = showAll || levelManager.isStageUnlocked(stage);
    		if (!stageUnlocked) {
    			break;
    		}
    		String caption = stage.getCaption();
    		if (levelManager.isStagePassed(stage)) {
    			caption = "* " + caption;
    		} else if (levelManager.isStageWorkedOn(stage)) {
    			caption = "+ " + caption;
    		}
       		stageCaptions.add(caption);
    	}
    	
    	// create the dialog
    	if (stageCaptions.size() == 1) {
    		return showChooseLevelMenu(stage1);
    	} else {
    		// TODO push this logic into the level manager
    		// TODO all strings should go to resources
    		String title = getString(R.string.pick_a_stage);
    		int stagesCount = resources.getStagesCount();
    		if (stageCaptions.size() < stagesCount) {
    			title = title + "\n(" + stageCaptions.size() + "/" + stagesCount + " " + getString(R.string.unlocked)+ ")";
    		}
        	builder.setTitle(title);
	    	String[] items = new String[stageCaptions.size()];
	    	items = stageCaptions.toArray(items);
	    	builder.setItems(items, new DialogInterface.OnClickListener() {
	    	    public void onClick(DialogInterface dialog, int item) {
	    	    	Stage stage = resources.getStage(item);
	    	    	showChooseLevelMenu(stage);
	    	    }
	    	});
	    	AlertDialog dialog = builder.create();
	    	dialog.show();
	    	return true;
    	}
    }
    private boolean showChooseLevelMenu(final Stage stage) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	
		// TODO push as much logic to level manager as possible
		final JaroResources resources = game.getJaroResources();
    	List<String> levelCaptions = new ArrayList<String>();
    	int size = resources.getLevelsCount(stage.getStageKey());
		LevelManager levelManager = game.getModel().getLevelManager();
		boolean showAll = levelManager.isShowAllLevels();
		for (int i = 0; i < size; i++) {
    		Level level = resources.getLevel(stage.getStageKey(), i);
    		boolean unlocked = showAll || game.getModel().getLevelManager().isLevelUnlocked(level);
    		if (!unlocked) {
    			break;
    		}
    		String caption = ((i + 1) + ". " + level.getCaption());
    		if (game.getModel().getLevelManager().isLevelPassed(level)) {
    			caption = "* " + caption;
    		}
    		levelCaptions.add(caption);
		}

		String title = stage.getCaption();
		if (levelCaptions.size() < size) {
			title = title + "\n(" + levelCaptions.size() + "/" + size + " " + getString(R.string.unlocked)+ ")";
		}
    	builder.setTitle(title);

    	String[] items = new String[levelCaptions.size()];
    	items = levelCaptions.toArray(items);
    	builder.setItems(items, new DialogInterface.OnClickListener() {
    	    public void onClick(DialogInterface dialog, int item) {
    	    	Level level = resources.getLevel(stage.getStageKey(), item);
    	    	game.getController().selectLevel(level);
    	    }
    	});
    	AlertDialog dialog = builder.create();
    	
    	dialog.show();

    	return true;
    }
    @Override
    public boolean onSearchRequested() {
    	game.getView().zoomIn();
    	return true;
    }
	@SuppressLint("NewApi")
	public void showLevelAdvanceMenu() {
		// we need to give the menu a brief pause for the visual element
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(getString(R.string.level_passed) + "\n" + getString(R.string.next_up));
    	Level nextLevel = game.getModel().getLevelManager().getNextLevel();
    	String levelCaption = nextLevel.getCaption();
    	
    	/* TODO add this back - but there's a serious performance issue in the way the DbResources is working
    	final List<Stage> unlockedStages = game.getModel().getLevelManager().getOtherUnlockedStages(nextLevel.getStageKey());
    	
    	String[] items = new String[unlockedStages.size() + 1];
    	int i = 0;
    	items[i++] = levelCaption;
    	for (Stage stage: unlockedStages) {
    		items[i++] = stage.getCaption();
    	}
    	*/
    	String[] items = { levelCaption };
    	builder.setItems(items, new DialogInterface.OnClickListener() {
    	    public void onClick(DialogInterface dialog, int item) {
//    	    	if (item > 0) {
//    	    		showChooseLevelMenu(unlockedStages.get(item - 1));
//    	    	}
    	    	passLevelMenuDone();
    	    }
    	});
    	builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				passLevelMenuDone();
			}
		});
    	try {
	    	builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					passLevelMenuDone();
				}
			});
    	} catch (Throwable t) {
    		// this might throw if API is not compatible
    		Log.i("JaroActivity", "showLevelAdvanceMenu.t");
    	}
    	AlertDialog dialog = builder.create();
    	dialog.show();
	}
	void passLevelMenuDone() {
		game.getController().startAcceptingMoves();
	}
	public boolean isLandscape() {
    	return (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
	}

}
