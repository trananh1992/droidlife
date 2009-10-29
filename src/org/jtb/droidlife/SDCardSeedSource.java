package org.jtb.droidlife;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import android.util.Log;

public abstract class SDCardSeedSource extends FileSeedSource {
	protected static final String SDCARD_PREFIX = "/sdcard/droidlife/";

	public SDCardSeedSource(String path) {
		super(SDCARD_PREFIX + "/" + path);
	}

	public String[] getNames() {
		String[] fileNames = new File(path).list();
		return fileNames;
	}

	@Override
	public Reader getReader(String name) {
		Reader reader = null;
		File f = null;
		try {
			f = new File(path + "/" + name);
			reader = new FileReader(f);
		} catch (FileNotFoundException e) {
			Log.e(getClass().getSimpleName(), "could not open file: " + f, e);
		}
		return reader;
	}

	@Override
	public void writeSeed(String name, World world) {
		Writer w = null;
		try {
			File f = new File(path + "/" + name);
			w = new FileWriter(f);
			SeedWriter wr = new Life106Writer();
			wr.write(world, w);
		} catch (IOException e) {
			Log.e(getClass().getSimpleName(), "error saving file", e);
		} finally {
			try {
				if (w != null) {
					w.close();
				}
			} catch (IOException e) {
				Log.e(getClass().getSimpleName(), "error closing file", e);
			}
		}
	}

	@Override
	public void removeSeed(Seeder seeder) {
		File f = new File(path + "/" + seeder.getName());
		f.delete();
	}

}
