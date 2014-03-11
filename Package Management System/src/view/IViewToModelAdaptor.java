package view;

import java.util.ArrayList;
import java.util.Date;

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
	public Pair<Person,Package> getPackageInfo(long packageID);
	public void checkOutPackage(Package pkg);
	
	/*
	 * Check in functions
	 */
	public ArrayList<Person> getPersonList();
	public Package checkInPackage(Person person, String comment);
	public void printLabel(Package pkg);
	public void sendPackageNotification(Person person);

	
	/*
	 * Admin functions
	 */
	
	public void authenticate(String password);
	
	public ArrayList<Pair<Person,Package>> getActivePackages();
	public ArrayList<Pair<Person,Package>> getPackagesByPerson(Person person);
	public ArrayList<Pair<Person,Package>> getPackagesByDate(Date date, String predicate);
	
	public void changeEmail(String newEmail, String newPassword);
	
	public void importPersonList(String fileName);
	public void savePersonList(ArrayList<Person> personList);
}
