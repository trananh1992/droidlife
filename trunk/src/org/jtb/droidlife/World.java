package org.jtb.droidlife;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Canvas;
import android.util.Log;

public class World {
	private static final Pattern RULE1_PATTERN = Pattern
			.compile("[bB](\\d+)/[sS](\\d+)");
	private static final Pattern RULE2_PATTERN = Pattern
			.compile("(\\d+)/(\\d+)");

	Cell[][] cells;
	private Cell[] current;
	private Cell[] previous;
	private int[] birthNeighbors;
	private int[] surviveNeighbors;
	private int generation = 0;
	private int population = 0;
	private int cellSize;

	public void clear() {
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				cells[i][j].die();
			}
		}
	}

	private void init() {
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				cells[i][j] = new Cell(this, i, j, cellSize);
			}
		}
	}

	public int getGeneration() {
		return generation;
	}

	public int getPopulation() {
		return population;
	}

	public void incPopulation() {
		population++;
	}

	public World(int xMax, int yMax, int cellSize, int[] birthNeighbors,
			int[] surviveNeighbors) {
		this.cellSize = cellSize;

		cells = new Cell[xMax + 2][yMax + 2];
		current = new Cell[yMax + 2];
		previous = new Cell[yMax + 2];
		this.birthNeighbors = birthNeighbors;
		this.surviveNeighbors = surviveNeighbors;

		init();
	}

	public void draw(Canvas canvas) {
		for (int i = 1; i < cells.length - 1; i++) {
			for (int j = 1; j < cells[i].length - 1; j++) {
				cells[i][j].draw(canvas);
			}
		}
	}

	public void generate() {
		copy(cells[0], previous);

		population = 0;
		Cell[] tmp;
		
		for (int i = 1; i < cells.length - 1; i++) {
			copy(cells[i], current);
			Cell cell;
			for (int j = 1; j < cells[i].length - 1; j++) {
				cell = cells[i][j];
				cell.generate(this, current, previous, birthNeighbors, surviveNeighbors);
				if (cell.isLiving()) {
					population++;
				}
			}
			tmp = previous;
			previous = current;
			current = tmp;
		}

		generation++;
	}

	private void copy(Cell[] src, Cell[] dest) {
		for (int i = 0; i < src.length; i++) {
			dest[i] = new Cell(src[i]);
		}
	}

	public String getRule() {
		StringBuilder sb = new StringBuilder();
		sb.append('b');
		for (int i = 0; i < birthNeighbors.length; i++) {
			sb.append(birthNeighbors[i]);
		}
		sb.append('s');
		for (int i = 0; i < surviveNeighbors.length; i++) {
			sb.append(surviveNeighbors[i]);
		}

		return sb.toString();
	}

	public void setRule(String rule) {
		if (rule == null || rule.length() == 0) {
			return;
		}
		
		Matcher m1 = RULE1_PATTERN.matcher(rule);
		Matcher m2 = RULE2_PATTERN.matcher(rule);

		if (m1.matches()) {
			birthNeighbors = toIntArray(m1.group(1));
			surviveNeighbors = toIntArray(m1.group(2));
		} else if (m2.matches()) {
			surviveNeighbors = toIntArray(m2.group(1));
			birthNeighbors = toIntArray(m2.group(2));
		} else {
			Log.e(getClass().getSimpleName(), "could not parse rule: " + rule);
		}
	}

	private int[] toIntArray(String s) {
		int[] ia = new int[s.length()];
		for (int i = 0; i < s.length(); i++) {
			ia[i] = s.charAt(i) - '0';
		}
		return ia;
	}
}
