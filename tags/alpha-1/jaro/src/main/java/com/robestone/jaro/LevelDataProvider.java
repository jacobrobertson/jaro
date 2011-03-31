package com.robestone.jaro;

public interface LevelDataProvider {

	LevelData getLevelData(int levelIndex);
	int getLastLevelIndex();
}
