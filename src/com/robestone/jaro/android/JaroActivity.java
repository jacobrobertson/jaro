package com.robestone.jaro.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.robestone.jaro.levels.JaroAssets;
import com.robestone.jaro.levels.JaroResources;
import com.robestone.jaro.levels.Level;
import com.robestone.jaro.levels.Stage;

public class JaroActivity extends Activity {

	private boolean showAllLevels = false;
	
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
			final JaroPreferences prefs = new JaroPreferences(this);
			if (prefs.isEulaRead()) {
				showHome();
			} else {
				showAbout();
			}
		}
	}
	private void createGame() {
		// TODO need to determine game type from what was clicked
		JaroAssets assets = new JaroAndroidAssets(getAssets());
		HtmlResources resources = new HtmlResources(assets);
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
	        case R.id.zoom:
	        	game.getView().zoomIn();
	        	return true;
	        default:
	            return false;
        }
    }
    private boolean showChooseStageMenu() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final JaroResources resources = game.getJaroResources();
    	List<String> stageCaptions = new ArrayList<String>();
    	Stage stage1 = null;
    	for (Stage stage: game.getJaroResources().getStages()) {
    		if (stage1 == null) {
    			stage1 = stage;
    		}
    		String stageKey = stage.getStageKey();
    		boolean unlocked = showAllLevels;
    		if (!unlocked) {
        		Level level = resources.getLevel(stageKey, 0);
    			unlocked = game.getModel().getLevelManager().isLevelUnlocked(level);
    		}
    		// TODO I want to make a better algorithm than this.  for example, if you pass
    		//		a certain percent of a stage, it unlocks the next stage
    		//		also, it should show the stages greyed out rather than just not showing up
    		if (!unlocked) {
    			break;
    		}
       		stageCaptions.add(stage.getCaption());
    	}
    	if (stageCaptions.size() == 1) {
    		return showChooseLevelMenu(stage1);
    	} else {
    		// TODO push this logic into the level manager
    		// TODO all strings should go to resources
    		String title = "Pick a Stage";
    		int stagesCount = resources.getStagesCount();
    		if (stageCaptions.size() < stagesCount) {
    			title = title + " (" + stageCaptions.size() + "/" + stagesCount + " unlocked)";
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
		for (int i = 0; i < size; i++) {
    		boolean unlocked = showAllLevels;
    		Level level = null;
    		if (!unlocked) {
    			level = resources.getLevel(stage.getStageKey(), i); 
    			unlocked = game.getModel().getLevelManager().isLevelUnlocked(level);
    		}
    		if (!unlocked) {
    			break;
    		}
    		levelCaptions.add((i + 1) + ". " + level.getCaption());
		}

		String title = stage.getCaption();
		if (levelCaptions.size() < size) {
			title = title + " (" + levelCaptions.size() + "/" + size + " unlocked)";
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
	public void showLevelAdvanceMenu() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(R.string.level_passed);
    	String[] items = {
    			"Continue", // TODO how to make in bundle? 
    			};
    	builder.setItems(items, new DialogInterface.OnClickListener() {
    	    public void onClick(DialogInterface dialog, int item) {
    	    	passLevelMenuDone();
    	    }
    	});
    	builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				passLevelMenuDone();
			}
		});
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
