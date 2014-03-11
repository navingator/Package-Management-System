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
		//TODO: do it after we have JSON reader/writer
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
	
	public void addPackage(Person person, Package pkg) {
		package2Person.put(pkg, person);
		packageIDMap.put(pkg.getPackageID(), pkg);
		
		// retrieve list of packages
		ArrayList<Package> pkgList = person2Package.get(person);
		if (pkgList == null) {
			addPerson(person);
			pkgList = new ArrayList<Package>();
		}
		
		// add new one to list
		pkgList.add(pkg);
		
		// associate person with new list
		person2Package.put(person, pkgList);
	}
	
	public void deletePackage(Person person, Package pkg) {
		package2Person.remove(pkg);
		packageIDMap.remove(pkg.getPackageID());
		
		// retrieve list of packages
		ArrayList<Package> pkgList = person2Package.get(person);
		pkgList.remove(pkg); // remove the package from the list
		person2Package.put(person, pkgList); // associate with new list
	}
	
	public void addPerson(Person person) {
		if(!person2Package.containsKey(person)) {
			personIDMap.put(person.getNetID(), person);
			person2Package.put(person, new ArrayList<Package>());
		}
	}
	
	public void deletePerson(Person person) {
		
		// remove person from personID map
		personIDMap.remove(person.getNetID());
		
		// remove all packages of person
		ArrayList<Package> pkgList = person2Package.get(person);
		for (Package pkg: pkgList) {
			deletePackage(person,pkg);
		}
		
		// remove person from the person2Package map
		person2Package.remove(person);
	}
	
	public ArrayList<Person> getAllPeople() {
		ArrayList<Person> personList = new ArrayList<Person>();
		personList.addAll(person2Package.keySet());
		return personList;
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
		
		dbMap.addPerson(navin);
		dbMap.addPerson(chris);
		dbMap.addPerson(christopher);
		
		dbMap.addPackage(navin,p1);
		dbMap.addPackage(navin,p2);
		dbMap.deletePackage(navin,p1);
		dbMap.addPackage(navin,p3);
		
		dbMap.addPackage(chris,p2);
		
		System.out.println(dbMap.person2Package.toString());
		System.out.println(dbMap.package2Person.toString()); //TODO detect collision
		System.out.println(dbMap.personIDMap.toString());
		System.out.println(dbMap.packageIDMap.toString());
		System.out.println(dbMap.getPerson(p2).getEmailAddress());
		System.out.println(dbMap.getPerson("np8").getEmailAddress());
		System.out.println(dbMap.getPackages(navin).toString());
		System.out.println(dbMap.getPackage(309435).toString());
		//System.out.println(dbMap.getPackage(123).toString());
		//System.out.println(getPerson(p1).getNetID());		// should throw exception
	}
	
}
