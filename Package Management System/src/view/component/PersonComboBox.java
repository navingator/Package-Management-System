package view.component;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JComboBox;

import util.Person;

public class PersonComboBox extends JComboBox<String>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6672267768163626009L;
	private ArrayList<Person> personList;
	private ArrayList<Person> currentPersons;

	public PersonComboBox() {
		
		personList = new ArrayList<Person>();
		currentPersons = new ArrayList<Person>();
		
		getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent arg0) {
				setMatchingPersons();
			}
		});
		
		setEditable(true);
	}
	
	/**
	 * Get the person selected in the PersonComboBox
	 * @return						Person object that is selected
	 */
	public Person getSelectedPerson() {
		return currentPersons.get(getSelectedIndex());
	}
	
	/**
	 * Set the list of people available for the list
	 * @param personList			ArrayList of persons to put in list
	 */
	public void setPersonList(ArrayList<Person> personList) {
		this.personList = personList;
		for (Person person: personList) {
			addItem(getPersonEntry(person));
		}
	}

	/**
	 * Find persons that match the search string and put them in the 
	 * JComboBox list.
	 */
	private void setMatchingPersons() {
		removeAllItems();
		currentPersons.clear();
		String search = (String) getEditor().getItem();
		System.out.println(getEditor().getItem());
		
		// collect persons that contain the search string
		for (Person person: personList) {
			String personString = getPersonEntry(person);
			if(personString.toLowerCase().contains(search.toLowerCase())) {
				currentPersons.add(person);
			}
		}
		
		// add all persons to list
		for (Person person: currentPersons) {
			addItem(getPersonEntry(person));
		}
		
		if(search.length() > 0) {
			showPopup();
		} else {
			hidePopup();
		}
	}
	
	/**
	 * Return a string that represents the entry for a person
	 * @param person				Person object
	 * @return						String representation of person object LastName, FirstName (PersonID)
	 */
	private String getPersonEntry(Person person) {
		return person.getLastFirstName() + " (" + person.getPersonID() + ")";
	}

}
