package view;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3659030814987459253L;

	private IViewToModelAdaptor modelAdaptor;
	
	private JPanel contentPane;
	private JTable tableActivePackages;
	private JTable tableStudentInfo;
	private JFrame frame;

	/**
	 * Create the frame.
	 */
	public MainFrame(IViewToModelAdaptor adpt) {
		setTitle("Package Management System");
		
		// set model adaptor
		this.modelAdaptor = adpt;
		this.frame = this;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(720, 446);
		setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnAdmin = new JMenu("File");
		mnAdmin.setMnemonic('f');
		menuBar.add(mnAdmin);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mnAdmin.add(mntmClose);
		
		JMenu mnHelp = new JMenu("Help");
		mnHelp.setMnemonic('h');
		menuBar.add(mnHelp);
		
		JMenuItem mntmHelpFiles = new JMenuItem("Help Files");
		mnHelp.add(mntmHelpFiles);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		String pickUpPanelName = "<html><body><table width='80'><tr><td><center>Pick Up</center></td></tr></table></body></html>";
		PanelPickUp panelPickUp = new PanelPickUp(frame,modelAdaptor);
		tabbedPane.addTab(pickUpPanelName, null, panelPickUp, null);
		
		String checkInPanelName = "<html><body><table width='80'><tr><td><center>Check In</center></td></tr></table></body></html>";
		JPanel panelCheckIn = new PanelCheckIn(frame,modelAdaptor);
		tabbedPane.addTab(checkInPanelName, null, panelCheckIn, null);
		
		String adminPanelName = "<html><body><table width='80'><tr><td><center>Admin</center></td></tr></table></body></html>";
		JPanel panelAdmin = new JPanel();
		tabbedPane.addTab(adminPanelName, null, panelAdmin, null);
		panelAdmin.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPaneAdmin = new JTabbedPane(JTabbedPane.TOP);
		panelAdmin.add(tabbedPaneAdmin, BorderLayout.CENTER);
		
		JPanel panelEditPackages = new JPanel();
		tabbedPaneAdmin.addTab("Packages", null, panelEditPackages, null);
		panelEditPackages.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.MIN_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.MIN_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		tableActivePackages = new JTable();
		tableActivePackages.setCellSelectionEnabled(true);
		panelEditPackages.add(tableActivePackages, "4, 2, fill, fill");
		
		JScrollPane scrollPaneEditStudentInfo = new JScrollPane();
		tabbedPaneAdmin.addTab("Student Information", null, scrollPaneEditStudentInfo, null);
		
		JPanel panelStudInfoTable = new JPanel();
		scrollPaneEditStudentInfo.setViewportView(panelStudInfoTable);
		panelStudInfoTable.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JButton btnImport = new JButton("Import");
		panelStudInfoTable.add(btnImport, "1, 1, left, bottom");
		btnImport.setToolTipText("Import student information from a csv file. Format: LastName,FirstName,NetID");
		
		JButton btnAddStudent = new JButton("Add");
		panelStudInfoTable.add(btnAddStudent, "2, 1");
		btnAddStudent.setToolTipText("Save changes to student information.");
		
		tableStudentInfo = new JTable();
		tableStudentInfo.setCellSelectionEnabled(true);
		panelStudInfoTable.add(tableStudentInfo, "1, 2, 2, 7, fill, fill");
		
		JButton btnEditStudent = new JButton("Edit");
		panelStudInfoTable.add(btnEditStudent, "3, 4");
		
		JButton btnDeleteStudent = new JButton("Delete");
		panelStudInfoTable.add(btnDeleteStudent, "3, 6");
		
		JPanel panelEditEmail = new JPanel();
		tabbedPaneAdmin.addTab("Email and Printer", null, panelEditEmail, null);
		panelEditEmail.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JButton btnChangeEmail = new JButton("Change Email");
		btnChangeEmail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// open the change email dialog
				String oldEmail = modelAdaptor.getEmailAddress();
				String oldAlias = modelAdaptor.getEmailAlias();
				
				ChangeEmail emailDlg = new ChangeEmail(frame,oldAlias,oldEmail);
				String newEmail[] = emailDlg.showDialog();
				
				// make the changes in the model
				if(newEmail != null) {
					modelAdaptor.changeEmail(newEmail[0], newEmail[1], newEmail[2]);
				}
			}
		});
		panelEditEmail.add(btnChangeEmail, "4, 4, default, fill");
		
		JButton btnSelectPrinter = new JButton("Select Printer");
		btnSelectPrinter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String printerName = getPrinterName(modelAdaptor.getPrinterNames());
				modelAdaptor.setPrinter(printerName);
			}
		});
		panelEditEmail.add(btnSelectPrinter, "4, 6, default, fill");

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
