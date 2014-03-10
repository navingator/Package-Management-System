package model.database;

import util.Package;
import util.Person;
import util.Pair;

import java.io.IOException;
import java.util.ArrayList;

/*
 * Class that handles operations to and from the model.database
 */

public class Database {
	
	PPMap peoplePersonMap;
	String dataFolderName;
	
	public Database(String dataFolderName) {
		this.dataFolderName = dataFolderName;
		this.peoplePersonMap = new PPMap(dataFolderName);
	}
	
	public void addPackage(Person person, Package pkg) {
		// modify current PPMap
		// write new person file
	}
	public void deletePackage(Package pkg) {
		// modify current PPMap
		// write new person file
	}
	
	
	public ArrayList<Pair<Package,Person>> getAllEntries() {
		return null;
	}
	public ArrayList<Pair<Package,Person>> getEntriesByDate(String predicate) {
		return null;
	}
	public ArrayList<Pair<Package,Person>> getEntriesByPerson(Person person) {
		return null;
	}

	private void writePersonFile(Person person) {
		try {
			throw new IOException();
		} catch(IOException e) {
			
		}
		
	}
	
}
