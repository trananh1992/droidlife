package org.jtb.droidlife;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Random;

import android.content.res.AssetManager;
import android.util.Log;

public class Life106AssetSeedSource extends AssetSeedSource {
	public Life106AssetSeedSource(AssetManager assetManager) {
		super(assetManager, "life106");
	}

	public ArrayList<Seeder> getSeeders() {
		ArrayList<Seeder> seeders = new ArrayList<Seeder>();
		try {
			String[] fileNames = assetManager.list(path);
			if (fileNames != null) {
				for (int i = 0; i < fileNames.length; i++) {
					Seeder seeder = new Life106Seeder(fileNames[i],
							assetManager.open(path + "/" + fileNames[i]));
					seeders.add(seeder);
				}
			}
		} catch (IOException e) {
			Log.w(getClass().getSimpleName(), e);
		}
		return seeders;
	}
}
