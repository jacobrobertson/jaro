package com.robestone.jaro.android;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.robestone.jaro.levels.JaroResources;
import com.robestone.jaro.levels.Level;
import com.robestone.jaro.levels.Stage;
import com.robestone.jaro.levels.Utils;

/**
 * Central class for performing all I/O of levels and stages.
 * 
 * @author Jacob
 */
public abstract class JaroAndroidResources implements JaroResources {
	
	
	public static final String JARO_GAME_TYPE = "Jaro";
	public static final String JAROBAN_GAME_TYPE = "Jaroban";
	
	private Map<String, Integer> ids = new HashMap<String, Integer>();
	
	private List<Stage> stages;
	private List<Level> currentLevels;
	private String currentStage;

	@Override
	public final Iterable<Stage> getStages() {
		return getStagesList();
	}
	private List<Stage> getStagesList() {
		if (stages == null) {
			stages = doGetStages();
		}
		return stages;
	}
	abstract List<Stage> doGetStages();

	@Override
	public final int getStagesCount() {
		return getStagesList().size();
	}

	@Override
	public final Stage getStage(int index) {
		return getStagesList().get(index);
	}

	@Override
	public final Iterable<Level> getLevels(String stageKey) {
		return getLevelsList(stageKey);
	}
	private List<Level> getLevelsList(String stageKey) {
		if (!stageKey.equals(currentStage)) {
			currentLevels = doGetLevels(stageKey);
			currentStage = stageKey;
		}
		return currentLevels;
	}
	abstract List<Level> doGetLevels(String stageKey);

	@Override
	public final int getLevelsCount(String stageKey) {
		return getLevelsList(stageKey).size();
	}

	@Override
	public final Level getLevel(String stageKey, int index) {
		return getLevelsList(stageKey).get(index);
	}

	public Integer getSpriteId(String spriteKey) {
		Integer id = ids.get(spriteKey);
		if (id != null) {
			return id;
		}
		Field[] fields = R.drawable.class.getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals(spriteKey)) {
				try {
					id = field.getInt(null);
					ids.put(spriteKey, id);
					return id;
				} catch (Exception e) {
					throw new RuntimeException("Could not access " + spriteKey, e);
				}
			}
		}
		throw new RuntimeException("Could not find sprite key " + spriteKey);
	}

	static String cleanKey(String k) {
		char c = k.charAt(k.length() - 1);
		if (Character.isDigit(c)) {
			k = k.substring(0, k.length() - 1);
		}
		return k;
	}
	static String cleanName(String fileName) {
		String caption = fileName;
		int pos = caption.indexOf('.');
		if (pos > 0) {
			caption = caption.substring(pos + 1);
		}
		caption = Utils.parseName(caption);
		return caption;
	}


}
