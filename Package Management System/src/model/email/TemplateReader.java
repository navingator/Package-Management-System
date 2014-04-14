package model.email;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.IModelToViewAdaptor;

public class TemplateReader {

	static IModelToViewAdaptor viewAdaptor;
	static String headers = 
			"NOTIFICATION-SUBJECT|NOTIFICATION-BODY|REMINDER-SUBJECT|REMINDER-BODY|SENDER-ALIAS|AUTO-LINEBREAK";
	
	public static void setViewAdaptor (IModelToViewAdaptor _viewAdaptor) { viewAdaptor = _viewAdaptor; }
	
	public static HashMap<String,String> getTemplates(String RootDir) {
		String filePath = RootDir + "/email-template.txt";
		HashMap<String,String> result = new HashMap<String,String>();
			
		try {
			// Read entire file into fileStr
			byte[] encoded = Files.readAllBytes(Paths.get(filePath));
			String fileStr = new String(encoded);
			
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
			if (result.get("AUTO-LINEBREAK").equals("TRUE")) {
				for (String key : result.keySet()) {
					result.put(key, result.get(key).replace(System.lineSeparator(), "<br>"));
				}
			}
			
//			for (String key : result.keySet()) {
//				System.out.format("%s:\n%s\n", key, result.get(key));
//			}
			
		} catch (IOException e) {
			viewAdaptor.displayError("Could not read Email template file.  Ensure that a template file exists in "+RootDir, "Could not read templates");
			return null;
		}
		
		return result;
	}
	
}
