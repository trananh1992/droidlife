package org.jtb.droidlife;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import android.util.Log;

public class Life106Seeder extends Seeder {
	private static class Point {
		int x, y;
	}
	
	private Reader reader;
	private ArrayList<Point> points;
	
	public Life106Seeder(String name, InputStream is) {
		super(name);
		this.reader = new InputStreamReader(is);
	}
	
	@Override
	public void seed(World world) {
		try {
			read();
			populate(world);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void read() throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(reader);

		points = new ArrayList<Point>();
		String line;

		while ((line = br.readLine()) != null) {
			if (line.startsWith("#")) {
				continue;
			}
			String[] sps = line.split("\\s");
			if (sps.length != 2) {
				Log.w(getClass().getSimpleName(), "invalid line: " + line);
				continue;
			}
			try {
				Point p = new Point();
				p.x = Integer.parseInt(sps[0]);
				p.y = Integer.parseInt(sps[1]);
				points.add(p);
			} catch (NumberFormatException nfe) {
				Log.w(getClass().getSimpleName(), nfe);
				continue;
			}
		}
	}

	private void populate(World world) {
		int xmax = world.cells.length-1;
		int ymax = world.cells[0].length-1;
		int xmid = xmax / 2;
		int ymid = ymax / 2;

		for (int i = 0; i < points.size(); i++) {
			int x = xmid + points.get(i).x;
			int y = ymid + points.get(i).y;

			if (x < 0 || x > xmax || y < 0 || y > ymax) {
				continue;
			}
			
			world.cells[x][y].spawn(RANDOM.nextInt(Cell.PHENOTYPES_SIZE));
		}
	}
}
