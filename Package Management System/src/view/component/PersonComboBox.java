package view.component;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.text.JTextComponent;

import util.Person;

public class PersonComboBox extends JComboBox<String>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6672267768163626009L;
	private ArrayList<Person> personList;
	private ArrayList<Person> currentPersons;
	private final JTextComponent inputTextBox = (JTextComponent) getEditor().getEditorComponent();
	
	public PersonComboBox() {
		
		personList = new ArrayList<Person>();
		currentPersons = new ArrayList<Person>();
		inputTextBox.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent ke) {
				char key = ke.getKeyChar();
				if (key == '\n') {
					if(getSelectedIndex() == -1) {
						setSelectedIndex(0);
					}
				} else if (Character.isLetterOrDigit(key) || 
						key == '\b' ||
						key == ' ' ||
						key == ',' ||
						key == '(' ||
						key == ')') {
					setMatchingPersons(key);
				}
			}
		});
		
		setEditable(true);
	}
	
	/**
	 * Get the person selected in the PersonComboBox
	 * @return						Person object that is selected
	 */
	public Person getSelectedPerson() {
		int selectedIndex = getSelectedIndex();
		if(selectedIndex == -1) {
			return null;
		}
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
	private void setMatchingPersons(char input) {
		String oldInput = inputTextBox.getText();
		String search = oldInput + input;
		
		// handle backspace
		if (input == '\b') {
			if(oldInput.length() >= 1) {
			search = search.substring(0, search.length()-1);
			} else if (oldInput.length() == 0) {
				search = "";
			}
		} 
		removeAllItems();
		currentPersons.clear();
		
		ArrayList<Person> topPersons = new ArrayList<Person>();
		// collect persons that contain the search string
		for (Person person: personList) {
			String personString = getPersonEntry(person);
			if(personString.toLowerCase().startsWith(search.toLowerCase())) {
				topPersons.add(person);
			} else if(personString.toLowerCase().contains(search.toLowerCase())) {
				currentPersons.add(person);
			}
		}
		currentPersons.addAll(0, topPersons);
		
		// add all persons to list
		for (Person person: currentPersons) {
			addItem(getPersonEntry(person));
		}
		
		if(currentPersons.size() > 0) {
			if (currentPersons.size() > 8) {
				setMaximumRowCount(8);
			} else {
				setMaximumRowCount(currentPersons.size());
			}
			setPopupVisible(true);
		} else {
			setPopupVisible(false);
		}
		
		if(search == "") {
			setPopupVisible(false);
		}
		
		inputTextBox.setText(oldInput);
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
