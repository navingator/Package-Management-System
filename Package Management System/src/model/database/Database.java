package model.database;

import util.FileIO;
import util.Package;
import util.Person;
import util.Pair;
import util.PropertyHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Logger;

import model.IModelToViewAdaptor;

/*
 * Class that handles operations to and from the data files and DBMaps
 */

public class Database {
	
	IModelToViewAdaptor viewAdaptor;
	
	private DBMaps dbMaps;
	private DBFileIO dbIO;
	
	private String packageDirPath;
	private String currentDirPath;
	private String archiveDirPath;
	
	private Logger logger;

	public Database(IModelToViewAdaptor viewAdaptor) {
		
		this.viewAdaptor = viewAdaptor;
		
		String progDirPath = PropertyHandler.getInstance().getProperty("program_directory");
		this.packageDirPath = progDirPath + "/packages";
		this.currentDirPath = packageDirPath + "/current";
		this.archiveDirPath = packageDirPath + "/archive";

		this.logger = Logger.getLogger(Database.class.getName());
		
		this.dbMaps = new DBMaps();
		this.dbIO = new DBFileIO();
	}
	
	/**
	 * Function whose start is controlled by the controller
	 * 
	 * Creates new package directories if they do not exist
	 * Reads the current database, initializing the DBMaps
	 */
	public void start() {
		// check if rootFolder and subfolders exist, create if they do not.
		FileIO.makeDirs(new String[] {packageDirPath, currentDirPath, archiveDirPath});
		
		// read the active package database
		readCurrentDatabase();
	}
	
	/**
	 * Checks in a package into the system, adding it to the dbMaps
	 * @param personID			ID of the owner
	 * @param pkg				package object to be added
	 * @return					Success of checking in package
	 */
	public boolean checkInPackage(String personID, Package pkg) {
		// check if package already exists
		long pkgID = pkg.getPackageID();
		if(dbMaps.getPackage(pkgID) != null) {
			logger.warning("Package ID: " + pkgID + " was already checked in.");
			return false;
		}
		// modify current DBMaps
		dbMaps.addPackage(personID, pkg);
		// write new person file
		writePersonFile(personID,currentDirPath);
		
		return true;
	}
	
	/**
	 * Sets a check out date for the package
	 * @param pkgID				Package to check out
	 * @return					Success of checking out package
	 */
	public boolean checkOutPackage(long pkgID) {
		// modify package checkOut date
		Package pkg = dbMaps.getPackage(pkgID);
		if(pkg.getCheckOutDate() != null) {
			logger.info("Package ID: " + pkgID + " was already checked out.");		
			return false;
		} 		

		// note that this automatically edits the package in DBMaps
		pkg.setCheckOutDate(new Date());
		
		// write to file
		writePersonFile(dbMaps.getOwnerID(pkgID),currentDirPath);
		
		return true;
	}
	
	/**
	 * Edits a package in the database, editing the DBMaps and
	 * writing the changes to the owner's file
	 * @param newPerson			Package object containing new attributes for the package
	 * @return					Success of editing the package
	 */
	public boolean editPackage(Package pkg) {
		long pkgID = pkg.getPackageID();
		if(dbMaps.getPackage(pkgID) == null) {
			logger.warning("Package (ID: " + pkgID + ") to be edited by database not found.");
			return false;
		}
		
		// edit person in database maps and write to file
		dbMaps.editPackage(pkg);
		writePersonFile(dbMaps.getOwnerID(pkgID), currentDirPath);
		return true;
	}

	/**
	 * Returns a list of filtered and sorted packages, 
	 * according to the filter and sort string
	 * 
	 * filter should be written with all filters in the format
	 * 		field1=value1:field2=value2:field3=value3
	 * etc., where fields and values are defined as follows
	 * 	Fields with accompanying values:
	 * 		checked_in			
	 * 			<boolean>		Include only packages that have not been checked out
	 * 		person_name
	 * 			<String>		User input string to search Person firstName and LastName		
	 * 		on_date
	 * 			<String>		YYYYMMDD Date to get entries checked-in on
	 *		before_date			
	 * 			<String>		YYYYMMDD Date to get entries checked-in before
	 * 		after_date
	 * 			<String>		YYYYMMDD Date get entries checked-in after
	 * 
	 * sort should be written with highest priority sorts first
	 * in the format
	 * 		field1=value1:field2=value2:field3=value3
	 * 
	 * Where fields and values are defined as follows
	 * 	Fields:
	 * 		last_name			Person last name
	 * 		first_name			Person first name
	 * 		person_ID			Person personID
	 * 		package_ID			Package packageID
	 * 		check_in_date		Package check in date
	 * 		check_out_date		Package check out date
	 * 	Values:
	 * 		ASCENDING
	 * 		DESCENDING
	 * 
	 * @param filter			String containing filtering options
	 * @param sort				String containing sort options
	 */
	
