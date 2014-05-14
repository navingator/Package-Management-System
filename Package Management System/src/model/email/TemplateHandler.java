package model.email;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.FileIO;
import model.IModelToViewAdaptor;

public class TemplateHandler {

	static IModelToViewAdaptor viewAdaptor;
	static String headers = 
			"NOTIFICATION-SUBJECT|NOTIFICATION-BODY|REMINDER-SUBJECT|REMINDER-BODY|SENDER-ALIAS|AUTO-LINEBREAK";
	
	public static void setViewAdaptor (IModelToViewAdaptor _viewAdaptor) { viewAdaptor = _viewAdaptor; }
	
	public static HashMap<String,String> getTemplates(boolean convert) {
		String RootDir = FileIO.getRootDir();
		HashMap<String,String> result = new HashMap<String,String>();
		
		// Get raw file string
		String fileStr = getRawFile();
		
		// Remove all C-style block comments
		fileStr = fileStr.replaceAll("(?s)/\\*.*?\\*/", "");
		
		// Create matcher for the text surrounded by headers
		Matcher m = Pattern.compile("("+headers+"):"+ "(.*?)" + "("+headers+"|\\z)", Pattern.DOTALL).matcher(fileStr);
		
		int start = 0;
		while (m.find(start)) {
			result.put(m.group(1), m.group(2).trim());	
			
			// Remember to start at the beginning of the last-captured group, in order to capture that section
			start = m.start(3);
		}
		
		// TODO - Ensure that templates where found for all headers.  Print Error otherwise.
		
		// Transform newlines into HTML <br> tags
		if (result.get("AUTO-LINEBREAK").equals("TRUE") && convert) {
			for (String key : result.keySet()) {
				result.put(key, result.get(key).replace(System.lineSeparator(), "<br>"));
			}
		}
		
//			for (String key : result.keySet()) {
//				System.out.format("%s:\n%s\n", key, result.get(key));
//			}
			
		return result;
	}

	public static String getRawFile() {
		String RootDir = FileIO.getRootDir();
		String filePath = RootDir + "/email-template.txt";
		
		String result = null;
		
		// Read entire file into string
		try {
			result = FileIO.loadFileAsString(filePath);
		} catch (IOException e) {
			viewAdaptor.displayError("Could not read Email template file.  Ensure that a template file exists in "+RootDir, "Could not read templates");
		}
		
		return result;
	}
	
	public static void writeRawFile(String newTemplate) {
		String RootDir = FileIO.getRootDir();
		String filePath = RootDir + "/email-template.txt";
		
		OutputStream os;
		try {
			os = new FileOutputStream(filePath, false);
			os.write(newTemplate.getBytes());
			os.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writeNewTemplates(Map<String, String> newTemplate) {
		String RootDir = FileIO.getRootDir();
		String filePath = RootDir + "/email-template.txt";

		try {
			BufferedWriter wr = new BufferedWriter(new FileWriter(filePath, false));
			
			for (String header : newTemplate.keySet()) {
				wr.write(header+":");
				wr.newLine();
				wr.write(newTemplate.get(header));
				wr.newLine();
				wr.newLine();
			}
			
			wr.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
