package com.robestone.jaro.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.robestone.jaro.Action;
import com.robestone.jaro.PieceRules;
import com.robestone.jaro.SoundPlayer;
import com.robestone.jaro.levels.Level;
import com.robestone.jaro.piecerules.BugRules;
import com.robestone.jaro.piecerules.JaroRules;
import com.robestone.jaro.piecerules.TurtleRules;

public class AndroidSoundPlayer implements SoundPlayer, AudioManager.OnAudioFocusChangeListener {

	public void addEatRule(String type, int res) {
		SoundActionRule rule = new SoundActionRule();
		rule.isRemove = true;
		rule.pieceTypeId = type;
		rule.resourceId = res;
		rules.add(rule);
	}
	public void addMoveRule(String type, int res) {
		SoundActionRule rule = new SoundActionRule();
		rule.isMove = true;
		rule.pieceTypeId = type;
		rule.resourceId = res;
		rules.add(rule);
	}
	public void addIllegalMoveRule(String type, int res) {
		SoundActionRule rule = new SoundActionRule();
		rule.isIllegalMove = true;
		rule.pieceTypeId = type;
		rule.resourceId = res;
		rules.add(rule);
	}

	private class SoundActionRule {
		
		public int soundId;
		public int resourceId;
		
		public String pieceTypeId;
		public boolean isMove;
		public boolean isRemove;
		public boolean isChange;
		public Object newState;
		public boolean isIllegalMove;
		
		public boolean matches(Level level, PieceRules actionRule, Action action, boolean isIllegalMove) {
			
			if (!action.getActingPiece().getType().equals(pieceTypeId)) {
				return false;
			}
			
			if (isIllegalMove != this.isIllegalMove) {
				return false;
			}
			
			if (isIllegalMove) {
				// because we already checked the piece type
				// this only works if the rule looks for "jaro" - so there's no way to tell what he bumped into
				return true;
			} else if (isMove && action.isMovePiece()) {
				return true;
			} else if (isRemove && action.isRemovePiece()) {
				return true;
			} else if (isChange) {
				Object actionState = action.getNewStateId();
				if (newState.equals(actionState)) {
					return true;
				}
			}
			
			return false;
		}
		
		public void load(SoundPool soundPool) {
			soundId = soundPool.load(AndroidSoundPlayer.this.context, resourceId, 1);
		}
		
	}
	
	private static final int NO_SOUND = -666;
	private static final float MUSIC_VOLUME = .25f;
	private static final float EFFECTS_VOLUME = .65f;
	
	private MediaPlayer levelPlayer;
	private boolean levelPlayerPaused;
	private SoundPool actionPlayer;
	private Context context;
	private List<SoundActionRule> rules;
	private AudioManager audioManager;
	
	private int levelPassedSound;
	
	public AndroidSoundPlayer(Context context) {
		this.context = context;
		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		initRules();
		levelPlayer = MediaPlayer.create(context, R.raw.theme1);
		levelPlayer.setLooping(true);
		levelPlayer.setVolume(MUSIC_VOLUME, MUSIC_VOLUME);
		
		actionPlayer = new SoundPool(rules.size(), AudioManager.STREAM_RING, 1);
		for (SoundActionRule rule: rules) {
			rule.load(actionPlayer);
		}
		levelPassedSound = actionPlayer.load(context, R.raw.end_level, 1);
	}
	
	@Override
	public void onGameDisplayed() {
		int result = 
				audioManager.requestAudioFocus(this,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
		if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
			start();
		}
	}
	@Override
	public void onGameHidden() {
		audioManager.abandonAudioFocus(this);
		pause();
	}
	
	private void initRules() {
		rules = new ArrayList<SoundActionRule>();
		addEatRule(BugRules.BUG_TYPE_ID, R.raw.eat_bug);
		addMoveRule(TurtleRules.TURTLE_TYPE_ID, R.raw.push_turtle);
		addIllegalMoveRule(JaroRules.JARO_TYPE_ID, R.raw.turtle_fall);
	}

	@Override
	public void levelPassed(Level level) {
		playAction(levelPassedSound);
	}

	/**
	 * TODO determine if we need a different player for each level, or a sound pool, or just reload the player each time, etc??
	 */
	@Override
	public void levelStarted(Level level) {
		start();
	}

	@Override
	public void levelStopped(Level level) {
		pause();
	}
	private void playAction(int soundId) {
		try {
			actionPlayer.play(soundId, EFFECTS_VOLUME, EFFECTS_VOLUME, 1, 0, 1f);
		} catch (RuntimeException re) {
			// just swallow for now - need to determine if this is an issue
		}
	}
	@Override
	public void action(Level level, PieceRules actionRule, Action action, boolean isIllegalMove) {
		int soundId = getSoundId(level, actionRule, action, isIllegalMove);
		if (soundId != NO_SOUND) {
			playAction(soundId);
		}
	}
	
	private int getSoundId(Level level, PieceRules actionRule, Action action, boolean isIllegalMove) {
		for (SoundActionRule rule: rules) {
			if (rule.matches(level, actionRule, action, isIllegalMove)) {
				return rule.soundId;
			}
		}
		return NO_SOUND;
	}
	@Override
	public void onAudioFocusChange(int type) {
		if (type == AudioManager.AUDIOFOCUS_GAIN) {
			levelStarted(null);
		} else {
			levelStopped(null);
			audioManager.abandonAudioFocus(this);
		}
	}
	private void pause() {
		levelPlayer.pause();
		levelPlayerPaused = true;
	}
	private void start() {
		if (levelPlayerPaused || !levelPlayer.isPlaying()) {
			levelPlayer.start();
			levelPlayerPaused = false;
		}
	}
	
}
