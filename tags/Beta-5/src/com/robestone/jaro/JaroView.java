package com.robestone.jaro;

import java.util.List;

public class JaroView {

	private JaroController controller;
	private JaroModel model;
	
	public void passedLevel() {
	}
	public void levelSelected() {
	}
	public void setController(JaroController controller) {
		this.controller = controller;
	}
	public void setModel(JaroModel model) {
		this.model = model;
	}
	public JaroController getController() {
		return controller;
	}
	public JaroModel getModel() {
		return model;
	}
	public int getColumns(boolean isLandscape) {
		if (isLandscape) {
			return getRows();
		} else {
			return getColumns();
		}
	}
	public int getRows(boolean isLandscape) {
		if (isLandscape) {
			return getColumns();
		} else {
			return getRows();
		}
	}
	private int getColumns() {
		return model.getGrid().getColumns();
	}
	private int getRows() {
		return model.getGrid().getRows();
	}
	public List<Piece> getPieces(int x, int y, boolean isLandscape) {
		if (isLandscape) {
			int c = getColumns();
			int ty = x;
			x = c - 1 - y;
			y = ty;
		}
		return model.getGrid().getPieces(x, y);
	}
	public String getSprite(Piece p, boolean isLandscape) {
		for (PieceRules rules: controller.getPieceRules()) {
			String id = rules.getSpriteId(p, isLandscape);
			if (id != null) {
				return id;
			}
		}
		throw new IllegalArgumentException("Could not find a matching sprite for " + p.getType() + "/" +  p.getSubType() + "/" + p.getState());
	}
}
