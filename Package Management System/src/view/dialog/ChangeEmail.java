package view.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;

/**
 * Dialog that allows the user to change an email.
 * @author Navin
 *
 */
public class ChangeEmail extends JDialog {

	private static final long serialVersionUID = -8304864601186531112L;
	
	private final JPanel panelEditEmail = new JPanel();
	private JTextField textFieldNewEmailName;
	private JTextField textFieldNewEmail;
	private JPasswordField passwordNew;
	private String[] result;


	/**
	 * Create the dialog with specified previous Alias and email parameters
	 */
	public ChangeEmail(JFrame frame, String oldName, String oldEmail) {
		super(frame,true);
		setTitle("Change Email");
		setSize(550, 250);
		setLocationRelativeTo(frame);
		panelEditEmail.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("pref:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(90dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("pref:grow"),},
			new RowSpec[] {
				RowSpec.decode("default:grow"),
				FormFactory.PREF_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		// Warning label
		JLabel lblNote = new JLabel("<html><center>This email address should be specifically created for this purpose.<br><b>DO NOT use a personal/business address.</b></center></html>");
		panelEditEmail.add(lblNote, "4, 2, 5, 1, center, default");
		
		// New Name Alias field
		JLabel lblChangeEmailName = new JLabel("Alias");
		panelEditEmail.add(lblChangeEmailName, "4, 4, left, default");
		
		textFieldNewEmailName = new JTextField();
		textFieldNewEmailName.setToolTipText(
				"The name displayed in the \"From:\" field. For example, Jones Mail Room");
		textFieldNewEmailName.setColumns(10);
		textFieldNewEmailName.setText(oldName);
		textFieldNewEmailName.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent arg0) {
	   	    	SwingUtilities.invokeLater( new Runnable() {
	    				@Override
	    				public void run() {
	    					textFieldNewEmailName.selectAll();		
	    				}
	   	    	});
			}
		});
		panelEditEmail.add(textFieldNewEmailName, "6, 4, fill, default");

		// New Email Address field
		JLabel lblNewEmail = new JLabel("Email");
		panelEditEmail.add(lblNewEmail, "4, 5, left, default");
		
		textFieldNewEmail = new JTextField();
		textFieldNewEmail.setToolTipText("The email address from which all notifications and reminders will be sent");
		textFieldNewEmail.setColumns(10);
		textFieldNewEmail.setText(oldEmail);
		textFieldNewEmail.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent arg0) {
	   	    	SwingUtilities.invokeLater( new Runnable() {
	    				@Override
	    				public void run() {
	    					textFieldNewEmail.selectAll();		
	    				}
	   	    	});
			}
		});
		panelEditEmail.add(textFieldNewEmail, "6, 5, fill, default");
		
		// New Password Field
		JLabel lblNewPassword = new JLabel("Password");
		panelEditEmail.add(lblNewPassword, "4, 6, left, default");
		
		passwordNew = new JPasswordField();
		passwordNew.setToolTipText("The password for the email address account");
		panelEditEmail.add(passwordNew, "6, 6, fill, default");
		
		
		// Submit changes
		JButton btnSubmitEmailChanges = new JButton("Submit");
		btnSubmitEmailChanges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String newEmailName = textFieldNewEmailName.getText();
				String newEmailAddress = textFieldNewEmail.getText();
				String newPassword = new String(passwordNew.getPassword());
				
				// If all fields are nonempty and the passwords match, set result and dispose
				if(newEmailName.length() > 0 &&
						newEmailAddress.length() > 0 &&
						newPassword.length() > 0) {
						
					//TODO Check and see if it works with the server
					result = new String[3];
					result[0] = newEmailName;
					result[1] = newEmailAddress;
					result[2] = newPassword;
					
					setVisible(false);
					dispose();
				} else {
					// send message saying that all fields must be filled
					JOptionPane.showMessageDialog(null, "Please fill out all fields.");
				}
			}
		});
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		panelEditEmail.add(btnSubmitEmailChanges, "6, 8, center, default");
		getContentPane().add(panelEditEmail);
	}
	
	/**
	 * Opens the dialog and returns the new email information
	 * @return		0 - New Alias
	 * 				1 - New Email Address
	 * 				2 - New Password
	 */
	public String[] showDialog() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
		return result;
	}
}
