package view;

import util.Package;
import util.Person;

/*
 * Interface outlining the operations that can be performed by the view (MainFrame)
 * on the model (model.database)
 */

public interface IViewToModelAdapter {
	public void createPackage(Person person, String comment);
	
	
}
