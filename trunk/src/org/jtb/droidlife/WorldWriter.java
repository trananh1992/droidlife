package org.jtb.droidlife;

import java.io.IOException;
import java.io.Writer;

public abstract class WorldWriter {
	public abstract void write(World world, Writer os) throws IOException;
}
