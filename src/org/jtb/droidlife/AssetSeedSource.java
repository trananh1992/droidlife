package org.jtb.droidlife;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import android.content.res.AssetManager;
import android.util.Log;

public abstract class AssetSeedSource extends FileSeedSource {
	protected AssetManager assetManager;

	public boolean isWritable() {
		return false;
	}
	
	public AssetSeedSource(AssetManager assetManager, String path) {
		super(path);
		this.assetManager = assetManager;
	}
	
	protected String[] getNames() {
		try {
			String[] fileNames = assetManager.list(path);
			return fileNames;
		} catch (IOException e) {
			Log.e(getClass().getSimpleName(), "error getting names", e);
			return null;
		}
	}
	
	@Override
	public Reader getReader(String name) {
		try {
			InputStream is = assetManager.open(path + "/" + name);
			Reader r = new InputStreamReader(is);
			return r;
		} catch (IOException e) {
			Log.e(getClass().getSimpleName(), "could not open asset", e);
			return null;
		}
	}

}
