package controller;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Date;

import model.IModelToViewAdaptor;
import model.PackageManager;
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

//TODO Find all of the system.out.println and replace with logger

public class Controller{
	
	private MainFrame viewFrame;
	private PackageManager modelPM;
	
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
				modelPM.getPackageFromID(packageID);
				return null;
			}
			
			public void checkOutPackage(long pkgID) {
				modelPM.checkOutPackage(pkgID);
			}
			
			public ArrayList<Person> getPersonList(String searchString) {
				return modelPM.getPersonList(searchString);
			}
			
			public void checkInPackage(String personID, String comment) {
				modelPM.checkInPackage(personID, comment);
				return null;
			}
			
			public void printLabel(long pkgID) {
				modelPM.printLabel(pkgID);
			}
			
			public void sendPackageNotification(String personID) {
				modelPM.sendPackageNotification(personID);
			}
			
			public ArrayList<Pair<Person,Package>> getPackages(String filter) {
				return modelPM.getPackages();
			}
			
			public ArrayList<Pair<Person,Package>> getPackagesByPerson(Person person) {
				return modelPM.getEntriesByPerson(person);
			}
			
			public ArrayList<Pair<Person,Package>> getPackagesByDate(Date date, String predicate) {
				return modelPM.getEntriesByDate(date, predicate);
			}
			
			public void authenticate(String password) {
				modelPM.checkAdminPassword(password);
			}
			
			public void changeEmail(String newEmail, String newPassword) {
				modelPM.changeEmail(newEmail, newPassword);
			}
			
			public void importPersonList(String fileName) {
				modelPM.importPersonList(fileName);
			}
			
			public void savePersonList(ArrayList<Person> personList) {
				modelPM.exportPersonList(personList);
			}
		});
		
		// TODO ask chris how anonymous classes can work without an instantiated modelPM object
		// Initialize model
		modelPM = new PackageManager(new IModelToViewAdaptor() {
			//TODO write IModelToViewAdaptor
			public void displayMessage(String message) {
				
			}
		});
	}
	
	public void start() {
		//TODO Start logger
		viewFrame.start();
		modelPM.start();
	}
	
	//TODO Send this to another file


}
