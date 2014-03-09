package util;

import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;

public class PPMap {

	private static HashMap<Person, ArrayList<Package>> person2Package = new HashMap<Person, ArrayList<Package>>();
	private static HashMap<Package, Person> package2Person = new HashMap<Package, Person>();
	
	public static void generateMapsFromDatabase(String fileName) {
		//TODO: do it after we have JSON reader/writer
	}
	
	public static Person getPerson(Package pkg) {
		return package2Person.get(pkg);
	}
	
	public static ArrayList<Package> getPackages(Person person) {
		return person2Package.get(person);
	}
	
	public static void addEntry(Person person, Package pkg) {
		package2Person.put(pkg, person);
		
		// retrieve list of packages
		ArrayList<Package> pkgList = person2Package.get(person);
		if (pkgList == null) {
			pkgList = new ArrayList<Package>();
		}
		
		// add new one to list
		pkgList.add(pkg);
		
		// associate person with new list
		person2Package.put(person, pkgList);
	}
	
	public static void deleteEntry(Person person, Package pkg) {
		package2Person.remove(pkg);
		
		// retrieve list of packages
		ArrayList<Package> pkgList = person2Package.get(person);
		if (pkgList.size() == 1) {
			//TODO: reconsider this, especially if we do not store people elsewhere
			person2Package.remove(person); // remove person if only package
		} else {
			pkgList.remove(pkg); // remove the package from the list
			person2Package.put(person, pkgList); // associate with new list
		}
	}
	
	public static void main(String[] args) {
		Date now = new Date();
		Package p1 = new Package(123,"",now);
		Package p2 = new Package(234,"It's huge. Get it out now.",new Date(now.getTime()-100000000));
		Package p3 = new Package(309435,"",new Date(now.getTime()-200000000));
		
		Person navin = new Person("Pathak", "Navin", "np8@rice.edu");
		Person chris = new Person("Henderson", "Chris", "cwh1@rice.edu");
		
		addEntry(navin,p1);
		addEntry(navin,p2);
		deleteEntry(navin,p1);
		addEntry(navin,p3);
		System.out.println(person2Package.toString());
		System.out.println(package2Person.toString());
		System.out.println(getPerson(p2).getEmailAddress());
		System.out.println(getPackages(navin).toString());
		//System.out.println(getPerson(p1).getNetID());		// should throw exception
		//System.out.println(getPackages(chris).toString());// should throw exception
		
	}
	
}
