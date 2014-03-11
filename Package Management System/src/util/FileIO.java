package util;

import java.io.File;

public class FileIO {
	
	/*
	 * Wrappers for the java.io package functions
	 */
	
	/*
	 * Make directories, checking if they already exist
	 */
	public static void makeDirs(String[] dirs) {
		for (String dName: dirs) {
			File dir = new File(dName);
			if(!dir.exists()) {
				dir.mkdirs();
			}
		}
	}

	public static void makeDirs(String dir) {
		makeDirs(new String[] {dir});
	}
}
