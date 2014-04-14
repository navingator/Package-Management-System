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
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextPane;
import javax.swing.JSplitPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import javax.swing.JTextArea;

/**
 * Dialog that allows the user to change the email template.
 * @author Chris 
 *
 */
public class EditTemplate extends JDialog {

	private static final long serialVersionUID = -4934812984844156761L;
	
	private final JPanel panelEditEmail = new JPanel();
	private String newTemplate = null;


	/**
	 * Create the dialog with specified previous Alias and email parameters
	 */
	public EditTemplate(JFrame frame, String oldTemplate) {
		super(frame,true);
		setTitle("Edit Email Template");
		setSize(900, 450);
		setLocationRelativeTo(frame);
		panelEditEmail.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(90dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));

		
		// Warning label
		JLabel lblNote = new JLabel("<html>\r\n<center>\r\nEdit the email templates below.<br>\r\nRecall that HTML may be used. Some basic HTML tags are:<br>\r\n&lt;b&gt;bold&lt;/b&gt; for <b>bold</b> <br>\r\n&lt;u&gt;underline&lt;/u&gt; for <u>underline</u> <br>\r\n&lt;em&gt;emphasis&lt;/em&gt; for <em>emphasis</em> <br>\r\n</center>\r\n</html>");
		panelEditEmail.add(lblNote, "2, 2, center, default");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panelEditEmail.add(scrollPane, "2, 4, fill, fill");
		
		final JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setText(oldTemplate);
	
		
		// Submit changes
		JButton btnSaveChanges = new JButton("Save");
		btnSaveChanges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// return new edited template file text
				newTemplate = textArea.getText();
				
				setVisible(false);
				dispose();
			}
		});
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		panelEditEmail.add(btnSaveChanges, "2, 6, center, default");
		getContentPane().add(panelEditEmail);
	}
	
	/**
	 * Opens the dialog and returns the new email information
	 * @return		0 - New Alias
	 * 				1 - New Email Address
	 * 				2 - New Password
	 */
	public String showDialog() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
		
		return newTemplate;
	}
}
