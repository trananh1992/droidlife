package org.jtb.droidlife;

import android.content.Context;

public class RandomSeeder extends Seeder {
	private int load = 5;
	
	public int getLoad() {
		return load;
	}

	public void setLoad(int load) {
		this.load = load;
	}

	public RandomSeeder(SeedSource seedSource) {
		super(seedSource, "Random");
	}

	public void seed(World world) {
		for (int i = 1; i < world.cells.length-1; i++) {
			for (int j = 1; j < world.cells[0].length-1; j++) {
				if (RANDOM.nextInt(load) == 0) {
					world.cells[i][j].spawn(RANDOM.nextInt(Cell.PHENOTYPES_SIZE));
				}
			}
		}
	}
	
	public SeederDialog.Builder getSeederDialogBuilder(Context context, GameView gameView) {
		RandomSeederDialog.Builder builder = new RandomSeederDialog.Builder(context, gameView, this);
		return builder;
	}
}