	public ArrayList<Pair<Person,Package>> getEntries(String filter, String sort) {
		
		// get all of the entries from the database
		ArrayList<Pair<Person,Package>> result = dbMaps.getAllEntries();
		DBFormat.filter(result, filter);
		DBFormat.sort(result, sort);
		
		return result;
	}
	
	/**
	 * Returns a list of all people whose name contains searchString
	 * Uses the java String contains function
	 * @param searchString		String containing user input search
	 * @return					ArrayList of people with searchString in their
	 * 							lastName, firstName representation
	 */
	public ArrayList<Person> getPersonList(String searchString) {
		ArrayList<Person> result = new ArrayList<Person>();
		ArrayList<Person> allPersons = getAllCurrentPersons();
		
		//convert searchString to lower case for searching
		//internet says this is locale specific, beware
		searchString = searchString.toLowerCase();
		
		for (Person person: allPersons) {
			if(person.getLastFirstName().toLowerCase().contains(searchString)) {
				result.add(person);
			}
		}
		
		return result;
	}
	
	/**
	 * Returns a package from the package ID
	 * @param pkgID				ID of package to retrieve
	 * @return					Package object with given ID
	 */
	public Package getPackage(long pkgID) {
		return dbMaps.getPackage(pkgID);
	}
	
	/**
	 * Returns a person from the person ID
	 * @param personID			ID of the person to retrieve
	 * @return					Person object with given ID
	 */
	public Person getPerson(String personID) {
		return dbMaps.getPerson(personID);
	}
	
	/**
	 * Returns a person from a pkgID
	 * @param pkgID				ID of the package to retrieve the person
	 * @return					Package object
	 */
	public Person getOwner(long pkgID) {
		return dbMaps.getPerson(dbMaps.getOwnerID(pkgID));
	}
	
	/**
	 * Adds a person to the database maps and writes a file for the person
	 * 
	 * If the person is already in the archive, their file will be moved and their
	 * data will be read from the archive file.
	 * 
	 * If the person is not in the archive, a new person will be added to the database maps
	 * and a new file will be written for them.
	 * 
	 * @param person			Person object containing new person information
	 * @return					Success of adding the person
	 */
	public boolean addPerson(Person person) {
		//Check if person is already in the system
		String personID = person.getPersonID();
		if(dbMaps.getPerson(personID) != null) {
			logger.warning("Person (ID: " + personID + ") to be added by database already exists.");
			return false;
		} 
		
		//Check if person is in the archive
		HashSet<String> archiveFileNames = new HashSet<String>(FileIO.getFileNamesFromDirectory(archiveDirPath));
		HashSet<String> archivePersonIDs = archiveFileNames;
		
		//If person file is in archive, add file to DBMaps and delete archive file
		if(archivePersonIDs.contains(personID)) {
			String archiveFile = archiveDirPath + '/' + personID;
			addPersonPackagesFromFile(archiveFile);
			FileIO.deleteFile(archiveFile);
			dbMaps.editPerson(person); //edit the person instead of adding
		} else {
			//If not in the archive, add new person to DBMaps
			dbMaps.addPerson(person);
		}
		
		//Write the new file
		writePersonFile(personID, currentDirPath);
		
		return true;
		 
	}
	
	/**
	 * Edits a person in the database, editing the DBMaps and
	 * writing the changes to their file
	 * @param newPerson			Person object containing new attributes for the person
	 * @return					Success of editing the person
	 */
	public boolean editPerson(Person newPerson) {
		String personID = newPerson.getPersonID();
		if(dbMaps.getPerson(personID) == null) {
			logger.warning("Person (ID: " + personID + ") to be edited by database not found.");
			return false;
		}
		
		// edit person in database maps and write to file
		dbMaps.editPerson(newPerson);
		writePersonFile(personID, currentDirPath);
		return true;
	}
	
