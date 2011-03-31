package com.robestone.jaro.android;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class SpriteAnimationThread extends Thread {

	private SurfaceHolder surfaceHolder;
	private GridView gridView;
	private boolean run = false;
	private int sleep = 25;

	public SpriteAnimationThread(SurfaceHolder surfaceHolder, GridView gridView) {
		this.surfaceHolder = surfaceHolder;
		this.gridView = gridView;
	}

	public void setRunning(boolean run) {
		this.run = run;
	}

	@Override
	public void run() {
		Canvas canvas;
		while (run) {
			canvas = null;
			try {
				canvas = surfaceHolder.lockCanvas(null);
				synchronized (surfaceHolder) {
					gridView.onDraw(canvas);
				}
				try {
					sleep(sleep);
				} catch (InterruptedException e) {
				}
			} finally {
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
}