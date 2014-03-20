package model.database;

import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.logging.Logger;

import util.Package;
import util.Pair;
import util.Person;

/*
 * Class containing a map that handles the storage of the person and package relationships 
 * for use by the database class only.
 */

public class DBMaps {
	
	private HashMap<String, ArrayList<Long>> personID2PackageIDs;
	private HashMap<Long, String> packageID2PersonID;
	private HashMap<String,Person> personIDMap;
	private HashMap<Long,Package> packageIDMap;
	
	Logger logger;
	
	public DBMaps() {
		this.personID2PackageIDs = new HashMap<String, ArrayList<Long>>();
		this.packageID2PersonID = new HashMap<Long, String>();
		this.personIDMap = new HashMap<String,Person>();
		this.packageIDMap = new HashMap<Long,Package>();
		
		this.logger = Logger.getLogger(DBMaps.class.getName());
	}
	
	/**
	 * Add a package to the maps. Associates the package with the
	 * personID and maps the packageID to the package.
	 * @param personID			ID of the person to add the package	
	 * @param pkg				Package object to add
	 */
	public void addPackage(String personID, Package pkg) {
		
		long pkgID = pkg.getPackageID();
		// If package is already in database, log the event
		if(packageIDMap.containsKey(pkgID)) {
			logger.warning("Package (ID: " + pkgID + ") to be added is already in the database.");
			return;
			//throw new DatabaseException("Package (ID: " + pkgID + ") to be added is already in the database.");
		} 

		// add to personID2PackageID
		// retrieve list of packageIDs
		ArrayList<Long> pkgIDList = personID2PackageIDs.get(personID);
		// add new package ID to list of person's packageIDs
		pkgIDList.add(pkgID);
		
		// add package to other maps
		packageID2PersonID.put(pkgID, personID);
		packageIDMap.put(pkgID, pkg);
	}
	
	/**
	 * Edit a package, given that they MUST HAVE THE SAME PackageID in the maps. 
	 * Edit the packageID to package map.
	 * @param newPackage		new package to replace the old package
	 */
	public void editPackage(Package newPackage) {
		long pkgID = newPackage.getPackageID();
		
		//If package is not in database, log the event
		if (!packageIDMap.containsKey(pkgID)) {
			logger.warning("Package (ID: " + pkgID + ") to be edited not found in database.");
			return;
		}

		//edit the packageIDMap only
		packageIDMap.put(pkgID, newPackage);
	}
	
	/**
	 * Delete a package from the maps. This removes it from the 
	 * personID2PackageIDs map, the packageID2PersonID map, and the packageIDMap
	 * @param pkgID				ID of package to be deleted
	 */
	public void deletePackage(long pkgID) {
		String personID = packageID2PersonID.get(pkgID);
		
		// If the package is not in the database, log the event.
		if(personID == null) {
			logger.warning("Package (ID: " + pkgID + ") to be deleted not found in database.");
			return;
		} 

		// remove from personID2PackageIDs map
		ArrayList<Long> pkgIDList = personID2PackageIDs.get(personID);
		pkgIDList.remove(pkgID); // remove the package from the list (passed by reference)

		// remove from other maps
		packageID2PersonID.remove(pkgID);
		packageIDMap.remove(pkgID);	
	}
	
	/**
	 * Add a person to the maps, specifically adding to personID to person map
	 * and personID2PackageIDs empty entry.
	 * @param person			Person to be added
	 */
	public void addPerson(Person person) {
		String personID = person.getPersonID();
		
		// If the person does not exist, return and log the event.
		if(personIDMap.containsKey(personID)) {
			logger.warning("Person (ID: " + personID + ") to be added is already in database.");
			return;
		}

		personIDMap.put(personID, person);
		personID2PackageIDs.put(personID, new ArrayList<Long>());
		
	}
	
	/**
	 * Edit a person in the maps
	 * This will retain all of their packages when their information is edited
	 * 
	 * Note: The new Person object must have the same personID as before
	 * 
	 * @param newPerson			Person to be edited (must have same personID)
	 */
	
	public void editPerson(Person newPerson) {
		String personID = newPerson.getPersonID();
		
		//If person is not in database, log the event
		if (!personIDMap.containsKey(personID)) {
			logger.warning("Person (ID: " + personID + ") to be edited not found in database.");
			return;
		} 

		//edit the personIDMap only
		personIDMap.put(personID, newPerson);
	}
	
