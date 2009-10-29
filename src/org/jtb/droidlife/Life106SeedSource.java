package org.jtb.droidlife;

public class Life106SeedSource extends SDCardSeedSource {
	public Life106SeedSource() {
		super("life106");
	}

	@Override
	protected Seeder newSeeder(String name) {
		return new Life106Seeder(this, name);
	}

}