	/**
	 * Moves a person from the current directory to the archive directory and deletes
	 * their information from the DBMaps
	 * @param personID			ID of the person to be deleted
	 * @return					Success of deleting the person
	 */
	public boolean deletePerson(String personID) {
		if(dbMaps.getPerson(personID) == null) {
			logger.warning("Person (ID: " + personID + ") to be deleted by database not found.");
			return false;
		}
		
		// move person file to the archive 
		writePersonFile(personID, archiveDirPath);
		FileIO.deleteFile(currentDirPath + '/' + personID);
		// remove person from DBMaps - must be after reading and writing file
		dbMaps.deletePerson(personID);
		
		return true;
		
	}
	
	/* Return a list of all persons in the current directory */
	private ArrayList<Person> getAllCurrentPersons() {
		return dbMaps.getAllPersons();
	}
	
	/* returns a list of all packages in the current directory */
	private ArrayList<Package> getAllCurrentPackages() {
		return dbMaps.getAllPackages();
	}
	
	/*
	 * Writes a json file containing a person object and all of its associated package objects
	 * to the directory indicated by baseDirectory
	 */
	private void writePersonFile(String personID, String baseDirectory) {
		// create the person and package pair from the DBMaps object
		Person person = dbMaps.getPerson(personID);
		ArrayList<Long> packageIDs = dbMaps.getOwnedPackageIDs(personID);
		ArrayList<Package> pkgList = new ArrayList<Package>();
		for (long pkgID: packageIDs) {
			pkgList.add(dbMaps.getPackage(pkgID));
		}
		
		Pair<Person,ArrayList<Package>> dbPair = 
				new Pair<Person,ArrayList<Package>>(person,pkgList);
		
		//write Pair object to file
		String fileName = baseDirectory + '/' + personID;
		try {
			dbIO.writeDatabaseJSONFile(dbPair, fileName);
		} catch (FileNotFoundException e) {
			logger.warning("Failed to find file: " + fileName);	
		}catch(IOException e) {
			logger.warning("Failed to write " + fileName);
			e.printStackTrace();
		}
	}
	
	/*
	 * Reads a json file containing a person object and all of its associated package objects
	 * from fileName into a Pair object
	 */
	private Pair<Person,ArrayList<Package>> readPersonFile(String fileName) {
		Pair<Person,ArrayList<Package>> dbPair = null;
		try {
			 dbPair = dbIO.readDatabaseJSONFile(fileName);
		} catch (FileNotFoundException e) {
			logger.warning("Failed to find file: " + fileName);	
		} catch (IOException e) {
			logger.warning("Failed to read " + fileName);
			e.printStackTrace();
		} 
		
		return dbPair;
		
	}
	
	/*
	 * Initializes the current database by reading all of the files in the current directory
	 * and placing all of their attributes into the database maps
	 */
	private void readCurrentDatabase() {
		ArrayList<String> currentFileNames = FileIO.getFileNamesFromDirectory(currentDirPath);
		for (String fileName: currentFileNames) {
			addPersonPackagesFromFile(currentDirPath+'/'+fileName);
		}
	}
	
	/*
	 * Reads and adds all of the person and package information from the fileName to database maps
	 * Note: *Does not rewrite the file, make sure that calling function will write file*
	 */
	private void addPersonPackagesFromFile(String fileName) {
		Pair<Person,ArrayList<Package>> dbPair = readPersonFile(fileName);
		Person person = dbPair.first;
		ArrayList<Package> packages = dbPair.second;
		
		dbMaps.addPerson(person);
		for (Package pkg: packages) {
			dbMaps.addPackage(person.getPersonID(), pkg);
		}
	}
		 
