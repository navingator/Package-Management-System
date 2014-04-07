package view.dialog;

import java.awt.BorderLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AddPerson extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8186594262976218336L;
	private JTextField textFieldLastName;
	private JTextField textFieldFirstName;
	private JTextField textFieldEmail;
	private JTextField textFieldNetID;
	private JButton btnSubmitNewPerson;
	private String[] result;

	
	public static enum Predicate {
		ADD_PERSON,
		EDIT_PERSON
	}

	/**
	 * Create the dialog.
	 */
	public AddPerson(JFrame frame, String oldLastName, String oldFirstName, 
			String netID, String oldEmail, Predicate pred) {
		super(frame,true);
		setSize(400, 200);
		setLocationRelativeTo(frame);
		
		if(pred == Predicate.ADD_PERSON) { setTitle("Add Person"); }
		if(pred == Predicate.EDIT_PERSON) { setTitle("Edit Person"); }
		
		getContentPane().setLayout(new BorderLayout());
		
		JPanel panelAddPerson = new JPanel();
		getContentPane().add(panelAddPerson, BorderLayout.CENTER);
		panelAddPerson.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
				
		
		// Last Name field
		JLabel lblLastName = new JLabel("Last Name");
		panelAddPerson.add(lblLastName, "4, 4");
		
		textFieldLastName = new JTextField();
		textFieldLastName.setColumns(10);
		textFieldLastName.setText(oldLastName);
		textFieldLastName.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent arg0) {
	   	    	SwingUtilities.invokeLater( new Runnable() {
	    				@Override
	    				public void run() {
	    					textFieldLastName.selectAll();		
	    				}
	   	    	});
			}
		});
		panelAddPerson.add(textFieldLastName, "6, 4, fill, default");
		
		// First Name field
		textFieldFirstName = new JTextField();
		textFieldFirstName.setColumns(10);
		textFieldFirstName.setText(oldFirstName);
		textFieldFirstName.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent arg0) {
	   	    	SwingUtilities.invokeLater( new Runnable() {
	    				@Override
	    				public void run() {
	    					textFieldFirstName.selectAll();		
	    				}
	   	    	});
			}
		});
		
		JLabel lblFirstName = new JLabel("First Name");
		panelAddPerson.add(lblFirstName, "4, 5");
		panelAddPerson.add(textFieldFirstName, "6, 5, fill, default");
		
		// New Email Address field
		textFieldEmail = new JTextField();
		textFieldEmail.setColumns(10);
		textFieldEmail.setText(oldEmail);
		textFieldEmail.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent arg0) {
	   	    	SwingUtilities.invokeLater( new Runnable() {
	    				@Override
	    				public void run() {
	    					textFieldEmail.selectAll();		
	    				}
	   	    	});
			}
		});
		JLabel lblEmail = new JLabel("Email");
		panelAddPerson.add(lblEmail, "4, 7");
		panelAddPerson.add(textFieldEmail, "6, 7, fill, default");
		
		
		// netID field
		JLabel lblNetid = new JLabel("NetID");
		panelAddPerson.add(lblNetid, "4, 6, left, default");
		
		textFieldNetID = new JTextField();
		panelAddPerson.add(textFieldNetID, "6, 6, fill, default");
		textFieldNetID.setColumns(10);
		textFieldNetID.setText(netID);
		textFieldNetID.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent arg0) {
	   	    	SwingUtilities.invokeLater( new Runnable() {
	    				@Override
	    				public void run() {
	    					textFieldNetID.selectAll();		
	    				}
	   	    	});
			}
		});
		
		
		// prevent change of netID when person is edited
		if(pred == Predicate.EDIT_PERSON) {
			textFieldNetID.setEnabled(false);
		}
		
		btnSubmitNewPerson = new JButton("Submit");
		btnSubmitNewPerson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				submitChanges();
			}
		});
		panelAddPerson.add(btnSubmitNewPerson, "6, 8, center, default");
	}

	/**
	 * Opens the dialog and returns the new person information
	 * @return					String Array containing the result
	 * 							0 - Last Name
	 * 							1 - First Name
	 * 							2 - NetID
	 * 							3 - Email Address
	 */
	public String[] showDialog() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
		return result;
	}
	
	private void submitChanges() {
		String lastName = textFieldLastName.getText();
		String firstName = textFieldFirstName.getText();
		String netID = textFieldNetID.getText();
		String emailAddress = textFieldEmail.getText();
		
		// If all fields are nonempty, set the result
		if(lastName.length() > 0 &&
				firstName.length() > 0 &&
				netID.length() > 0 &&
				emailAddress.length() > 0) {
			result = new String[4];
			result[0] = lastName;
			result[1] = firstName;
			result[2] = netID;
			result[3] = emailAddress;
			
			setVisible(false);
			dispose();
		} else {
			JOptionPane.showMessageDialog(null, "Please fill out all fields.");
		}
		
		
	}

}
