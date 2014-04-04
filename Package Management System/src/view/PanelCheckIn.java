package view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.JComboBox;

import util.Person;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PanelCheckIn extends JPanel {

	private static final long serialVersionUID = -5330361979358721465L;
	
	private JComboBox<Person> comboBoxStudentName;
	private JTextField textFieldComment;
	private ArrayList<Person> personList;
	private JFrame frame;
	private IViewToModelAdaptor modelAdaptor;

	/**
	 * Create the panel.
	 */
	public PanelCheckIn(JFrame frame, IViewToModelAdaptor modelAdaptor) {
		
		this.frame = frame;
		this.modelAdaptor = modelAdaptor;

		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("pref:grow"),},
			new RowSpec[] {
				RowSpec.decode("36px:grow"),
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("20px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JLabel lblStudent = new JLabel("Student:");
		add(lblStudent, "3, 2, left, bottom");
		
		comboBoxStudentName = new JComboBox<Person>();
		comboBoxStudentName.setRenderer(new PersonComboBoxRenderer());
		comboBoxStudentName.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent arg0) {
				//TODO
				setMatchingPersons();
			}

		});
		comboBoxStudentName.getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent arg0) {
				setPersonList();
				//setMatchingPersons();
			}
		});
		comboBoxStudentName.setEditable(true);
		add(comboBoxStudentName, "3, 3, fill, top");
		
		JButton btnConfirmCheckIn = new JButton("Confirm");
		btnConfirmCheckIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				checkInSelection();
			}
		});
		
		JLabel lblNewLabel = new JLabel("Comment:");
		add(lblNewLabel, "3, 5, left, bottom");
		
		textFieldComment = new JTextField();
		add(textFieldComment, "3, 6, fill, default");
		textFieldComment.setColumns(20);
		add(btnConfirmCheckIn, "3, 8, center, default");
		
	}
	
	/**
	 * Get a list of person objects from the database
	 */
	private void setPersonList() {
		personList = modelAdaptor.getPersonList("");
		for (Person person: personList) {
			comboBoxStudentName.addItem(person);
		}
	}
	
	/**
	 * Check in the selected person
	 */
	private void checkInSelection() {
		Person owner = comboBoxStudentName.getItemAt(comboBoxStudentName.getSelectedIndex());
		modelAdaptor.checkInPackage(owner.getPersonID(), textFieldComment.getText());
		textFieldComment.setText("");
		comboBoxStudentName.getEditor().setItem("");
	}
	
	/**
	 * Find persons that match the search string and put them in the 
	 * JComboBox list.
	 */
	private void setMatchingPersons() {
		comboBoxStudentName.removeAllItems();
		String search = comboBoxStudentName.getEditor().getItem().toString().toLowerCase();
		for (Person person: personList) {
			String personString = person.getLastFirstName() + " (" + person.getPersonID() + ")";
			if(personString.toLowerCase().contains(search)) {
				comboBoxStudentName.addItem(person);
			}
			
			if(search.length() > 0) {
				comboBoxStudentName.showPopup();
			} else {
				comboBoxStudentName.hidePopup();
			}
		}
	}
	
	private class PersonComboBoxRenderer extends DefaultListCellRenderer {

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if(value instanceof Person){
                Person person = (Person) value;
                setText(person.getLastFirstName() + " (" + person.getPersonID() + ")");
            }
            return this;
		}
	}

}
