package view.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import util.Person;
import view.IViewToModelAdaptor;
import view.component.PersonComboBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PanelCheckIn extends JPanel {

	private static final long serialVersionUID = -5330361979358721465L;
	
	private PersonComboBox comboBoxStudentName;
	private JTextField textFieldComment;
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
				FormFactory.DEFAULT_COLSPEC,
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
		
		comboBoxStudentName = new PersonComboBox();

		add(comboBoxStudentName, "3, 3, fill, top");

		JLabel lblNewLabel = new JLabel("Comment:");
		add(lblNewLabel, "3, 5, left, bottom");
		
		textFieldComment = new JTextField();
		add(textFieldComment, "3, 6, fill, default");
		textFieldComment.setColumns(30);
		textFieldComment.setToolTipText("Optional comment to be displayed with email notification");
		
		JButton btnConfirmCheckIn = new JButton("Confirm");
		btnConfirmCheckIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				checkInSelection();
			}
		});
		add(btnConfirmCheckIn, "3, 8, center, default");
		
	}
	
	/**
	 * Get a list of person objects from the database
	 */
	private ArrayList<Person> getPersonList() {
		ArrayList<Person> personList = modelAdaptor.getPersonList("");
		
		// sort
		Collections.sort(personList, new Comparator<Person>() {
			public int compare(Person p1, Person p2) {
				// last name
				if(p1.getLastName().toLowerCase() != p2.getLastName().toLowerCase()) {
					return p1.getLastName().toLowerCase().compareTo(p2.getLastName().toLowerCase());
				} 
				// first name
				if (p1.getFirstName().toLowerCase() != p2.getFirstName().toLowerCase()) {
					return p1.getFirstName().toLowerCase().compareTo(p2.getFirstName().toLowerCase());
				} 
				// personID
				return p1.getPersonID().toLowerCase().compareTo(p2.getPersonID().toLowerCase());
			}
		});
		
		return personList;
	}
	
	/**
	 * Check in the selected person
	 */
	private void checkInSelection() {
		
		Person owner = comboBoxStudentName.getSelectedPerson();
		
		if (owner == null) {
			JOptionPane.showMessageDialog(frame, "Please choose a name from the provided list.", 
					"Invalid Person", JOptionPane.WARNING_MESSAGE);
			comboBoxStudentName.getEditor().getEditorComponent().requestFocus();
		} else {
			// check into database
			long pkgID = modelAdaptor.checkInPackage(owner.getPersonID(), textFieldComment.getText());
			
			// print a label
			if (!modelAdaptor.printLabel(pkgID)) {
				JOptionPane.showMessageDialog(frame, "Failed to print the package label.\n"
						+ "Please reprint the label from the packages tab of the admin panel.", 
						"Failed Print", JOptionPane.WARNING_MESSAGE);
			}
			
			// send a package notification
			if (!modelAdaptor.sendPackageNotification(owner.getPersonID(), pkgID)) {
				JOptionPane.showMessageDialog(frame, "Failed to send package notification.\n"
						+ "Please resend notification from the packages tab of the admin panel.", 
						"Failed Notification", JOptionPane.WARNING_MESSAGE);
			}
			
			// notify success
			JOptionPane.showMessageDialog(frame, "Package for " + owner.getFullName() + " successfully checked in!", 
					"Success", JOptionPane.INFORMATION_MESSAGE);
		}
		
		// reset fields
		textFieldComment.setText("");
		comboBoxStudentName.getEditor().setItem("");
		comboBoxStudentName.getEditor().getEditorComponent().requestFocus();

	}

	public void init() {
		comboBoxStudentName.removeAllItems();
		comboBoxStudentName.setPersonList(getPersonList());
		comboBoxStudentName.getEditor().setItem("");
	}
	
}
