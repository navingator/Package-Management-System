package model.database;

import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;

import util.Package;
import util.Person;

/*
 * Class containing a map that handles the storage of the person and package relationships 
 * for use by the database class only.
 */

public class DatabaseMaps {

	//TODO make private after testing
	public HashMap<Person, ArrayList<Package>> person2Package;
	public HashMap<Package, Person> package2Person;
	public HashMap<String,Person> personIDMap;
	public HashMap<Long,Package> packageIDMap;
	
	public DatabaseMaps(String folderName) {
		this.person2Package = new HashMap<Person, ArrayList<Package>>();
		this.package2Person = new HashMap<Package, Person>();
		
		//TODO Finish implementing these 2 maps - generateMapsFromFolder
		this.personIDMap = new HashMap<String,Person>();
		this.packageIDMap = new HashMap<Long,Package>();
		
		generateMapsFromFolder(folderName);
	}
	
	/*
	 * Generates both maps from indicated folder
	 * All people and package objects initially created here
	 */
	private void generateMapsFromFolder(String folderName) {
		//TODO: do it after we have JSON reader/writer. Shove it in the database?
	}
	
	/*
	 * Add a package to the maps
	 * Will reassign a package if the package already exists
	 */
	public void addPackage(Person person, Package pkg) {
		
		// If package is already in database, remove it
		if(packageIDMap.containsKey(pkg.getPackageID())) {
			deletePackage(pkg); // remove package
		}
		
		// retrieve list of packages after removal, if any
		ArrayList<Package> pkgList = person2Package.get(person);
		// If person is not in database, add person.
		if (pkgList == null) {
			addPerson(person);
			pkgList = new ArrayList<Package>();
		}
		
		// add new package to list of person's packages
		pkgList.add(pkg);
		
		// associate person with new list and write maps
		person2Package.put(person, pkgList);
		package2Person.put(pkg, person);
		packageIDMap.put(pkg.getPackageID(), pkg);
	}
	
	/*
	 * Delete a package from the maps
	 */
	public void deletePackage(Package pkg) {
		Person person = package2Person.get(pkg);
		if(person != null) {	
			// retrieve list of packages
			ArrayList<Package> pkgList = person2Package.get(person);
			pkgList.remove(pkg); // remove the package from the list (passed by reference)

			package2Person.remove(pkg);
			packageIDMap.remove(pkg.getPackageID());	
		}
	}
	
	/*
	 * Add a person to the maps
	 * Will edit a person if their netID already exists
	 * This will retain all of their packages when their information is edited
	 */
	public void addPerson(Person person) {
		// Create container for packages
		ArrayList<Package> pkgList = new ArrayList<Package>();
		String netID = person.getNetID();
		
		// Check if a person exists and remove the person and the person's packages if so
		if(personIDMap.containsKey(netID)) {
			// get the person's packages
			Person old = getPerson(netID);
			pkgList = getPackages(old);
			deletePerson(old);
		}
		
		
		// set person maps without packages
		personIDMap.put(netID, person);
		person2Package.put(person, new ArrayList<Package>());
		
		// reassign packages
		for (Package pkg: pkgList) {
			addPackage(person,pkg);
		}
		
	}
	
	/*
	 * Delete a person and all of person's packages from the maps
	 */
	public void deletePerson(Person person) {
		if(person2Package.containsKey(person)) {
			// remove all packages of person
			ArrayList<Package> pkgList = new ArrayList<Package>(person2Package.get(person));
			
			for (Package pkg: pkgList) {
				deletePackage(pkg);
			}
			
			// remove person from the maps
			personIDMap.remove(person.getNetID());
			person2Package.remove(person);
		}
	}
	
	public Person getPerson(Package pkg) {
		return package2Person.get(pkg);
	}
	
	public Person getPerson(String netID) {
		return personIDMap.get(netID);
	}
	
	public Package getPackage(long packageID) {
		return packageIDMap.get(packageID);
	}
	
	public ArrayList<Package> getPackages(Person person) {
		return person2Package.get(person);
	}
	
	public ArrayList<Person> getAllPersons() {
		return new ArrayList<Person>(person2Package.keySet());
	}
	
	public ArrayList<Package> getAllPackages() {
		return new ArrayList<Package>(package2Person.keySet());
	}
	
	public ArrayList<String> getAllNetID() {
		return new ArrayList<String>(personIDMap.keySet());
	}
	
	public ArrayList<Long> getAllPackageID() {
		return new ArrayList<Long>(packageIDMap.keySet());
	}
	
	//TODO test new implementation
	public static void main(String[] args) {
		DatabaseMaps dbMap = new DatabaseMaps("C:/Users/Navin/Documents/Package Management System/Data/active");
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
		
		dbMap.addPackage(navin,p1);
		dbMap.addPackage(navin,p2);
		dbMap.addPackage(navin,p2);
		dbMap.addPackage(navin,p3);
		
		dbMap.addPackage(christopher,p2);
		dbMap.deletePerson(navin);
		
		System.out.println(dbMap.person2Package.toString());
		System.out.println(dbMap.package2Person.toString());
		System.out.println(dbMap.personIDMap.toString());
		System.out.println(dbMap.packageIDMap.toString());
		System.out.println(dbMap.getPerson(p3).getFullName());
		System.out.println(dbMap.getPerson("cwh1").getFullName());
		System.out.println(dbMap.getPackages(chris).toString());
		System.out.println(dbMap.getPackage(309435).toString());
		//System.out.println(dbMap.getPackage(123).toString());
		//System.out.println(getPerson(p1).getNetID());		// should throw exception
	}
	
}
