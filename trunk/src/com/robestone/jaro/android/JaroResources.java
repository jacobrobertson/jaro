package com.robestone.jaro.android;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;

import com.robestone.jaro.levels.InputStreamBuilder;

public class JaroResources implements InputStreamBuilder {
	
	private Activity activity;
	private Map<String, Integer> ids = new HashMap<String, Integer>();
	
	public JaroResources(Activity activity) {
		this.activity = activity;
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

	@Override
	public InputStream buildLevelInputStream(String levelKey) {
		Field[] fields = R.raw.class.getDeclaredFields();
		int id = -1;
		for (Field field: fields) {
			if (field.getName().equals(levelKey)) {
				try {
					id = field.getInt(null);
				} catch (Exception e) {
					throw new RuntimeException("Could not find level: " + levelKey, e);
				}
			}
		}
		if (id < 0) {
			throw new RuntimeException("Could not find level: " + levelKey);
		}
		return activity.getResources().openRawResource(id);
	}

	@Override
	public InputStream buildStagesInputStream() {
		return activity.getResources().openRawResource(R.raw.stages);
	}
}
