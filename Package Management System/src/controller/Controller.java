package controller;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Date;

import model.IModelToViewAdaptor;
import model.database.Database;
import util.Package;
import util.Pair;
import util.Person;
import view.IViewToModelAdaptor;
import view.MainFrame;

/**
 * Controller controlling the view and the underlying model,
 * instantiating them both as well as their communications and adaptors.
 */

public class Controller{
	
	private MainFrame viewFrame;
	//TODO implement model
	private Database ActiveDB;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Controller control = new Controller();
					control.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public Controller() {
		// Initialize view
		viewFrame = new MainFrame(new IViewToModelAdaptor() {
			public Pair<Person,Package> getPackageInfo(long packageID) {
				//TODO
				return null;
			}
			
			public void checkOutPackage(Package pkg) {
				//TODO
			}
			
			public ArrayList<Person> getPersonList() {
				//TODO
				return null;
			}
			
			public Package checkInPackage(Person person, String comment) {
				//TODO
				return null;
			}
			
			public void printLabel(Package pkg) {
				//TODO
			}
			
			public void sendPackageNotification(Person person) {
				//TODO
			}
			
			public void authenticate(String password) {
				//TODO
			}
			
			public ArrayList<Pair<Person,Package>> getActivePackages() {
				return ActiveDB.getActiveEntries();
			}
			
			public ArrayList<Pair<Person,Package>> getPackagesByPerson(Person person) {
				return ActiveDB.getEntriesByPerson(person);
			}
			
			public ArrayList<Pair<Person,Package>> getPackagesByDate(Date date, String predicate) {
				return ActiveDB.getEntriesByDate(date, predicate);
			}
			
			public void changeEmail(String newEmail, String newPassword) {
				//TODO
			}
			
			public void importPersonList(String fileName) {
				//TODO
			}
			
			public void savePersonList(ArrayList<Person> personList) {
				//TODO
			}
		});
		
		// Initialize model
	}
	
	public void start() {
		viewFrame.start();
		//TODO Start model
	}
	
	//TODO Send this to another file
	public Package checkInPackage(Person person, String comment) {
		Date now = new Date();
		//TODO Actually make the packageID work
		Package pkg = new Package(0, comment, now);
		return ActiveDB.checkInPackage(person, pkg);
	}

}