	/**
	 * Delete a person and all of person's packages from the maps
	 * 
	 * @param personID			ID of the person to be deleted
	 */
	public void deletePerson(String personID) {
		
		Person person = personIDMap.get(personID);
		
		// If person not found, log the event
		if(person == null) {
			logger.warning("Person (ID: " + personID + ") to be deleted delete not found in database.");
			return;
			//throw new DatabaseException("Person (Name: " + person.getFullName() + ") to be deleted delete not found in database.")
		} 

		// remove all packages of person
		ArrayList<Long> pkgIDList = new ArrayList<Long>(personID2PackageIDs.get(personID));
		
		for (long pkgID: pkgIDList) {
			deletePackage(pkgID);
		}
		
		// remove person from the maps
		personIDMap.remove(person.getPersonID());
		personID2PackageIDs.remove(personID);

	}
	
	/*
	 * Getters for the DBMaps class
	 */
	
	public Person getPerson(String personID) {
		return personIDMap.get(personID);
	}
	
	public String getOwnerID(long pkgID) {
		return packageID2PersonID.get(pkgID);
	}
	
	public Package getPackage(long packageID) {
		return packageIDMap.get(packageID);
	}
	
	public ArrayList<Long> getOwnedPackageIDs(String personID) {
		return new ArrayList<Long>(personID2PackageIDs.get(personID));
	}
	
	public ArrayList<Person> getAllPersons() {
		return new ArrayList<Person>(personIDMap.values());
	}
	
	public ArrayList<Package> getAllPackages() {
		return new ArrayList<Package>(packageIDMap.values());
	}
	
	public ArrayList<String> getAllPersonIDs() {
		return new ArrayList<String>(personIDMap.keySet());
	}
	
	public ArrayList<Long> getAllPackageIDs() {
		return new ArrayList<Long>(packageIDMap.keySet());
	}
	
	/**
	 * Returns an ArrayList of all entries
	 * @return					ArrayList of all entries
	 */
	public ArrayList<Pair<Person,Package>> getAllEntries() {
		ArrayList<Pair<Person,Package>> result = new ArrayList<Pair<Person,Package>>();
		ArrayList<Long> pkgIDList = getAllPackageIDs(); 
		for (long pkgID: pkgIDList) {
			Package pkg = getPackage(pkgID);
			Person person = getPerson(getOwnerID(pkgID));
			result.add(new Pair<Person,Package>(person,pkg));
		}
		return result;
	}
	
	public static void main(String[] args) {
		DBMaps dbMap = new DBMaps();
		Date now = new Date();
		Package p1 = new Package(123,"",now);
		Package p2 = new Package(234,"It's huge. Get it out now.",new Date(now.getTime()-100000000));
		Package p3 = new Package(309435,"",new Date(now.getTime()-200000000));
		
		Person navin = new Person("Pathak", "Navin", "np8@rice.edu","np8");
		Person chris = new Person("Henderson", "Chris", "cwh1@rice.edu","cwh1");
		Person christopher = new Person("Henderson", "Christopher", "cwh1@rice.edu","cwh1");
		Person ambi = new Person("Bobmanuel","Ambi","ajb6@rice.edu","ajb6");
		
		dbMap.addPerson(navin);
		dbMap.addPerson(chris);
		dbMap.addPerson(christopher);
		dbMap.addPerson(navin);
		dbMap.addPerson(ambi);
		
		dbMap.editPerson(christopher);
		
		dbMap.addPackage(navin.getPersonID(),p1);
		dbMap.addPackage(navin.getPersonID(),p2);
		dbMap.addPackage(navin.getPersonID(),p2);
		dbMap.addPackage(navin.getPersonID(),p3);
		
		p3.setCheckOutDate(now);
		dbMap.editPackage(p3);
		
		dbMap.deletePackage(p2.getPackageID());
		dbMap.deletePerson(ambi.getPersonID());
		
		System.out.println(dbMap.personID2PackageIDs.toString());
		System.out.println(dbMap.packageID2PersonID.toString());
		System.out.println(dbMap.personIDMap.toString());
		System.out.println(dbMap.packageIDMap.toString());
		System.out.println(dbMap.getPerson("cwh1").getFullName());
		System.out.println(dbMap.getPerson(dbMap.getOwnerID(p3.getPackageID())).getFullName());
		System.out.println(dbMap.getPackage(309435).getCheckOutDate());
		System.out.println(dbMap.getOwnedPackageIDs(chris.getPersonID()).toString());
		System.out.println(dbMap.getAllPersons().toString());
		System.out.println(dbMap.getAllPackages().toString());
		System.out.println(dbMap.getAllPersonIDs().toString());
		System.out.println(dbMap.getAllPackageIDs().toString());
		System.out.println(dbMap.getAllEntries().get(0).second.toString());
		//System.out.println(dbMap.getPackage(123).toString());
		//System.out.println(getPerson(p1).getpersonID());		// should throw exception
	}
	
}


