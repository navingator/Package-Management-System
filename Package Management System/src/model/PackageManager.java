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
	
	private String progDirName;
	
	public PackageManager(IModelToViewAdaptor adpt) {
		
		viewAdaptor = adpt;
		
		// initialize the database
		db = new Database(progDirName,viewAdaptor);
		mailer = new Emailer(viewAdaptor);
		printer = new LabelPrinter(viewAdaptor);
		
		//TODO viewAdaptor
//		db = new Database(progDirName,viewAdaptor);
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
		// TODO Auto-generated method stub
		return false;
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
	
	public Boolean isCheckedOut(long pkgID) {
		Package pkg = db.getPackage(pkgID);
		return (pkg.getCheckOutDate() == null);
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
