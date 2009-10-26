package org.jtb.droidlife;

import java.util.ArrayList;
import java.util.Arrays;

public class GeneratedSeedSource extends SeedSource {
	private static ArrayList<Seeder> SEEDERS = new ArrayList<Seeder>();
	
	static {
		SEEDERS.add(new RandomSeeder("Random"));
	}
	
	@Override
	public ArrayList<Seeder> getSeeders() {
		return SEEDERS;
	}

}
