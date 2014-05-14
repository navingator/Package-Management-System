package view;

import java.util.ArrayList;
import java.util.Map;

import util.Package;
import util.Pair;
import util.Person;

/*
 * Interface outlining the operations that can be performed by the view (MainFrame)
 * on the model (model.database)
 */

public interface IViewToModelAdaptor {
	
	/*
	 * Pick up functions
	 */
	
	/**
	 * Returns the package from the packageID
	 * 
	 * @param pkgID				ID of the package
	 * @return					Package object
	 */
	public Package getPackage(long pkgID);
	
	/**
	 * returns the owner for use in confirmation message
	 * 
	 * @param pkgID				ID of the package to determine the owner of
	 * @return 					Person object containing information about owner
	 */
	public Person getPackageOwner(long pkgID);
	
	/**
	 * Checks out the package after getting confirmation
	 * 
	 * @param pkgID				ID of the package to be checked out
	 * @return					Success of checking out package
	 */
	public boolean checkOutPackage(long pkgID);
	
	/*
	 * Check in functions
	 */
	
	/**
	 * Returns a list of person objects filtered by searchString
	 * 
	 * @param searchString		String representing search 
	 * @return					ArrayList of person objects matching searchString
	 */
	public ArrayList<Person> getPersonList(String searchString);
	
	/**
	 * Checks in a package into the database
	 * 
	 * @param personID			ID of the package owner
	 * @param comment			Comment input by the user
	 * @return					ID of the package
	 */
	public long checkInPackage(String personID, String comment);
	
	/**
	 * Send a package notification email alerting the person
	 * that a package has been checked in
	 * 
	 * @param personID			ID of the package owner
	 * @param pkgID				ID of the package
	 * @return					Success of sending email
	 */
	public boolean sendPackageNotification(String personID, long pkgID);
	
	
	/**
	 * Send package reminders for everyone with packages not
	 * checked out
	 * 
	 * @return					Success of sending all reminders
	 */
	public boolean sendPackageReminders();

	/**
	 * Print a label for the package. 
	 * Can also be called by the admin functions.
	 * 
	 * @param pkgID				ID of the package 
	 * @return					Success of printing
	 */
	public boolean printLabel(long pkgID);
	
	/**
	 * Returns a list of all of the printers with associated drivers on
	 * the computer.
	 * 
	 * @return					Array of Strings of printer names
	 */
	public String[] getPrinterNames();
	
	/**
	 * Sets the printer to the printer with the given name.
	 * @param PrinterName		Name of the printer to set
	 */
	public void setPrinter(String PrinterName);
	
	/*
	 * Admin functions
	 */
	
	/**
	 * Authenticates the user to gain access to the admin functions
	 * 
	 * @param password			User input password
	 * @return					Success of authentication
	 */
	public boolean authenticate(String password);
	
	/**
	 * Changes the email information of the user and attempts to connect to the mail
	 * server with the new credentials. Returns whether or not this was successful.
	 * 
	 * @param newEmail			New email input by user
	 * @param newPassword		New password input by user
	 * @param newAlias			New email alias input by user
	 */
	public void changeEmail(String newEmail, String newPassword, String newAlias);
	
	/**
	 * Returns the email address stored
	 * @return 					String containing email address
	 */
	public String getEmailAddress();
	
	/**
	 * Returns the email alias stored
	 * @return 					String containing email alias
	 */
	public String getEmailAlias();
	
	/**
	 * Returns raw email template
	 * @return                  String containing email template
	 */
	public String getRawEmailTemplate();
	
	/**
	 * Returns email template as a Map of Strings to Strings.  
	 * 
	 * @param convert           If TRUE is given for convert, will replace linebreaks with <br> HTML tags if the AUTO-LINEBREAK option is TRUE in the template file.
	 * 							If FALSE is given, will not replace linebreaks.
	 * @param comments          If TRUE, returns with comments intact.  Otherwise removes comments.
	 * 
	 * @return                  String containing email template
	 */
	public Map<String,String> getEmailTemplates(boolean convert, boolean comments);
	
	/**
	 * Returns all of the packages with the appropriate filters applied
	 * 
	 * @param filter			String containing filtering information
	 * @param sort				String containing sorting information
	 * @return					ArrayList of (person, package) pairs after filter is applied
	 */
	public ArrayList<Pair<Person,Package>> getPackages(String filter, String sort);
	
	/**
	 * Reads a list of people from a csv file and adds the people to the database
	 * CSV format: LastName,FirstName,EmailAddress,PersonID 
	 * 
	 * @param fileName			Location of the csv
	 * @return					Success of importing the person list
	 */
	public void importPersonCSV(String fileName);
	
	/**
	 * Adds a person to the database
	 *
	 * @param personID			personID of the person - user input
	 * @param firstName			First Name of the person - user input
	 * @param lastName			Last Name of the person - user input
	 * @param emailAddress		Email Address of the person - user input
	 * @return					Success of adding person
	 */
	public boolean addPerson(String personID, String firstName, String lastName, String emailAddress);
	
	/**
	 * Edits a person already in the database
	 * 
	 * @param personID			personID of the person - must already exist in the database
	 * @param firstName			First Name of the person - user input
	 * @param lastName			Last Name of the person - user input
	 * @param emailAddress		Email Address of the person - user input
	 * @return					Success of editing person
	 */
	public boolean editPerson(String personID, String firstName, String lastName, String emailAddress);
	
	/**
	 * Deletes a person already in the database
	 * 
	 * @param personID			personID of the person - must already exist in the database
	 * @return 
	 */
	public boolean deletePerson(String personID);

	/**
	 * Changes the Email template file
	 * 
	 * @param newTemplate
	 */
	public void changeEmailTemplate(Map<String,String> newTemplate);
}
