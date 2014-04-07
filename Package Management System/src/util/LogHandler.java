package util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * Class that creates settings for the parent log "" and cleans logs
 * @author Navin
 *
 */
public class LogHandler {

	private Logger logger;
	private String logDirPath;
	
	public LogHandler() {
		this.logger = Logger.getLogger("");
		logger.setLevel(Level.INFO);
	}
	
	public void init(String progPath) {
		FileHandler fh;
		
	    try {  

	    	// get the file name
	    	SimpleDateFormat ft = new SimpleDateFormat("YYYY_MM_dd_HH_mm_ss");
	    	Date now = new Date();
	    	String logFileName = "PMS_" + ft.format(now) + ".log";
	    	
	    	// create the directory if it does not exist
	    	this.logDirPath = progPath + "/logs";
	    	File logDir = new File(logDirPath);
	    	if(!logDir.exists()) {
	    		logDir.mkdirs();
	    	}
	    	
	        // This block configure the logger with handler and formatter
	    	
	        fh = new FileHandler(logDirPath + '/' + logFileName);  
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);   

	    } catch (SecurityException e) {
	        e.printStackTrace();  
	    } catch (IOException e) {
	        e.printStackTrace();  
	    } 	
	}
	
	/**
	 * Deletes logs that are over 1 month old
	 */
	public void cleanLogs() {
		
		Date now = new Date();
		ArrayList<String> fileNames = FileIO.getFileNamesFromDirectory(logDirPath);
		for(String fileName: fileNames) {
			File logFile = new File(logDirPath + '/' +  fileName);
			Date lastModified = new Date(logFile.lastModified());
			
			final long MONTH = 2592000000L;
			if (now.getTime() - MONTH > lastModified.getTime()) {
				logFile.delete();
			}
		}
	}
	
}
