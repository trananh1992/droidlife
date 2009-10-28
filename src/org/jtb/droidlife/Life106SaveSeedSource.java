package org.jtb.droidlife;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import android.util.Log;

public class Life106SaveSeedSource extends SDCardSeedSource {
	public Life106SaveSeedSource() {
		super("save/life106");
	}

	@Override
	protected Seeder newSeeder(String name) {
		return new Life106Seeder(this, name);
	}
}
