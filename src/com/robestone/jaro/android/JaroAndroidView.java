package com.robestone.jaro.android;

import android.graphics.drawable.Drawable;

import com.robestone.jaro.JaroView;

public class JaroAndroidView extends JaroView {
	enum Zoom { smallest, biggest, middle };
	private static final float ZOOM_MIDDLE = .5f;
	
	private int maxCellLen;
	private Zoom zoom = Zoom.smallest;
	private GridView gridView;
	private JaroActivity activity;
	
	public JaroAndroidView(JaroActivity activity) {
		this.activity = activity;
		Drawable d = activity.getResources().getDrawable(R.drawable.background);
		// TODO need the right way to determine this - for now, it's scaling fine, but I'm not positive it's choosing the best image
		this.maxCellLen = 96;// (this is from the xhdpi)//  d.getMinimumHeight() * 5; // TODO this is the problem???? not sure.
	}
	public void setGridView(GridView gridView) {
		this.gridView = gridView;
	}
	@Override
	public void passedLevel() {
		levelSelected(true);
	}
	@Override
	public void levelSelected() {
		levelSelected(false);
	}
	private void levelSelected(boolean showAdvanceMenu) {
		zoom = Zoom.smallest;
		gridView.clearCurrentLevel();
		if (showAdvanceMenu) {
			activity.showLevelAdvanceMenu();
		}
	}
	public int getCellLength() {
		if (zoom == Zoom.biggest) {
			return maxCellLen;
		}
		boolean isLandscape = activity.isLandscape();
		// this setting is "zoom (in/out) to fit"
		int cols = getColumns(isLandscape);
		int windowWidth = gridView.getWidth();
		int minCellWidth = windowWidth / cols;
		if (minCellWidth > maxCellLen) {
			minCellWidth = maxCellLen;
		}
		int rows = getRows(isLandscape);
		int windowHeight = gridView.getHeight();
		int minCellHeight = windowHeight / rows;
		if (minCellHeight > maxCellLen) {
			minCellHeight = maxCellLen;
		}
		int minCellLen = minCellHeight;
		if (minCellLen > minCellWidth) {
			minCellLen = minCellWidth;
		}
		
		if (zoom == Zoom.smallest) {
			return minCellLen;
		} else {
			return (int) ((maxCellLen - minCellLen) * ZOOM_MIDDLE + minCellLen);
		}
	}
	
	public void zoomIn() {
		if (zoom == Zoom.smallest) {
			zoom = Zoom.middle;
		} else if (zoom == Zoom.middle) {
			zoom = Zoom.biggest;
		} else {
			zoom = Zoom.smallest;
		}
	}
}
