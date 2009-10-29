package org.jtb.droidlife;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

class GameView extends SurfaceView implements Seedable {
	private class GameThread extends Thread {
		private Handler mHandler;
		private boolean mRun = false;
		private SurfaceHolder mSurfaceHolder;

		public GameThread(SurfaceHolder surfaceHolder, Context context,
				Handler handler) {

			mSurfaceHolder = surfaceHolder;
			mHandler = handler;
			mContext = context;
		}

		@Override
		public void run() {
			mRun = true;
			while (mRun) {
				mWorld.generate();
				draw();

				mActivityHandler.sendMessage(mActivityHandler.obtainMessage(
						GameActivity.UPDATE_GEN_WHAT, mWorld.getGeneration()));
				mActivityHandler.sendMessage(mActivityHandler.obtainMessage(
						GameActivity.UPDATE_POP_WHAT, mWorld.getPopulation()));
			}

		}

		public void setRunning(boolean b) {
			mRun = b;
		}

		public boolean isRunning() {
			return mRun;
		}
	}

	private Context mContext;
	private GameThread thread;
	private int mCanvasWidth;
	private int mCanvasHeight;
	private World mWorld;
	private SurfaceHolder mSurfaceHolder;
	private Prefs prefs;
	private Handler mActivityHandler;
	private Seeder mSeeder;

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);

		setFocusable(true);
		prefs = new Prefs(context);
	}

	public GameThread getThread() {
		return thread;
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
	}

	public void setSize(int width, int height) {
		mCanvasWidth = width;
		mCanvasHeight = height;
	}

	public void stop() {
		if (thread != null) {
			// we have to tell thread to shut down & wait for it to finish, or
			// else
			// it might touch the Surface after we return and explode
			boolean retry = true;
			thread.setRunning(false);
			while (retry) {
				try {
					thread.join();
					retry = false;
				} catch (InterruptedException e) {
				}
			}
			thread = null;
		}
		mActivityHandler.sendMessage(mActivityHandler.obtainMessage(
				GameActivity.UPDATE_STATUS_WHAT, false));
	}

	public boolean isSeeded() {
		return mWorld != null;
	}

	public void start() {
		thread = new GameThread(getHolder(), mContext, new Handler() {
			@Override
			public void handleMessage(Message m) {
				// Use for pushing back messages.
			}
		});
		thread.start();
		mActivityHandler.sendMessage(mActivityHandler.obtainMessage(
				GameActivity.UPDATE_STATUS_WHAT, true));
	}

	public void seed(Seeder seeder) {
		this.mSeeder = seeder;

		int cellSize = prefs.getCellSize();
		int[] birthNeighbors = prefs.getBirthRule();
		int[] surviveNeighbors = prefs.getSurvivalRule();
		mWorld = new World(mCanvasWidth / cellSize, mCanvasHeight / cellSize,
				cellSize, birthNeighbors, surviveNeighbors);
		mActivityHandler.sendMessage(mActivityHandler.obtainMessage(
				GameActivity.UPDATE_NAME_WHAT, seeder.getName()));
		seeder.seed(mWorld);
		refresh();
	}

	public void step() {
		mWorld.generate();
		refresh();
	}

	private void draw() {
		Canvas c = null;
		try {
			c = mSurfaceHolder.lockCanvas(null);
			if (c == null) {
				Log
						.w(getClass().getSimpleName(),
								"canvas is not ready to draw");
				return;
			}
			synchronized (mSurfaceHolder) {
				c.drawARGB(255, 0, 0, 0);
				mWorld.draw(c);
			}
		} finally {
			if (c != null) {
				mSurfaceHolder.unlockCanvasAndPost(c);
			}
		}
	}

	public void refresh() {
		if (mWorld == null) {
			return;
		}
		mActivityHandler.sendMessage(mActivityHandler.obtainMessage(
				GameActivity.UPDATE_STATUS_WHAT, isRunning()));
		mActivityHandler.sendMessage(mActivityHandler.obtainMessage(
				GameActivity.UPDATE_TYPE_WHAT, mWorld.getType()));
		mActivityHandler.sendMessage(mActivityHandler.obtainMessage(
				GameActivity.UPDATE_GEN_WHAT, mWorld.getGeneration()));
		mActivityHandler.sendMessage(mActivityHandler.obtainMessage(
				GameActivity.UPDATE_POP_WHAT, mWorld.getPopulation()));
		draw();
	}

	public boolean isRunning() {
		return thread != null;
	}

	public void setActivityHandler(Handler mActivityHandler) {
		this.mActivityHandler = mActivityHandler;
	}

	public void setSurfaceHolder(SurfaceHolder mSurfaceHolder) {
		this.mSurfaceHolder = mSurfaceHolder;
	}

	public String save(String name) {
		SeedSource ss;

		if (mSeeder == null) {
			ss = new Life106SeedSource();
		} else if (!mSeeder.getSeedSource().isWritable()) {
			ss = new Life106SeedSource();

			// we are duplicating a seed from a non-writable source,
			// qualify the copy with a unique identifier so they
			// can be distinguished
			name = new SeedNameQualifier(name).toString();
		} else {
			ss = mSeeder.getSeedSource();
		}

		if (!ss.isWritable()) {
			throw new AssertionError("seed is not writable");
		}

		ss.writeSeed(name, mWorld);
		SeederManager.getInstance(mContext).refresh();

		return name;
	}
}
