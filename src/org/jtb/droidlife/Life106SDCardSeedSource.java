package org.jtb.droidlife;

public class Life106SDCardSeedSource extends SDCardSeedSource {
	public Life106SDCardSeedSource() {
		super("life106");
	}

	@Override
	protected Seeder newSeeder(String name) {
		return new Life106Seeder(this, name);
	}

}
