package controller;

import java.awt.EventQueue;
import java.util.ArrayList;

import model.IModelToViewAdaptor;
import model.PackageManager;
import util.FileIO;
import util.Package;
import util.Pair;
import util.Person;
import util.PropertyHandler;
import view.IViewToModelAdaptor;
import view.MainFrame;

/**
 * Controller controlling the view and the underlying model,
 * instantiating them both as well as their communications and adaptors.
 */
public class Controller{
	
	private MainFrame viewFrame;
	private PackageManager modelPM;
	
	private LogHandler logHandler;
	private PropertyHandler propHandler;
	
	private String progDirName;
	
	/**
	 * Function which initializes and starts the controller
	 */
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
	
	/**
	 * Class which acts as the go between for the model and the view
	 */
	public Controller() { 
		
		this.propHandler = PropertyHandler.getInstance();
		this.logHandler = new LogHandler();
		init();
		
		/* Initializes the view */
		viewFrame = new MainFrame(new IViewToModelAdaptor() {

			public boolean checkedOut(long pkgID) {
				return modelPM.checkedOut(pkgID);
			}

			public Person getPackageOwner(long pkgID) {
				return modelPM.getOwner(pkgID);
			}
			
			public void checkOutPackage(long pkgID) {
				modelPM.checkOutPackage(pkgID);
			}
			
			public ArrayList<Person> getPersonList(String searchString) {
				return modelPM.getPersonList(searchString);
			}
			
			public void checkInPackage(String personID, String comment) {
				modelPM.checkInPackage(personID, comment);
			}
			
			public boolean sendPackageNotification(String personID) {
				return modelPM.sendPackageNotification(personID);
			}
			
			public boolean printLabel(long pkgID) {
				return modelPM.printLabel(pkgID);
			}
			
			public ArrayList<Pair<Person,Package>> getPackages(String filter, String sort) {
				return modelPM.getPackages(filter,sort);
			}
			
			public boolean authenticate(String password) {
				return modelPM.checkAdminPassword(password);
			}
			
			public boolean changeEmail(String newEmail, String newPassword) {
				return modelPM.changeEmail(newEmail, newPassword);
			}
			
			public void importPersonCSV(String fileName) {
				modelPM.importPersonCSV(fileName);
			}

			public void addPerson(String personID, String firstName,
					String lastName, String emailAddress) {
				Person person = new Person(lastName,firstName,emailAddress,personID);
				modelPM.addPerson(person);
				
			}

			public void editPerson(String personID, String firstName,
					String lastName, String emailAddress) {
				Person person = new Person(lastName,firstName,emailAddress,personID);
				modelPM.editPerson(person);
				
			}

			public void deletePerson(String personID) {
				modelPM.deletePerson(personID);
			}
			
		});
		
		// TODO ask chris how anonymous classes can work without an instantiated modelPM object
		/* Initialize model */
		modelPM = new PackageManager(new IModelToViewAdaptor() {
			//TODO write IModelToViewAdaptor
			public void displayMessage(String message) {
				
			}

			public void displayError(String string) {
				// TODO Auto-generated method stub
				
			}

			public String getChoiceFromList(String[] choices) {
				// TODO Auto-generated method stub
				return null;
			}

			public String[] changeEmail(String senderAddress,
					String senderPassword, String senderAlias) {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}
	
	/**
	 * Function initializes helper classes
	 */
	public void init() {
		// set and create root directory, if it doesn't exist
		String home = System.getProperty("user.home");
		this.progDirName = home + "/Documents/Package Management System";
		FileIO.makeDirs(progDirName);
		
		// Start helper classes
		propHandler.init(progDirName);
		logHandler.init(progDirName);
		
		logHandler.cleanLogs();
	}
	
	public void start() {
		// Start view and model
		viewFrame.start();
		modelPM.start();
	}

}
