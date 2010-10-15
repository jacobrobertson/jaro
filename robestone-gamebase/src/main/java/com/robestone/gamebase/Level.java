package com.robestone.gamebase;

public class Level {

	private int number;
	private String name;
	
	public Level(int number) {
		this.number = number;
	}
	public Level(int number, String name) {
		this.number = number;
		this.name = name;
	}
	public int getNumber() {
		return number;
	}
	public String getName() {
		return name;
	}
	
	
	
}
