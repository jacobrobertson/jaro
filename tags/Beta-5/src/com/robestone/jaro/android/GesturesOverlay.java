package com.robestone.jaro.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.robestone.jaro.Direction;
import com.robestone.jaro.JaroController;

/**
 * Transparent view that overlays the game, and captures gestures.
 */
public class GesturesOverlay extends View implements OnTouchListener {

	private Paint xPaintBackground;
	private Paint xPaintForeground;
	private Direction directionFading;
	private int alphaFadingIndex;
	private List<Paint> fadingPaint;
	private boolean dragging = false;
	private int currentDrags;
	private float dragX;
	private float dragY;
	
	private JaroController controller;
	private JaroAndroidView jaroView;
	private JaroActivity activity;
	
	public GesturesOverlay(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		
		activity = (JaroActivity) context;
		JaroAndroidGame jaroGame = ((JaroActivity) context).getGame();
		this.controller = jaroGame.getController();
		this.jaroView = jaroGame.getView();
		
		setFocusable(true);
		setFocusableInTouchMode(true);
		this.setOnTouchListener(this);

		xPaintBackground = new Paint();
		xPaintBackground.setColor(Color.WHITE);
		xPaintBackground.setAntiAlias(true);
		xPaintBackground.setAlpha(25);
		xPaintBackground.setStrokeWidth(1);
        
		xPaintForeground = new Paint();
		xPaintForeground.setColor(Color.BLACK);
		xPaintForeground.setAntiAlias(true);
		xPaintForeground.setAlpha(25);
		xPaintForeground.setStrokeWidth(1);
        
        fadingPaint = new ArrayList<Paint>();
        int min = 5;
        int max = 50;
        int interval = 5;
        for (int i = max; i >= min; i-=interval) {
    		Paint fPaint = new Paint();
    		fPaint.setColor(Color.WHITE);
            fPaint.setAntiAlias(true);
            fPaint.setAlpha(i);
            fPaint.setStyle(Style.FILL);
			fadingPaint.add(fPaint);
		}
        
    }
	public boolean onTouch(View view, MotionEvent event) {
//		Log.i("GesturesOverly", "onTouch." + event.getAction() + "." + currentDrags);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			dragging = false;
			currentDrags = 0;
			dragX = event.getX();
			dragY = event.getY();
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			dragging = true;
			Direction dir = getDirectionFromDrag(event.getX(), event.getY());
			if (dir != null) {
				currentDrags++;
				dragX = event.getX();
				dragY = event.getY();
				sendDirection(dir);
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (!dragging || currentDrags == 0) {
				Direction dir = getDirection((int) event.getX(), (int) event.getY());
				sendDirection(dir);
				startFading(dir);
			}
			dragging = false;
		}
		return true;
	}
	private void sendDirection(Direction direction) {
		if (activity.isLandscape()) {
			switch (direction) {
				case down: direction = Direction.left; break;
				case up: direction = Direction.right; break;
				case left: direction = Direction.up; break;
				case right: direction = Direction.down; break;
			}
		}
		controller.move(direction);
	}
	private void startFading(Direction dir) {
		directionFading = dir;
		alphaFadingIndex = -1;
		postInvalidate();
	}
	private void nextFade(Canvas canvas) {
		if (directionFading != null) {
			alphaFadingIndex++;
			if (alphaFadingIndex >= fadingPaint.size()) {
				alphaFadingIndex = -1;
				directionFading = null;
			} else {
				drawArrow(directionFading, canvas);
				postInvalidate();
			}
		}
	}
	private void drawArrow(Direction dir, Canvas canvas) {

		int w4 = getWidth() / 4;
		int h4 = getHeight() / 4;
		
		int x1, y1;
		int x2, y2;
		int x3, y3;
		
		if (Direction.up == dir) {
			x1 = w4;
			y1 = h4;
			x2 = w4 * 2;
			y2 = 0;
			x3 = w4 * 3;
			y3 = h4;
		} else if (Direction.left == dir) {
			x1 = 0;
			y1 = h4 * 2;
			x2 = w4;
			y2 = h4;
			x3 = w4;
			y3 = h4 * 3;
		} else if (Direction.down == dir) {
			x1 = w4;
			y1 = h4 * 3;
			x2 = w4 * 3;
			y2 = h4 * 3;
			x3 = w4 * 2;
			y3 = h4 * 4;
		} else { //if (JaroConstants.right.equals(dir)) {
			x1 = w4 * 3;
			y1 = h4;
			x2 = w4 * 4;
			y2 = h4 * 2;
			x3 = w4 * 3;
			y3 = h4 * 3;
		}
		
		Path path = new Path();
		path.moveTo(x1, y1);
		path.lineTo(x2, y2);
		path.lineTo(x3, y3);
		path.close();
		Paint fPaint = fadingPaint.get(alphaFadingIndex);
		canvas.drawPath(path, fPaint);
		
	}
	private Direction getDirection(int x, int y) {
		return getDirection(x, y, getWidth(), getHeight());
	}
	private Direction getDirectionFromDrag(float x, float y) {
		int neededLen = jaroView.getCellLength();
		float xlen = x - dragX;
		float ylen = y - dragY;
		
		float axlen = Math.abs(xlen);
		float aylen = Math.abs(ylen);
		
		Direction dir = null;
		if (axlen > neededLen && axlen > aylen) {
			if (xlen < 0) {
				dir = Direction.left;
			} else {
				dir = Direction.right;
			}
		} else if (aylen > neededLen) {
			if (ylen < 0) {
				dir = Direction.up;
			} else {
				dir = Direction.down;
			}
		}
		return dir;
	}
	public static Direction getDirection(int x, int y, int w, int h) {		
		// get the center of the screen
		int cx = w / 2;
		int cy = h / 2;
		
		// diffs
		float dx = cx - x;
		float dy = cy - y;
		
		// calculate out the rise/run, and the max points
		float ratio = (float) h / (float) w;
		float maxY = Math.abs(dx * ratio);
		float absY = Math.abs(dy);
		
		Direction dir;
		if (absY < maxY) {
			if (dx > 0) {
				dir = Direction.left;
			} else {
				dir = Direction.right;
			}
		} else {
			if (dy > 0) {
				dir = Direction.up;
			} else {
				dir = Direction.down;
			}
		}
		return dir;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		drawX(canvas, xPaintBackground, 0);
		drawX(canvas, xPaintForeground, 1);
		
		nextFade(canvas);
	}
	private void drawX(Canvas canvas, Paint paint, int offset) {
		canvas.drawLine(0 + offset, 0, getWidth() - 1 + offset, getHeight() - 1, paint);
		canvas.drawLine(getWidth() - 1 + offset, 0, 0 + offset, getHeight() - 1, paint);
	}

}
