package org.jtb.droidlife;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Cell {
	static int PHENOTYPES_SIZE = 7;

	private static Random RANDOM = new Random(System.currentTimeMillis());
	private static Cell[] NEIGHBORS = new Cell[8];
	private static int[] PHENOTYPES = new int[PHENOTYPES_SIZE];

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
	private int x, y, size, cX, cY, radius;
	private int phenotype = -1;
	private int[] birthRule, survivalRule;
	private World world;
	
	public Cell(World world, int[] birthRule, int[] survivalRule, int x, int y,
			int size) {
		this.world = world;
		this.survivalRule = survivalRule;
		this.birthRule = birthRule;
		this.x = x;
		this.y = y;
		this.size = size;
		
		cX = x * size;
		cY = y * size;
		radius = size / 2;		
	}

	public Cell(Cell c) {
		this.world = c.world;
		this.survivalRule = c.survivalRule;
		this.birthRule = c.birthRule;
		this.x = c.x;
		this.y = c.y;
		this.size = c.size;
		this.age = c.age;
		this.phenotype = c.phenotype;		

		cX = x * size;
		cY = y * size;
		radius = size / 2;
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
		CIRCLE_PAINT.setColor(getColor());
		canvas.drawCircle(cX, cY, radius, CIRCLE_PAINT);
	}

	public void generate(World world, Cell[] current, Cell[] previous) {
		if (isLiving()) {
			age++;
		}

		findNeighbors(world, current, previous);
		int count = living();

		if (isLiving()) {
			boolean survive = false;
			for (int k = 0; k < survivalRule.length; k++) {
				if (count == survivalRule[k]) {
					survive = true;
					break;
				}
			}
			if (!survive) {
				die();
			}
		} else {
			for (int k = 0; k < birthRule.length; k++) {
				if (count == birthRule[k]) {
					phenotype = dominantPhenotype();
					spawn(phenotype);
					break;
				}
			}
		}

	}

	private void findNeighbors(World world, Cell[] current, Cell[] previous) {
		NEIGHBORS[0] = previous[y - 1];
		NEIGHBORS[1] = previous[y];
		NEIGHBORS[2] = previous[y + 1];

		NEIGHBORS[3] = current[y - 1];
		NEIGHBORS[4] = current[y + 1];

		NEIGHBORS[5] = world.cells[x + 1][y - 1];
		NEIGHBORS[6] = world.cells[x + 1][y];
		NEIGHBORS[7] = world.cells[x + 1][y + 1];
	}

	private int living() {
		int count = 0;

		for (int i = 0; i < NEIGHBORS.length; i++) {
			if (NEIGHBORS[i].isLiving()) {
				count++;
			}
		}

		return count;
	}

	private int dominantPhenotype() {
		for (int i = 0; i < PHENOTYPES.length; i++) {
			PHENOTYPES[i] = 0;
		}
		
		for (int i = 0; i < NEIGHBORS.length; i++) {
			if (NEIGHBORS[i].isLiving()) {
				PHENOTYPES[NEIGHBORS[i].phenotype]++;
			}
		}

		int mIndex = 0, max = 0;

		int start = RANDOM.nextInt(PHENOTYPES_SIZE);
		int i = start;
		boolean done = false;
		
		while (!done) {
			if ((PHENOTYPES[i] > max)
					|| (PHENOTYPES[i] == max && RANDOM.nextBoolean())) {
				max = PHENOTYPES[i];
				mIndex = i;
			}			
			
			i++;
			if (i == PHENOTYPES_SIZE) {
				i = 0;
			}
			if (i == start) {
				done = true;
			}
		}

		return mIndex;
	}
}
