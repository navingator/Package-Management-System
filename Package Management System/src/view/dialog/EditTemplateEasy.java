package view.dialog;

import java.awt.BorderLayout;
import java.awt.Desktop;
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
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import util.FileIO;

import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.JCheckBox;

import java.awt.GridLayout;
import java.awt.Color;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

/**
 * Dialog that allows the user to change the email template.
 * @author Chris 
 *
 */
public class EditTemplateEasy extends JDialog {

	private static final long serialVersionUID = -4934812984844156761L;
	
	private final JPanel panelEditEmail = new JPanel();
	private Map<String,String> newTemplates = null;
	

	private String sidebarHTML;
	private JTextField txtSenderAlias;
	private JTextField txtNotificationSubject;
	private JTextField txtReminderSubject;

	/**
	 * Create the dialog with specified previous Alias and email parameters
	 */
	public EditTemplateEasy(JFrame frame, Map<String,String> templateMap) {
		super(frame,true);
		setTitle("Edit Email Template");
		setSize(850, 450);
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
		
		// Reminder label
		JLabel lblNote = new JLabel("Edit the Email templates below.  Reminders for the format are in the box to the right.");
		panelEditEmail.add(lblNote, "2, 2, center, default");
		
		// Load sidebar HTML help
		String RootDir = FileIO.getRootDir();
		String filePath = RootDir + "/template-help.html";
		
		
		try {
			// Read entire file into string
			sidebarHTML = FileIO.loadFileAsString(filePath);
			
			// Add link to the document itself that can be loaded in a web browser.
			String fileURL = filePath.replace("\\", "/").replace(" ", "%20");
			String browserLink = String.format("<i>To view this information in your browser, <a href=\"file:/%s\">click here.</a></i>", fileURL);

			sidebarHTML = sidebarHTML.replace("<body>", "<body>" + browserLink);
		} catch (IOException e) {
			sidebarHTML = "Could not load file \"template-help.html\".";
		}
	
		
		
		
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setResizeWeight(1.0);
		splitPane_1.setContinuousLayout(true);
		panelEditEmail.add(splitPane_1, "2, 4");
		

		
		JPanel pnlTemplates = new JPanel();
		pnlTemplates.setForeground(Color.BLACK);
		//pnlBody.add(pnlTemplates, BorderLayout.NORTH);
		pnlTemplates.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("129px"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(122dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("22px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(70dlu;pref)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(70dlu;default)"),}));
		
		JScrollPane scrollPane = new JScrollPane(pnlTemplates);
		splitPane_1.setLeftComponent(scrollPane);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		JPanel panel = new JPanel();
		pnlTemplates.add(panel, "2, 2, fill, fill");
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("129px"),},
			new RowSpec[] {
				RowSpec.decode("22px"),}));
		
		final JCheckBox chckbxAutoLinebreak = new JCheckBox("Auto-Linebreak");
		
		panel.add(chckbxAutoLinebreak, "1, 1, right, default");
		chckbxAutoLinebreak.setToolTipText("Automatically converts linebreaks into <br> HTML tags.  Unless you know what you're doing, you probably want this checked.");
		chckbxAutoLinebreak.setSelected(templateMap.get("AUTO-LINEBREAK").equals("TRUE"));
		
		System.out.format("AutoLinebreak: %s\n", chckbxAutoLinebreak.getSelectedObjects());
		
		JLabel lblSenderAlias = new JLabel("Sender Alias");
		pnlTemplates.add(lblSenderAlias, "2, 4, right, default");
		
		txtSenderAlias = new JTextField();
		pnlTemplates.add(txtSenderAlias, "4, 4, fill, default");
		txtSenderAlias.setColumns(10);
		txtSenderAlias.setText(templateMap.get("SENDER-ALIAS"));
		
		JLabel lblNotificationSubject = new JLabel("Notification Subject");
		pnlTemplates.add(lblNotificationSubject, "2, 6, right, default");
		
		txtNotificationSubject = new JTextField();
		pnlTemplates.add(txtNotificationSubject, "4, 6, 3, 1, fill, default");
		txtNotificationSubject.setColumns(10);
		txtNotificationSubject.setText(templateMap.get("NOTIFICATION-SUBJECT"));
		
		JLabel lblNotificationEmail = new JLabel("Notification Body");
		pnlTemplates.add(lblNotificationEmail, "2, 8, right, top");
		
		final JTextPane txtNotificationBody = new JTextPane();
		pnlTemplates.add(txtNotificationBody, "4, 8, 3, 1, fill, fill");
		txtNotificationBody.setText(templateMap.get("NOTIFICATION-BODY"));

		
		JLabel lblReminderSubject = new JLabel("Reminder Subject");
		pnlTemplates.add(lblReminderSubject, "2, 10, right, default");
		
		txtReminderSubject = new JTextField();
		pnlTemplates.add(txtReminderSubject, "4, 10, 3, 1, fill, default");
		txtReminderSubject.setColumns(10);
		txtReminderSubject.setText(templateMap.get("REMINDER-SUBJECT"));

		
		JLabel lblReminderBody = new JLabel("Reminder Body");
		pnlTemplates.add(lblReminderBody, "2, 12, right, top");

		
		final JTextPane txtReminderBody = new JTextPane();
		pnlTemplates.add(txtReminderBody, "4, 12, 3, 1, fill, fill");
		txtReminderBody.setText(templateMap.get("REMINDER-BODY"));

		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		splitPane_1.setRightComponent(scrollPane_1);
		
		JTextPane pnltxtHelp = new JTextPane();
		pnltxtHelp.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent event) {
		        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		        	String url = null;
		        	
		        	url = event.getURL().toString();

		        	try { 
						Desktop.getDesktop().browse(URI.create(url));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
		        }
		    }
		});
		pnltxtHelp.setContentType("text/html");
		pnltxtHelp.setText(sidebarHTML);
		pnltxtHelp.setEditable(false);
		scrollPane_1.setViewportView(pnltxtHelp);
		
		// Submit changes
		JButton btnSaveChanges = new JButton("Save");
		btnSaveChanges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// return new edited template text
				newTemplates = new HashMap<String,String>();
				newTemplates.put("AUTO-LINEBREAK", chckbxAutoLinebreak.getSelectedObjects() != null ? "TRUE" : "FALSE");
				newTemplates.put("SENDER-ALIAS", txtSenderAlias.getText());
				newTemplates.put("NOTIFICATION-SUBJECT", txtNotificationSubject.getText());
				newTemplates.put("NOTIFICATION-BODY", txtNotificationBody.getText());
				newTemplates.put("REMINDER-SUBJECT", txtReminderSubject.getText());
				newTemplates.put("REMINDER-BODY", txtReminderBody.getText());
				
				setVisible(false);
				dispose();
			}
		});
		
		panelEditEmail.add(btnSaveChanges, "2, 6, center, default");
		getContentPane().add(panelEditEmail);
	}
	
	/**
	 * Opens the dialog and returns the new email information
	 * @return		0 - New Alias
	 * 				1 - New Email Address
	 * 				2 - New Password
	 */
	public Map<String,String> showDialog() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
		//splitPane.setDividerLocation(0.8);
		return newTemplates;
	}
}
