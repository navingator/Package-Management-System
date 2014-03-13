package view;

import java.util.ArrayList;
import util.Package;
import util.Pair;
import util.Person;

/*
 * Interface outlining the operations that can be performed by the view (MainFrame)
 * on the model (model.database)
 */

public interface IViewToModelAdaptor {
	
	/**
	 * Pick up functions
	 */
	
	/**
	 * Returns true if the package is already checked out
	 * 
	 * @param pkgID			ID of the package to determine state of
	 * @return				Boolean containing package state
	 */
	public Boolean checkedOut(long pkgID);
	
	/**
	 * returns the owner for use in confirmation message
	 * 
	 * @param pkgID			ID of the package to determine the owner of
	 * @return 				Person object containing information about owner
	 */
	public Person getPackageOwner(long pkgID);
	
	/**
	 * Checks out the package after getting confirmation
	 * 
	 * @param pkgID			ID of the package to be checked out
	 * @return				Success state
	 */
	public Boolean checkOutPackage(long pkgID);
	
	/**
	 * Check in functions
	 */
	
	/**
	 * Returns a list of person objects filtered by searchString
	 * 
	 * @param searchString	String representing search 
	 * @return				ArrayList of person objects matching searchString
	 */
	public ArrayList<Person> getPersonList(String searchString);
	
	/**
	 * Checks in a package into the database
	 * @param personID		ID of the package owner
	 * @param comment		Comment input by the user
	 * @return				Success state	
	 */
	public Boolean checkInPackage(String personID, String comment);
	
	/**
	 * Send a package notification email alerting the person
	 * that a package has been checked in
	 * 
	 * @param	
	 */
	public Boolean sendPackageNotification(String personID);

	
	/*
	 * Admin functions
	 */
	public void authenticate(String password);
	public void changeEmail(String newEmail, String newPassword);
	
	public ArrayList<Pair<Person,Package>> getPackages(String filter);
	public void printLabel(long pkgID);
	
	public void importPersonList(String fileName);
	public void addPerson(String personID);
	public void editPerson(String personID);
	public void deletePerson(String personID);
}
