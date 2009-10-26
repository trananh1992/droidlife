package org.jtb.droidlife;

import java.util.Random;

import android.content.Context;

public abstract class Seeder implements Comparable<Seeder> {
	protected static Random RANDOM = new Random(System.currentTimeMillis());

	protected String name;
	
	public Seeder(String name) {
		this.name = name;
	}
	public abstract void seed(World world);
	
	public SeederDialog.Builder getSeederDialogBuilder(Context context, GameView gameView) {
		return null;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
		
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Seeder other = (Seeder) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public int compareTo(Seeder other) {
		return name.compareTo(other.name);
	}
	
}
