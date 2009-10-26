package org.jtb.droidlife;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity implements
		OnSharedPreferenceChangeListener {

	static final int UPDATE_TYPE_WHAT = 0;
	static final int UPDATE_GEN_WHAT = 1;
	static final int UPDATE_POP_WHAT = 2;
	static final int UPDATE_STATUS_WHAT = 3;

	static final int SEED_MENU_DIALOG = 0;
	static final int INFO_DIALOG = 1;

	private static final int MENU_PLAY = 0;
	private static final int MENU_PAUSE = 1;
	private static final int MENU_SEED = 2;
	private static final int MENU_PREFS = 3;
	private static final int MENU_HELP = 4;

	private AlertDialog mSeedDialog;
	private AlertDialog mInfoDialog;

	private GameView mGameView;
	private Menu mMenu;
	private TextView mGenText;
	private TextView mTypeText;
	private TextView mPopText;
	private ImageView mStatusImage;
	private LinearLayout mMainLayout;
	private Prefs mPrefs;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_TYPE_WHAT:
				String type = (String) msg.obj;
				mTypeText.setText("Type: " + type);
				break;
			case UPDATE_GEN_WHAT:
				int gen = (Integer) msg.obj;
				mGenText.setText("Gen: " + gen);
				break;
			case UPDATE_POP_WHAT:
				int pop = (Integer) msg.obj;
				mPopText.setText("Pop: " + pop);
				break;
			case UPDATE_STATUS_WHAT:
				boolean playing = (Boolean) msg.obj;
				if (playing) {
					mStatusImage.setImageResource(R.drawable.play);
				} else {
					mStatusImage.setImageResource(R.drawable.pause);
				}
				break;
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		mMenu = menu;

		menu.add(0, MENU_SEED, 0, R.string.menu_seed).setIcon(
				android.R.drawable.ic_menu_share);
		menu.add(1, MENU_PLAY, 0, R.string.menu_play).setIcon(
				android.R.drawable.ic_media_play);
		menu.add(2, MENU_PAUSE, 0, R.string.menu_pause).setIcon(
				android.R.drawable.ic_media_pause);
		menu.add(3, MENU_PREFS, 0, R.string.menu_prefs).setIcon(
				android.R.drawable.ic_menu_preferences);
		menu.add(4, MENU_HELP, 0, R.string.menu_help).setIcon(
				android.R.drawable.ic_menu_help);

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (mGameView.isRunning()) {
			mMenu.setGroupVisible(0, false);
			mMenu.setGroupVisible(1, false);
			mMenu.setGroupVisible(2, true);
		} else if (mGameView.isSeeded()) {
			mMenu.setGroupVisible(0, true);
			mMenu.setGroupVisible(1, true);
			mMenu.setGroupVisible(2, false);
		} else {
			mMenu.setGroupVisible(0, true);
			mMenu.setGroupVisible(1, false);
			mMenu.setGroupVisible(2, false);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_PLAY:
			mGameView.start();
			return true;
		case MENU_PAUSE:
			mGameView.stop();
			return true;
		case MENU_SEED:
			AlertDialog.Builder builder = new SeedMenuDialog.Builder(this,
					mGameView);
			mSeedDialog = builder.create();
			mSeedDialog.show();
			mSeedDialog.setOwnerActivity(this);
			return true;
		case MENU_PREFS:
			Intent i = new Intent(this, PrefsActivity.class);
			startActivity(i);
			return true;
		case MENU_HELP:
			showDialog(INFO_DIALOG);
			return true;
		}

		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		mGameView = (GameView) findViewById(R.id.game);
		mGameView.setActivityHandler(mHandler);
		mTypeText = (TextView) findViewById(R.id.type_text);
		mGenText = (TextView) findViewById(R.id.generation_text);
		mPopText = (TextView) findViewById(R.id.population_text);
		mStatusImage = (ImageView) findViewById(R.id.status_image);
		mMainLayout = (LinearLayout) findViewById(R.id.main_layout);

		mPrefs = new Prefs(this);

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);

		//
		// seed sources
		//

		SeedSource.values = new ArrayList<SeedSource>();
		SeedSource sl;

		sl = new GeneratedSeedSource();
		SeedSource.values.add(sl);
		sl = new Life106AssetSeedSource(getAssets());
		SeedSource.values.add(sl);
		sl = new Life106SDCardSeedSource();
		SeedSource.values.add(sl);
	}

	@Override
	protected void onPause() {
		super.onPause();

		mGameView.stop();

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!mGameView.isSeeded()) {
			Toast.makeText(this,
					"Life is not seeded. Select Menu>Seed to begin.",
					Toast.LENGTH_LONG).show();
		}
	}

	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder;

		switch (id) {
		case INFO_DIALOG:
			builder = new InfoDialog.Builder(this);
			mSeedDialog = builder.create();
			return mSeedDialog;
		}
		return null;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			if (mGameView.isRunning()) {
				mGameView.stop();
			} else if (mGameView.isSeeded()) {
				mGameView.start();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
		Log.d(getClass().getSimpleName(), "pref changed:" + key);
		if (key.equals("keepScreenOn")) {
			mMainLayout.setKeepScreenOn(mPrefs.isKeepScreenOn());
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		}
	}
}
