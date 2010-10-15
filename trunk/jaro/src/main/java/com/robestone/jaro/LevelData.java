package com.robestone.jaro;

public class LevelData {

	private String data;
	private int columns;
	private int rows;
	private String name;
	private String caption;
	
	public LevelData(String data, int columns, int rows) {
		this(data, columns, rows, null, null);
	}
	public LevelData(String data, int columns, int rows, String name, String caption) {
		this.data = data;
		this.columns = columns;
		this.caption = caption;
		this.name = name;
		this.rows = rows; // data.length() / columns;
	}
	public String getPieceType(int x, int y) {
		return null;
	}
	public String getData() {
		return data;
	}

	public int getColumns() {
		return columns;
	}
	public int getRows() {
		return rows;
	}
	public String getCaption() {
		return caption;
	}
	public String getName() {
		return name;
	}
	
}
