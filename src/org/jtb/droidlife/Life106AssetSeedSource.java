package org.jtb.droidlife;

import java.util.ArrayList;

import android.content.res.AssetManager;

public class Life106AssetSeedSource extends AssetSeedSource {

	public Life106AssetSeedSource(AssetManager assetManager) {
		super(assetManager, "life106");
	}

	protected Seeder newSeeder(String name) {
		return new Life106Seeder(this, name);
	}
}
