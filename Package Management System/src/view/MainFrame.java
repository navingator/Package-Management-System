package view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import view.dialog.ChangeEmail;
import view.panel.PanelCheckIn;
import view.panel.PanelPickUp;
import view.panel.TabbedPaneAdmin;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3659030814987459253L;

	private IViewToModelAdaptor modelAdaptor;
	
	private JPanel contentPane;
	private MainFrame frame;
	/**
	 * Create the frame.
	 */
	public MainFrame(IViewToModelAdaptor adpt) {
		setTitle("Package Management System");
		
		// set model adaptor
		this.modelAdaptor = adpt;
		this.frame = this;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 450);
		setLocationRelativeTo(null);
		
		//TODO Menu Bar
//		JMenuBar menuBar = new JMenuBar();
//		setJMenuBar(menuBar);
//		
//		JMenu mnAdmin = new JMenu("File");
//		mnAdmin.setMnemonic('f');
//		menuBar.add(mnAdmin);
//		
//		JMenuItem mntmClose = new JMenuItem("Close");
//		mnAdmin.add(mntmClose);
//		
//		JMenu mnHelp = new JMenu("Help");
//		mnHelp.setMnemonic('h');
//		menuBar.add(mnHelp);
//		
//		JMenuItem mntmHelpFiles = new JMenuItem("Help Files");
//		mnHelp.add(mntmHelpFiles);
//		
//		JMenuItem mntmAbout = new JMenuItem("About");
//		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		

		
		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		final String pickUpPanelName = "<html><body><table width='80'><tr><td><center>Pick Up</center></td></tr></table></body></html>";
		final String checkInPanelName = "<html><body><table width='80'><tr><td><center>Check In</center></td></tr></table></body></html>";
		final String adminPanelName = "<html><body><table width='80'><tr><td><center>Admin</center></td></tr></table></body></html>";

		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		final PanelPickUp panelPickUp = new PanelPickUp(frame,modelAdaptor);
		tabbedPane.addTab(pickUpPanelName, null, panelPickUp, null);
		
		final PanelCheckIn panelCheckIn = new PanelCheckIn(frame,modelAdaptor);
		tabbedPane.addTab(checkInPanelName, null, panelCheckIn, null);
		
		final JPanel panelAdmin = new JPanel();
		tabbedPane.addTab(adminPanelName, null, panelAdmin, null);
		
		panelAdmin.setLayout(new BorderLayout(0, 0));
		
		final TabbedPaneAdmin tabbedPaneAdmin = new TabbedPaneAdmin(frame,modelAdaptor);
		panelAdmin.add(tabbedPaneAdmin, BorderLayout.CENTER);
//		tabbedPane.addTab(adminPanelName, null, tabbedPaneAdmin, null);
		
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				switch(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex())) {
				case checkInPanelName:
					panelCheckIn.init();
					break;
				case adminPanelName:
					tabbedPaneAdmin.init();
				}
			}
		});

	}
	
	/*
	 * Start the frame
	 */
	public void start() {
		setVisible(true);
	}
	
	/**
	 * Display a message dialog with a given message and title
	 * @param message			Message to be displayed
	 * @param title				Title of the message to be displayed
	 */
	public void displayMessage(String message, String title) {
		JOptionPane.showMessageDialog(frame, message, title, JOptionPane.DEFAULT_OPTION);
	}
	
	/**
	 * Display an error dialog with given text and title
	 * @param error				Error string to be displayed
	 * @param title				Title of the error to be displayed
	 */
	public void displayError(String error, String title) {
		JOptionPane.showMessageDialog(frame, error, title, JOptionPane.ERROR_MESSAGE);
		
	}
	
	/**
	 * Display a warning dialog with given warning string and title
	 * @param warning			Warning string to be displayed
	 * @param title				Title of the warning to be displayed
	 */
	public void displayWarning(String warning, String title) {
		JOptionPane.showMessageDialog(frame, warning, title, JOptionPane.WARNING_MESSAGE);		
	}
	
	/**
	 * Get a choice from a list of options from the user through 
	 * a dialog box
	 * 
	 * @param message			Message string to be displayed 
	 * @param title				Title of the dialog box to be displayed
	 * @param choices			Choices to be displayed in the dialog box
	 * @return					String of the choice that is chosen
	 */
	public String getChoiceFromList(String message, String title, String[] choices) {
		return (String) JOptionPane.showInputDialog(frame, message, title, 
				JOptionPane.PLAIN_MESSAGE, null, choices, choices[0]);
	}
	
	public String getPrinterName(String[] printerNames) {
		return getChoiceFromList("Please choose a printer from the list below:", 
				"Change Printer", printerNames);
	}
	
	/**
	 * Get input in a yes/no style dialog
	 * @param message			Message string to be displayed
	 * @param title				Title of the dialog box to be displayed
	 * @param options			Options to be displayed, in order. E.g. {"Yes","No"}
	 * @return					Integer representing the option chosen
	 */
	public int getButtonInput(String message, String title, String[] options) {
		return JOptionPane.showOptionDialog(frame, message, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
	}
	
	public String[] changeEmail(String oldEmail, String oldPassword, String oldAlias) {
		ChangeEmail emailDlg = new ChangeEmail(frame,oldAlias,oldEmail);
		return emailDlg.showDialog();
	}
	
}
