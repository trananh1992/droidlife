package org.jtb.droidlife;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.util.Log;

public class Life106SDCardSeedSource extends SDCardSeedSource {
	public Life106SDCardSeedSource() {
		super(SDCARD_PREFIX + "life106");
	}

	public ArrayList<Seeder> getSeeders() {
		ArrayList<Seeder> seeders = new ArrayList<Seeder>();
		try {
			String[] fileNames = new File(path).list();
			if (fileNames != null) {
				for (int i = 0; i < fileNames.length; i++) {
					InputStream is = new FileInputStream(path + "/"
							+ fileNames[i]);
					Seeder seeder = new Life106Seeder(fileNames[i], is);
					seeders.add(seeder);
				}
			}
		} catch (IOException e) {
			Log.w(getClass().getSimpleName(), e);
		}
		return seeders;
	}
}
