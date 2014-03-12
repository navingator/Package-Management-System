package model;

import java.util.ArrayList;
import java.util.Date;

import model.database.Database;
import model.email.Emailer;
import model.print.LabelPrinter;
import util.FileIO;
import util.Package;
import util.Pair;
import util.Person;

public class PackageManager {

	private IModelToViewAdaptor viewAdaptor;
	
	private Database db;
	private Emailer mailer;
	private LabelPrinter printer;
	
	private String rootDirName;
	
	public PackageManager(IModelToViewAdaptor adpt) {
		
		viewAdaptor = adpt;
		
		// set and create root directory, if it doesn't exist
		String home = System.getProperty("user.home");
		rootDirName = home + "/Documents/Package Management System";
		System.out.println("Creating root directory: " + rootDirName);
		FileIO.makeDirs(rootDirName);
		
		// initialize the database
		db = new Database(rootDirName);
		mailer = new Emailer();
		printer = new LabelPrinter();
	}
	
	public void start() {
		db.start();
		mailer.start();
		printer.start();
	}

	public void getPackageFromID(long packageID) {
		// TODO Auto-generated method stub
		
	}
	
	public Package checkInPackage(Person person, String comment) {
		Date now = new Date();
		//TODO Actually make the packageID work
		Package pkg = new Package(0, comment, now);
		return db.checkInPackage(person, pkg);
	}

	public void checkOutPackage(Package pkg) {
		db.checkOutPackage(pkg);
	}

	public void printLabel(Package pkg) {
		// TODO Auto-generated method stub
		// TODO generate barcode and print label
		
	}

	public void sendPackageNotification(Person person) {
		// TODO Auto-generated method stub
		//emailer.sendPackageNotification(person)
		
	}

	public ArrayList<Person> getAllPeople() {
		return db.getAllCurrentPersons();
	}
	
	public ArrayList<Pair<Person, Package>> getActiveEntries() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<Pair<Person, Package>> getEntriesByPerson(Person person) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<Pair<Person, Package>> getEntriesByDate(Date date,
			String predicate) {
		// TODO Auto-generated method stub
		return null;
	}

	public void checkAdminPassword(String password) {
		// TODO Auto-generated method stub
		
	}

	public void changeEmail(String newEmail, String newPassword) {
		// TODO Auto-generated method stub
		
	}

	public void importPersonList(String fileName) {
		// TODO Auto-generated method stub
		
	}

	public void exportPersonList(ArrayList<Person> personList) {
		// TODO Auto-generated method stub
		
	}
	
}
