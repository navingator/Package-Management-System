package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.database.Database;
import model.email.Emailer;
import model.print.LabelPrinter;
import util.Package;
import util.Pair;
import util.Person;

/**
 * Package Manager class functions as a large model to string together the
 * database, emailer, and printer
 * @author Navin
 *
 */
public class PackageManager {

	private IModelToViewAdaptor viewAdaptor;
	
	private Database db;
	private Emailer mailer;
	private LabelPrinter printer;
	
	public PackageManager(IModelToViewAdaptor adpt) {
		
		viewAdaptor = adpt;
		
		// initialize the database
		db = new Database(viewAdaptor);
		mailer = new Emailer(viewAdaptor);
		printer = new LabelPrinter(viewAdaptor);
	}
	
	public void start() {
		// Start the database, mailer, and printer
		db.start();
		mailer.start(db.getEntries("checked_in=TRUE", "person_ID=ASCENDING"));
		printer.start();
	}
	
	//TODO Functions that run on a schedule - mainly send reminders
	
	/*
	 * Emailer functions
	 */
	
	public boolean sendPackageNotification(String personID, long pkgID) {
		Person person = db.getPerson(personID);
		Package pkg = db.getPackage(pkgID);
		return mailer.sendPackageNotification(person, pkg);
	}
	
	public boolean sendPackageReminders() {
		ArrayList<Pair<Person,Package>> entriesSortedByPerson = 
				db.getEntries("checked_in=true", "person_ID=ASCENDING");
		return mailer.sendAllReminders(entriesSortedByPerson);
	}
	
	public boolean checkAdminPassword(String password) {
		// TODO Auto-generated method stub
		return false;
	}

	public void changeEmail(String newEmail, String newPassword, String newAlias) {
		mailer.setEmailProperties(newEmail, newPassword, newAlias);
	}
	
	public String getEmailAddress() {
		return mailer.getSenderAddress();
	}

	public String getEmailAlias() {
		return mailer.getSenderAlias();
	}
	
	/*
	 * Printer functions
	 */
	public boolean printLabel(long pkgID) {
		String personName = db.getOwner(pkgID).getLastFirstName();
		return printer.printLabel(Long.valueOf(pkgID).toString(), personName);
	}
	
	public String[] getPrinterNames() {
		return printer.getPrinterNames();
	}
	
	public void setPrinter(String printerName) {
		printer.setPrinter(printerName);
	}
	
	/*
	 * Database functions
	 */
	
	public long checkInPackage(String personID, String comment) {
		// create a packageID
		Date now = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddHHmmss");
		long pkgID = Long.valueOf(ft.format(now)).longValue();
		
		Package pkg = new Package(pkgID, comment, now);
		db.checkInPackage(personID, pkg);
		return pkg.getPackageID();
	}
	
	public void checkOutPackage(long pkgID) {
		db.checkOutPackage(pkgID);		
	}
	
	public Package getPackage(long pkgID) {
		return db.getPackage(pkgID);
	}
	
	public Person getOwner(long pkgID) {
		return db.getOwner(pkgID);
	}

	public ArrayList<Person> getPersonList(String searchString) {
		return db.getPersonList(searchString);
	}
	

	public ArrayList<Pair<Person, Package>> getPackages(String filter, String sort) {
		return db.getEntries(filter,sort);
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
