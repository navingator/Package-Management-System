package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileIO {
	
	private static String progRootDir;
	
	public static void init(String newRootDir) 
	{ 
		// Remember root dir
		progRootDir = newRootDir; 
	}
	
	public static String getRootDir() { return progRootDir; }
	
	public static String loadFileAsString(String filePath) throws IOException {
		// Read entire file into string
		byte[] encoded;
		encoded = Files.readAllBytes(Paths.get(filePath));
		return new String(encoded);
	}
	
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

	/*
	 * Same function for converting String into String[] for input
	 */
	public static void makeDirs(String dir) {
		makeDirs(new String[] {dir});
	}
	
	/*
	 * Returns all of the files from the directory
	 */

	public static ArrayList<String> getFileNamesFromDirectory(
			String dirName) {
		ArrayList<String> results = new ArrayList<String>();
		File[] files = new File(dirName).listFiles();
		
		for (File file: files) {
			if(file.isFile()) {
				results.add(file.getName());
			}
		}
		
		return results;
	}

	public static void deleteFile(String fileName) {
		File file = new File(fileName);
		file.delete();
	}
}
