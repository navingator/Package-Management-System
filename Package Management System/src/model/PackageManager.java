package model;

import java.text.SimpleDateFormat;
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
	private String[] subDirectories;
	
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
		
		//TODO viewAdaptor
//		db = new Database(rootDirName,viewAdaptor);
//		mailer = new Emailer(viewAdaptor);
//		printer = new LabelPrinter(viewAdaptor);
	}
	
	public void start() {
		db.start();
		mailer.start();
		printer.start();
	}
	
	// TODO
	private void makeDirectories() {
		System.out.println();
	}

	//TODO Functions that run on a schedule - mainly send reminders
	
	/*
	 * Emailer functions
	 */
	
	public boolean sendPackageNotification(String personID) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean checkAdminPassword(String password) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean changeEmail(String newEmail, String newPassword) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/*
	 * Printer functions
	 */
	public boolean printLabel(long pkgID) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/*
	 * Database functions
	 */
	
	public Package checkInPackage(String personID, String comment) {
		// create a packageID
		Date now = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddhhmmssSS");
		long pkgID = Long.valueOf(ft.format(now)).longValue();
		
		Package pkg = new Package(pkgID, comment, now);
		db.checkInPackage(personID, pkg);
		return pkg;
	}
	
	public void checkOutPackage(long pkgID) {
		db.checkOutPackage(pkgID);		
	}
	
	//TODO change to package state instead of checkedOut
	public Boolean checkedOut(long pkgID) {
		//TODO
		return null;
	}
	
	public Person getOwner(long pkgID) {
		//TODO
		return null;
	}

	public ArrayList<Person> getPersonList(String searchString) {
		return db.getPersonList(searchString);
	}
	

	public ArrayList<Pair<Person, Package>> getPackages(String options) {
		return db.getEntries(options);
	}

	public void importPersonCSV(String fileName) {
		db.importPersonsFromCSV(fileName);		
	}

	public void addPerson(Person person) {
		db.addPerson(person);
	}

	public void editPerson(Person person) {
		db.editPerson(person);		
	}

	public void deletePerson(String personID) {
		db.deletePerson(personID);
	}
	
}
