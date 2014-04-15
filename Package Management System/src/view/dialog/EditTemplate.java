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

import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import util.FileIO;

import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;

/**
 * Dialog that allows the user to change the email template.
 * @author Chris 
 *
 */
public class EditTemplate extends JDialog {

	private static final long serialVersionUID = -4934812984844156761L;
	
	private final JPanel panelEditEmail = new JPanel();
	private String newTemplate = null;

	private JSplitPane splitPane;
	

	private String sidebarHTML;

	/**
	 * Create the dialog with specified previous Alias and email parameters
	 */
	public EditTemplate(JFrame frame, String oldTemplate) {
		super(frame,true);
		setTitle("Edit Email Template");
		setSize(1000, 450);
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
		JLabel lblNote = new JLabel("Edit the Email templates below.  Reminders for the format are in the box to the right.");
		panelEditEmail.add(lblNote, "2, 2, center, default");
		
		this.splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		splitPane.setResizeWeight(1.0);
		panelEditEmail.add(splitPane, "2, 4, fill, fill");
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		final JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setText(oldTemplate);
		
		textArea.setPreferredSize(null);
		
		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(scrollPane_1);
		
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
		
		JEditorPane lblNewLabel = new JEditorPane();
		
		// Allow links in the HTML help file to open in the default web browser
		lblNewLabel.addHyperlinkListener(new HyperlinkListener() {
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
		lblNewLabel.setContentType("text/html");
		lblNewLabel.setText(sidebarHTML);
		lblNewLabel.setEditable(false);
		scrollPane_1.setViewportView(lblNewLabel);
	
		
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
		splitPane.setDividerLocation(0.8);
		return newTemplate;
	}
}
