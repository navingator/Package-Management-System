package controller;

import java.awt.EventQueue;
import java.util.Date;

import model.IModelToViewAdapter;
import model.database.Database;
import util.Package;
import util.Person;
import view.IViewToModelAdapter;
import view.MainFrame;

/**
 * Controller controlling the view and the underlying model,
 * instantiating them both as well as their communications and adaptors.
 */

public class Controller implements IViewToModelAdapter, IModelToViewAdapter{
	
	private static Database ActiveDB;
	
	public static void main(String[] args) {
		
		// initialize model.database
		ActiveDB = new Database("$HOME$/Documents/Package Management Utility/Data/Active Packages");

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void createPackage(Person person, String comment) {
		Date now = new Date();
		//TODO Actually make the packageID work
		Package pkg = new Package(0, comment, now);
		ActiveDB.addPackage(person, pkg);
	}

}
