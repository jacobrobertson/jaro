package com.robestone.jaro.android;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.robestone.jaro.levels.JaroResources;

/**
 * Central class for performing all I/O of levels and stages.
 * 
 * @author Jacob
 */
public abstract class JaroAndroidResources implements JaroResources {
	
	
	public static final String JARO_GAME_TYPE = "Jaro";
	public static final String JAROBAN_GAME_TYPE = "Jaroban";
	
	private Map<String, Integer> ids = new HashMap<String, Integer>();
	
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

}
