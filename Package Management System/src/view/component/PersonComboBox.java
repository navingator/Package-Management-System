package view.component;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;

import util.Person;

public class PersonComboBox extends JComboBox<Person>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6672267768163626009L;
	private ArrayList<Person> personList;

	public PersonComboBox() {
		setRenderer(new PersonComboBoxRenderer());
		
		getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent arg0) {
				setMatchingPersons();
			}
		});
		
		setEditable(true);
	}
	
	/**
	 * Set the list of people available for the list
	 * @param personList			ArrayList of persons to put in list
	 */
	public void setPersonList(ArrayList<Person> personList) {
		this.personList = personList;
		for (Person person: personList) {
			addItem(person);
		}
	}

	/**
	 * Find persons that match the search string and put them in the 
	 * JComboBox list.
	 */
	private void setMatchingPersons() {
		removeAllItems();
		String search = "navin";
		for (Person person: personList) {
			String personString = getPersonEntry(person);
			if(personString.toLowerCase().contains(search.toLowerCase())) {
				addItem(person);
			}
			
			if(search.length() > 0) {
				showPopup();
			} else {
				hidePopup();
			}
		}
	}
	
	private String getPersonEntry(Person person) {
		return person.getLastFirstName() + " (" + person.getPersonID() + ")";
	}
	
	private class PersonComboBoxRenderer extends DefaultListCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8382546602369739973L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if(value instanceof Person){
                Person person = (Person) value;
                setText(getPersonEntry(person));
            }
            return this;
		}
	}

}
