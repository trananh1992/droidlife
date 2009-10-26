package org.jtb.droidlife;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Random;

import android.content.res.AssetManager;
import android.util.Log;

public abstract class SDCardSeedSource extends FileSeedSource {
	protected static final String SDCARD_PREFIX = "/sdcard/droidlife/";
	
	public SDCardSeedSource(String path) {
		super(path);
	}
}
