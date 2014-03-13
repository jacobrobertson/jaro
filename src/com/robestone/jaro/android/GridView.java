package com.robestone.jaro.android;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.robestone.jaro.Piece;

/**
 * Animation strategy
 * - the invalidate is firing every 50 ms, so that loop takes care of it - we can tighten that if we need to...
 * - whenever we paint, we check each sprite to see if it's "in motion"
 * - we only need to check certain types of sprites, but for now just do them all
 * - if a sprite is "on top of" two things they both need to be lightened.  
 * --- For example, jaro could be moving from on top of a bush to on top of another bush
 * 
 * @author jacob
 */
public class GridView extends SurfaceView implements SurfaceHolder.Callback {

	private Random random = new Random();
	private JaroAndroidResources resources;
	private JaroActivity activity;
	private SpriteAnimationThread spriteAnimationThread;
	private JaroAndroidView jaroView;
	
	private class CellInfo {
		String spriteKey;
		int currentFrame;
		/**
		 * Tells us when this frame was last drawn.
		 */
		long frameDrawTime = -1;
	}
	private Map<Integer, CellInfo> pieceAnimations = new HashMap<Integer, GridView.CellInfo>();
	
	public GridView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		this.activity = (JaroActivity) context;
		getHolder().addCallback(this);
		
