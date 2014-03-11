package model.database;

import util.FileIO;
import util.Package;
import util.Person;
import util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/*
 * Class that handles operations to and from the data files and DatabaseMaps
 */

public class Database {
	
	private DatabaseMaps dbMap;
	private String dataDirName;
	private String activeDirName;
	private String archiveDirName;
	
	public Database(String rootDirName) {
		this.dataDirName = rootDirName + "/data";
		this.activeDirName = dataDirName + "/active";
		this.archiveDirName = dataDirName + "/archive";
	}
	
	public void start() {
		// check if rootFolder and subfolders exist
		System.out.println("Creating data directories: " + dataDirName + ", " + archiveDirName);
		FileIO.makeDirs(new String[] {dataDirName, activeDirName, archiveDirName});
		this.dbMap = new DatabaseMaps(activeDirName);
	}
	
	
	//TODO check in package
	public Package checkInPackage(Person person, Package pkg) {
		// modify current DatabaseMaps, packageIDMap
		// write new person file
		return null;
	}
	
	//TODO check out package
	public void checkOutPackage(Package pkg) {
		// modify package checkOut date
		// remove package from packageID:Package map
	}
	
	/* return sorted entries for all active entries */
	//TODO get active entries
	public ArrayList<Pair<Person, Package>> getActiveEntries() {
		// 
		return null;
	}
	//TODO get entries by date
	public ArrayList<Pair<Person, Package>> getEntriesByDate(Date date, String predicate) {
		return null;
	}
	//TODO get entries by person
	public ArrayList<Pair<Person, Package>> getEntriesByPerson(Person person) {
		return null;
	}

	
	// make the directory structure required by the database
	
	/* 
	 * return entries sorted by:
	 * 	LastName
	 * 	FirstName
	 * 	Check-out date
	 *  Check-in date
	 */
	
	//TODO write sort function
	private ArrayList<Pair<Package,Person>> sortEntries(ArrayList<Pair<Package,Person>> PPList){
		return PPList;
	}
	
	//TODO write person file
	private void writePersonFile(Person person) {
		try {
			throw new IOException();
		} catch(IOException e) {
			
		}
	}
	
	//TODO add person
	private void addPerson(Person person) {
		//Check if person is in the archive
		
		//If person file is in archive, move file from archive and modify DatabaseMaps
		
		//Else, create a new file 
		
		//add to DatabaseMaps, netID:Person map, and packageID:Package map
	}
	
	//TODO delete person
	private void deletePerson(Person person) {
		// move person file to the archive 
		
		// remove person from DatabaseMaps, netID:Person map, and packageID:Package map
	}

	public ArrayList<Person> getAllPeople() {
		return dbMap.getAllPersons();
	}
	
}
