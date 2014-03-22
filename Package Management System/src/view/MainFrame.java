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
import javax.swing.JTabbedPane;

import javax.swing.JTextField;
import javax.swing.JLabel;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JPasswordField;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3659030814987459253L;

	private IViewToModelAdaptor modelAdaptor;
	
	private JPanel contentPane;
	private JTable tableActivePackages;
	private JTable tableStudentInfo;
	private JTextField textFieldNewEmail;
	private JPasswordField passwordNewConfirm;
	private JPasswordField passwordNew;
	private JTextField textFieldNewEmailName;

	/**
	 * Create the frame.
	 */
	public MainFrame(IViewToModelAdaptor adpt) {
		
		// set model adaptor
		this.modelAdaptor = adpt;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 720, 446);
		
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
		PanelPickUp panelPickUp = new PanelPickUp();
		tabbedPane.addTab(pickUpPanelName, null, panelPickUp, null);
		
		String checkInPanelName = "<html><body><table width='80'><tr><td><center>Check In</center></td></tr></table></body></html>";
		JPanel panelCheckIn = new PanelCheckIn();
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
		panelEditEmail.add(btnChangeEmail, "4, 4, default, fill");
		
		JButton btnSelectPrinter = new JButton("Select Printer");
		panelEditEmail.add(btnSelectPrinter, "4, 6, default, fill");

	}
	
	/*
	 * Start the frame
	 */
	public void start() {
		setVisible(true);
	}
	
	//TODO
	public void displayMessage(String msg) {
		
	}
	
	//TODO
	public void displayError(String err) {
		
	}
	
	//TODO
	public String getChoiceFromList(String[] choices) {
		return null;
	}
	
	//TODO
	public String[] changeEmail(String senderAddress, String senderPassword, String senderAlias) {
		return null;
	}
	
}