		JaroAndroidGame game = activity.getGame();
		jaroView = game.getView();
		resources = game.getJaroResources();
		game.getView().setGridView(this);
	}
	public void clearCurrentLevel() {
		pieceAnimations = new HashMap<Integer, GridView.CellInfo>();
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		SurfaceHolder lholder = holder; // getHolder();
		spriteAnimationThread = new SpriteAnimationThread(lholder, this);
		spriteAnimationThread.setRunning(true);
        spriteAnimationThread.start();
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
        spriteAnimationThread.setRunning(false);
        while (retry) {
            try {
                spriteAnimationThread.join();
                retry = false;
            } catch (InterruptedException e) {
                    // we will try it again and again...
            }
        }
        spriteAnimationThread = null;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// this was causing a concurrent modification on accessing the pieces
		// check to ensure only one thread tries to draw at a time
		onDrawInner(canvas);
	}
	protected void onDrawInner(Canvas canvas) {		
		canvas.drawColor(Color.BLACK);
		
		boolean isLandscape = activity.isLandscape();
		int cellLen = jaroView.getCellLength();
		
		int w = jaroView.getColumns(isLandscape);
		int h = jaroView.getRows(isLandscape);
		
		Point offset = getGridOffset(isLandscape, w, h, cellLen);
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				drawBackground(canvas, i, j, isLandscape, cellLen, offset);
			}
		}
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				drawCell(i, j, isLandscape, cellLen, canvas, offset);
			}
		}
	}
	
	private void drawBackground(Canvas canvas, int x, int y, boolean isLandscape, int cellLen, Point offset) {
		// TODO a lot of this is pretty inefficent - it went this way because of some existing code I was reusing
		// TODO should combine this method with drawCell, and only do these computations once for each cell, not twice
		Rect bounds = getBounds(x, y, isLandscape, cellLen);
		bounds.offset(offset.x, offset.y);
		
		// draw the ground
		Drawable background = activity.getResources().getDrawable(R.drawable.background);
		background.setBounds(bounds);
		background.draw(canvas);
	}
	
	private Rect getBounds(int x, int y, boolean isLandscape, int cellLen) {
		Point pt = getCellPosition(x, y, isLandscape, cellLen);
		Rect bounds = new Rect(pt.x, pt.y, pt.x + cellLen, pt.y + cellLen);
		return bounds;
	}
	private void drawCell(int x, int y, boolean isLandscape, int cellLen, Canvas canvas, Point offset) {
		Rect bounds = getBounds(x, y, isLandscape, cellLen);
		bounds.offset(offset.x, offset.y);
		
		// draw the sprite
		List<Drawable> drawables = getDrawables(x, y, isLandscape);
		if (drawables != null) {
			boolean bottom = drawables.size() > 1;
			for (Drawable drawable: drawables) {
				drawable.setBounds(bounds);
				int alpha;
				if (bottom) {
					alpha = 125;
				} else {
					alpha = 255;
				}
				drawable.setAlpha(alpha);
				drawable.draw(canvas);
				bottom = false;
			}
		}
	}
	private synchronized List<Drawable> getDrawables(int x, int y, boolean isLandscape) {
		List<Piece> pieces = jaroView.getPieces(x, y, isLandscape);
		if (pieces == null) {
			return null;
		}
		// TODO I really don't want to make a new ArrayList, but I need to figure out the concurrent modification issues on this
		pieces = new ArrayList<Piece>(pieces);
		List<Drawable> drawables = new ArrayList<Drawable>();
		for (Piece piece: pieces) {
			String spriteKey = jaroView.getSprite(piece, isLandscape);
			int id = resources.getSpriteId(spriteKey);
			
			// determine if this id is an animated sprite
			Drawable d = activity.getResources().getDrawable(id);
			if (d instanceof AnimationDrawable) {
				AnimationDrawable a = (AnimationDrawable) d;
				CellInfo info = pieceAnimations.get(piece.getId());
				if (info == null) {
					info = new CellInfo();
					info.spriteKey = spriteKey;
					int frameCount = a.getNumberOfFrames();
					info.currentFrame = (int) (random.nextFloat() * (frameCount - 1));
					pieceAnimations.put(piece.getId(), info);
				}
				// see if it's time to change to next frame or not
				boolean next = false;
				if (info.frameDrawTime == -1) {
					next = true;
				} else {
					// trying to add additional random element to movement behavior
					try {
						long requiredDuration = (long) (a.getDuration(info.currentFrame) * (random.nextBoolean() ? 1.05f : .95f));
						long actualDuration = System.currentTimeMillis() - info.frameDrawTime;
						if (actualDuration >= requiredDuration) {
							next = true;
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						// this exception happens when (for example) we start the level over, or undo, and the piece it's reverting to has more frames
						// Log.e("GridView", "ArrayIndexOutOfBounds: " + piece + "/" + id + "/" + spriteKey);
						next = true;
					}
				}
				if (next) {
					info.currentFrame++;
					info.frameDrawTime = System.currentTimeMillis();
				}
				if (info.currentFrame >= a.getNumberOfFrames()) {
					info.currentFrame = 0;
				}
				d = a.getFrame(info.currentFrame);
				
				if (d == null) {
					throw new IllegalStateException(
							"Animation thread working against wrong map for sprite " + spriteKey + ".\n" + 
									"Current piece: " + piece + "\n" +
									"Info spriteKey: " + info.spriteKey + "\n" +
									"AnimationDrawable " + a + ", " + "frame=" + info.currentFrame + "; a.getNumberOfFrames=" + a.getNumberOfFrames());
				}
			}
			if (d == null) {
				throw new IllegalArgumentException("No sprite " + spriteKey + "/" + piece);
			}
			drawables.add(d);
		}
		Collections.reverse(drawables);
		return drawables;
	}
	private Point getCellPosition(int x, int y, boolean isLandscape, int cellLen) {
		int xx = x * cellLen;
		int yy = y * cellLen;
		
		// Based on the size of the screen, and the size of the maze, create an offset.
		int visibleWidthOfMaze = jaroView.getColumns(isLandscape) * cellLen;
		int visibleHeightOfMaze = jaroView.getRows(isLandscape) * cellLen;
		int windowHeight = getHeight();
		int windowWidth = getWidth();
		
		int xpad = (windowWidth - visibleWidthOfMaze) / 2;
		int ypad = (windowHeight - visibleHeightOfMaze) / 2;
		
		return new Point(xx + xpad, yy + ypad);
	}
	/**
	 * Finds out how much we can slide the grid over to try and keep jaro in the center.
	 */
	private Point getGridOffset(boolean isLandscape, int cols, int rows, int cellLen) {
		Point topLeft = getCellPosition(0, 0, isLandscape, cellLen);
		
		int bottomRightCols = jaroView.getColumns(isLandscape);
		int bottomRightRows = jaroView.getRows(isLandscape);
		Point bottomRight = getCellPosition(bottomRightCols, bottomRightRows, isLandscape, cellLen);

		int jaroCol = jaroView.getModel().getJaroColumn();
		int jaroRow = jaroView.getModel().getJaroRow();
		
		int offsetX = getGridJaroOffset(topLeft.x, jaroCol, getWidth(), bottomRight.x, cellLen);
		int offsetY = getGridJaroOffset(topLeft.y, jaroRow, getHeight(), bottomRight.y, cellLen);
		return new Point(offsetX, offsetY);
	}
	private int getGridJaroOffset(int plannedOffset, int jaroGridPos, int screenLen, int cornerPos, int cellLen) {
		// if it's on-screen then we know it's zoomed out
		if (plannedOffset >= 0) {
			return 0;
		}
		
		int center = screenLen / 2;
		int jaroScreenPos = jaroGridPos * cellLen + plannedOffset + (cellLen / 2);
		int offset = center - jaroScreenPos;
		
		if (offset > 0) {
			if (offset + plannedOffset > 0) {
				offset = -plannedOffset;
			}
		} else if (offset < 0) {
			if (cornerPos + offset  < screenLen) {
				offset = screenLen - cornerPos;
			}
		}

		return offset;
	}
}
