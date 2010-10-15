package com.robestone.jaro.example;

import com.robestone.jaro.LevelData;
import com.robestone.jaro.LevelDataProvider;

public class ExampleLevelDataProvider implements LevelDataProvider {

	@Override
	public LevelData getLevelData(int levelIndex) {
		return data[levelIndex];
	}
	@Override
	public int getLastLevelIndex() {
		return data.length - 1;
	}
	private static final LevelData[] data = {
		new LevelData(
		"xx[j]xx" +
		"x/' `~x" +
		"x[nzn]x" +
		"x[z z]x" +
		"x[n n]x" +
		"x:. ,;x" +
		"xx[z]xx", 
		7, 7), 
		
		new LevelData(
		"xx[j]xx" +
		"/-' `-~" +
		"[ nzn ]" +
		"[zonoz]" +
		"[ n n ]" +
		":_. ,_;" +
		"xx[n]xx" +
		"xx[z]xx",
		7, 7), 
		
		new LevelData(
		"[nnjnn]" +
		"[no on]" +
		"[ nzn ]" +
		"[zoooz]" +
		"[ o n ]" +
		"[zn ,_;" +
		":_.z]xx",
		7, 7), 
		
		new LevelData(
		"zn j nz" +
		" r>n<7 " +
		" }nzn} " +
		"z}znz}z" +
		" }n n} " +
		" L>n<J " +
		"zn z nz",
		7, 7), 
		
		new LevelData(
		"xx[j]x" +
		"x/' `~" +
		"-'nzn]" +
		" nz z]" +
		" ^n n]" +
		" L7 ,;" +
		"#z}f]x",
		6, 7), 
		
		new LevelData(
		"#znj#zn" +
		" r> <7 " +
		"#vozov " +
		"znzzzn " +
		" ^ono^ " +
		" L===J " +
		"#znznf ",
		7, 7), 
		
//		new LevelData(
//		"" +
//		"" +
//		"" +
//		"" +
//		"" +
//		"" +
//		"",
	};

}
