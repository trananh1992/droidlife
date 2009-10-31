package org.jtb.droidlife;

import java.util.Random;

import android.graphics.Canvas;

public class World {
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
				cells[i][j] = new Cell(this, birthNeighbors, surviveNeighbors, i, j, cellSize);
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
		for (int i = 1; i < cells.length - 1; i++) {
			copy(cells[i], current);
			for (int j = 1; j < cells[i].length - 1; j++) {
				Cell cell = cells[i][j];
				cell.generate(this, current, previous);
				if (cell.isLiving()) {
					population++;
				}
			}
			Cell[] tmp = previous;
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

	public String getType() {
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
}
