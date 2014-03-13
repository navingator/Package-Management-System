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

	public void getPackageFromID(long packageID) {
		// TODO Auto-generated method stub
		
	}
	
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

	public void printLabel(long pkgID) {
		// TODO Auto-generated method stub
		// TODO generate barcode and print label
		
	}

	public void sendPackageNotification(String personID) {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<Person> getPersonList(String searchString) {
		// TODO
		return null;
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