	public boolean importPersonsFromCSV(String filePath) {
		// Get a list of all people in the database
		ArrayList<String> currentPersons = dbMaps.getAllPersonIDs();
		
		// Remove everyone from the database
		for(String personID: currentPersons) {
			deletePerson(personID);
		}
		
		// Get a list of all people in the file
		ArrayList<Person> csvPersons = new ArrayList<Person>();
		ArrayList<Pair<String,String>> failedToRead = new ArrayList<Pair<String,String>>();
		try {
			csvPersons = dbIO.readDatabaseCSVFile(filePath,failedToRead);
		} catch (FileNotFoundException e) {
			logger.severe("Failed to find file: " + filePath);
			viewAdaptor.displayError("Failed to find file: " + filePath, "Cannot Find File");
		} catch (IOException e) {
			logger.severe("Failed to read file:" + filePath);
			viewAdaptor.displayError("Failed to read file: " + filePath, "Error");
		} catch (FileFormatException e) {
			logger.warning("Invalid csv file format for file: " + filePath);
			viewAdaptor.displayError("Invalid file format for file: " + filePath +
					"\n Please ensure the file has a header and follows the format: " +
					"\n Last Name, First Name, Email, ID", "Invalid File Format");
		}
		
		if(failedToRead.size() != 0) {
			String errorMsg = "\nFailed to read the following people: \n" + 
					"-------------------------------------\n"
					+ "Person - reason\n" + 
					"-------------------------------------\n";
			for (Pair<String,String> error: failedToRead) {
				errorMsg = errorMsg + error.first + " - " + error.second + '\n';
			}
			logger.warning(errorMsg);
			viewAdaptor.displayWarning(errorMsg, "Warning");
		}	
		
		// Add all people to the database
		for(Person person: csvPersons) {
			addPerson(person);
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		Database db = new Database(null);
		db.start();
		System.out.println("Start:");
		System.out.println("current persons = " + db.getAllCurrentPersons().toString());
		System.out.println("current active entries = " + db.getEntries("", "").toString());
		System.out.println("current packages = " + db.getAllCurrentPackages().toString() + '\n');
		
		Date now = new Date();
		
		db.importPersonsFromCSV("testFiles/Test Roster.csv");
		
		Package p1 = new Package(123+now.getTime(),"",now);
		Package p2 = new Package(234+now.getTime(),"It's huge.\n Get it out now.",new Date(now.getTime()-100000000));
		Package p3 = new Package(309435+now.getTime(),"",new Date(now.getTime()-200000000));
		Package p4 = new Package(1+now.getTime(),"",new Date(now.getTime()-300000000));
		Package p5 = new Package(2+now.getTime(),"",new Date(now.getTime()-400000000));

		Person navin = new Person("Pathak", "Navin", "np8@rice.edu", "np8");
		Person chris = new Person("Henderson", "Chris", "cwh1@rice.edu", "cwh1");
		Person christopher = new Person("Henderson", "Christopher", "cwh1@rice.edu", "cwh1");
		Person ambi = new Person("Bobmanuel", "Ambi", "ajb6@rice.edu", "ajb6");
		
		db.addPerson(navin);
		db.addPerson(chris);
		db.addPerson(ambi);

		System.out.println("Added Persons:");
		System.out.println("current persons = " + db.getAllCurrentPersons().toString());
		System.out.println("current entries = " +db.getEntries("","").toString());
		System.out.println("current packages = " + db.getAllCurrentPackages().toString() + '\n');
		
		db.checkInPackage(navin.getPersonID(),p1);
		System.out.println("Checked In1:");
		System.out.println("current persons = " + db.getAllCurrentPersons().toString());
		System.out.println("current entries = " +db.getEntries("","").toString());
		System.out.println("current packages = " + db.getAllCurrentPackages().toString() + '\n');
		db.checkInPackage(ambi.getPersonID(),p2);
		db.checkInPackage(chris.getPersonID(),p3);
		System.out.println("Checked In3:");
		System.out.println("current persons = " + db.getAllCurrentPersons().toString());
		System.out.println("current entries = " +db.getEntries("","").toString());
		System.out.println("current packages = " + db.getAllCurrentPackages().toString() + '\n');
		db.checkInPackage(chris.getPersonID(),p4);
		db.checkInPackage(navin.getPersonID(),p5);
		
		System.out.println("Checked In5:");
		System.out.println("current persons = " + db.getAllCurrentPersons().toString());
		System.out.println("current entries = " +db.getEntries("","").toString());
		System.out.println("current packages = " + db.getAllCurrentPackages().toString() + '\n');
		
		db.checkOutPackage(p5.getPackageID());
		db.checkOutPackage(p4.getPackageID());
		db.checkOutPackage(p2.getPackageID());
		db.checkOutPackage(p5.getPackageID());

		System.out.println("Checked Out:");
		System.out.println("current persons = " + db.getAllCurrentPersons().toString());
		System.out.println("current entries = " +db.getEntries("","").toString());
		System.out.println("current packages = " + db.getAllCurrentPackages().toString() + '\n');
		
		db.deletePerson(navin.getPersonID());
		db.editPerson(christopher);
		
		System.out.println("current persons = " + db.getAllCurrentPersons().toString());
		System.out.println("current entries = " +db.getEntries("","").toString());
		System.out.println("current packages = " + db.getAllCurrentPackages().toString());
		
	}
	
}
