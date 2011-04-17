package com.robestone.jaro;

public enum Direction {

	up(0, -1), down(0, 1), left(-1, 0), right(1, 0);

	private int xDirection;
	private int yDireection;
	private Direction(int xDirection, int yDireection) {
		this.xDirection = xDirection;
		this.yDireection = yDireection;
	}
	public int getXDirection() {
		return xDirection;
	}
	public int getYDireection() {
		return yDireection;
	}
	
	
}
