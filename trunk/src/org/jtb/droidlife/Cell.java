package org.jtb.droidlife;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Cell {
	private static Random RANDOM = new Random(System.currentTimeMillis());
	private static Cell[] NEIGHBORS = new Cell[8];
	private static float COLOR_FACTOR = 1.5f;

	private static Paint CIRCLE_PAINT;

	static {
		CIRCLE_PAINT = new Paint();
		CIRCLE_PAINT.setAntiAlias(true);
	}

	private int age = -1;
	private int x, y, size, cX, cY, radius;
	private World world;
	private int color = Color.WHITE;

	public Cell(World world, int x, int y,
			int size) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.size = size;

		cX = x * size;
		cY = y * size;
		radius = size / 2;
	}

	public Cell(Cell c) {
		this.world = c.world;
		this.x = c.x;
		this.y = c.y;
		this.size = c.size;
		this.age = c.age;
		this.color = c.color;

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

	public void spawn() {
		age = 0;

		int r = RANDOM.nextInt(0xFF);
		int g = RANDOM.nextInt(0xFF);
		int b = RANDOM.nextInt(0xFF);

		color = Color.rgb(r, g, b);
	}

	public void spawn(int color) {
		age = 0;
		this.color = color;
	}

	public void die() {
		age = -1;
	}

	private int getColor() {
		return color;
	}

	public void draw(Canvas canvas) {
		if (!isLiving()) {
			return;
		}
		CIRCLE_PAINT.setColor(color);
		canvas.drawCircle(cX, cY, radius, CIRCLE_PAINT);
	}

	public void generate(World world, Cell[] current, Cell[] previous,
			int[] birthRule, int[] survivalRule) {
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
					int bc = birthColor(count);
					spawn(bc);
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

	private int birthColor(int count) {
		int rSum = 0, gSum = 0, bSum = 0;

		for (int i = 0; i < NEIGHBORS.length; i++) {
			if (!NEIGHBORS[i].isLiving()) {
				continue;
			}
			int c = NEIGHBORS[i].getColor();
			int r = Color.red(c);
			int g = Color.green(c);
			int b = Color.blue(c);

			rSum += r / count;
			gSum += g / count;
			bSum += b / count;
		}

		int fr = rSum;
		int fg = gSum;
		int fb = bSum;

		if (rSum > gSum) {
			fr *= COLOR_FACTOR;
			fg /= COLOR_FACTOR;
		}
		if (rSum > bSum) {
			fr *= COLOR_FACTOR;
			fb /= COLOR_FACTOR;
		}
		if (gSum > rSum) {
			fg *= COLOR_FACTOR;
			fr /= COLOR_FACTOR;
		}
		if (gSum > bSum) {
			fg *= COLOR_FACTOR;
			fb /= COLOR_FACTOR;
		}
		if (bSum > rSum) {
			fb *= COLOR_FACTOR;
			fr /= COLOR_FACTOR;
		}
		if (bSum > gSum) {
			fb *= COLOR_FACTOR;
			fg /= COLOR_FACTOR;
		}

		fr = Math.min(0xff, fr);
		fg = Math.min(0xff, fg);
		fb = Math.min(0xff, fb);

		return Color.rgb(fr, fg, fb);
	}
}
