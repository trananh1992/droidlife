package org.jtb.droidlife;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Cell {
	private static Random RANDOM = new Random(System.currentTimeMillis());

	static int PHENOTYPES_SIZE = 7;

	private static Paint CIRCLE_PAINT;
	private static Paint POINT_PAINT;

	static {
		CIRCLE_PAINT = new Paint();
		CIRCLE_PAINT.setAntiAlias(true);

		POINT_PAINT = new Paint();
		POINT_PAINT.setAntiAlias(true);
		POINT_PAINT.setStyle(Paint.Style.STROKE);
	}

	private int age = -1;
	private int x, y, size;
	private int phenotype = -1;
	private int[] birthNeighbors, surviveNeighbors;
	private World world;

	public Cell(World world, int[] birthNeighbors, int[] surviveNeighbors,
			int x, int y, int size) {
		this.world = world;
		this.surviveNeighbors = surviveNeighbors;
		this.birthNeighbors = birthNeighbors;
		this.x = x;
		this.y = y;
		this.size = size;
	}

	public Cell(Cell c) {
		this.world = c.world;
		this.surviveNeighbors = c.surviveNeighbors;
		this.birthNeighbors = c.birthNeighbors;
		this.x = c.x;
		this.y = c.y;
		this.size = c.size;
		this.age = c.age;
		this.phenotype = c.phenotype;
	}

	public int getAge() {
		return age;
	}

	public boolean isLiving() {
		return age != -1;
	}

	public void spawn(int phenotype) {
		this.phenotype = phenotype;
		age = 0;
	}

	public void die() {
		age = -1;
		phenotype = -1;
	}

	private int getColor() {
		switch (phenotype) {
		case 0:
			return Color.WHITE;
		case 1:
			return Color.GREEN;
		case 2:
			return Color.BLUE;
		case 3:
			return Color.RED;
		case 4:
			return Color.YELLOW;
		case 5:
			return Color.MAGENTA;
		case 6:
			return Color.CYAN;
		default:
			return Color.WHITE;
		}
	}

	public void draw(Canvas canvas) {
		if (!isLiving()) {
			return;
		}
		if (size > 2) {
			CIRCLE_PAINT.setColor(getColor());
			canvas.drawCircle(x * size, y * size, size / 2, CIRCLE_PAINT);
		} else {
			POINT_PAINT.setColor(getColor());
			POINT_PAINT.setStrokeWidth(size);
			canvas.drawPoint(x * size, y * size, POINT_PAINT);
		}
	}

	public void generate(World world, Cell[] current, Cell[] previous) {
		if (isLiving()) {
			age++;
		}

		Cell[] neighbors = neighbors(world, current, previous);
		int count = living(neighbors);

		if (isLiving()) {
			boolean survive = false;
			for (int k = 0; k < surviveNeighbors.length; k++) {
				if (count == surviveNeighbors[k]) {
					survive = true;
					break;
				}
			}
			if (!survive) {
				die();
			}
		} else {
			for (int k = 0; k < birthNeighbors.length; k++) {
				if (count == birthNeighbors[k]) {
					phenotype = dominantPhenotype(neighbors);
					spawn(phenotype);
					break;
				}
			}
		}

	}

	private Cell[] neighbors(World world, Cell[] current, Cell[] previous) {
		Cell[] neighbors = new Cell[8];

		neighbors[0] = previous[y - 1];
		neighbors[1] = previous[y];
		neighbors[2] = previous[y + 1];

		neighbors[3] = current[y - 1];
		neighbors[4] = current[y + 1];

		neighbors[5] = world.cells[x + 1][y - 1];
		neighbors[6] = world.cells[x + 1][y];
		neighbors[7] = world.cells[x + 1][y + 1];

		return neighbors;
	}

	private int living(Cell[] neighbors) {
		int count = 0;

		for (int i = 0; i < neighbors.length; i++) {
			if (neighbors[i].isLiving()) {
				count++;
			}
		}

		return count;
	}

	private int dominantPhenotype(Cell[] neighbors) {
		int[] phenotypes = new int[PHENOTYPES_SIZE];

		for (int i = 0; i < neighbors.length; i++) {
			if (neighbors[i].isLiving()) {
				phenotypes[neighbors[i].phenotype]++;
			}
		}

		int mIndex = 0, max = 0;

		if (RANDOM.nextBoolean()) {
			for (int i = 0; i < phenotypes.length; i++) {
				if ((phenotypes[i] > max)
						|| (phenotypes[i] == max && RANDOM.nextBoolean())) {
					max = phenotypes[i];
					mIndex = i;
				}
			}
		} else {
			for (int i = phenotypes.length - 1; i >= 0; i--) {
				if ((phenotypes[i] > max)
						|| (phenotypes[i] == max && RANDOM.nextBoolean())) {
					max = phenotypes[i];
					mIndex = i;
				}

			}
		}

		return mIndex;
	}
}
