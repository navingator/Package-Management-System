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

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3659030814987459253L;

	private IViewToModelAdaptor modelAdaptor;
	
	private JPanel contentPane;
	private JTextField textField;
	private JTable tableActivePackages;
	private JTable tableStudentInfo;
	private JTextField textFieldNewEmail;
	private JPasswordField passwordNewConfirm;
	private JPasswordField passwordNew;
	private JTextField textField_1;
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
		JPanel panelPickUp = new JPanel();
		tabbedPane.addTab(pickUpPanelName, null, panelPickUp, null);
		panelPickUp.setLayout(new FormLayout(new ColumnSpec[] {
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
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JLabel lblPleaseScanA = new JLabel("Please Scan a Package");
		lblPleaseScanA.setFont(new Font("Dialog", Font.BOLD, 24));
		panelPickUp.add(lblPleaseScanA, "4, 4, center, default");
		
		textField_1 = new JTextField();
		textField_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField_1.setHorizontalAlignment(SwingConstants.CENTER);
		panelPickUp.add(textField_1, "4, 8, fill, default");
		textField_1.setColumns(10);
		
		JButton btnSubmit = new JButton("Submit");
		panelPickUp.add(btnSubmit, "4, 10, center, default");
		
		String checkInPanelName = "<html><body><table width='80'><tr><td><center>Check In</center></td></tr></table></body></html>";
		JPanel panelCheckIn = new JPanel();
		panelCheckIn.setBorder(null);
		tabbedPane.addTab(checkInPanelName, null, panelCheckIn, null);
		panelCheckIn.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("pref:grow"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("400px"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("pref:grow"),},
			new RowSpec[] {
				RowSpec.decode("36px:grow"),
				RowSpec.decode("20px"),
				RowSpec.decode("33px"),
				RowSpec.decode("168px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JLabel lblStudent = new JLabel("Student:");
		panelCheckIn.add(lblStudent, "3, 1, left, bottom");
		
		textField = new JTextField();
		panelCheckIn.add(textField, "3, 2, fill, top");
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Comment:");
		panelCheckIn.add(lblNewLabel, "3, 3, left, bottom");
		
		JTextArea textArea = new JTextArea();
		panelCheckIn.add(textArea, "3, 4, fill, fill");
		
		JButton btnConfirmCheckIn = new JButton("Confirm");
		btnConfirmCheckIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		panelCheckIn.add(btnConfirmCheckIn, "3, 6, center, default");
		
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
		
		JPanel panelEditEmail = new JPanel();
		tabbedPaneAdmin.addTab("Email PropertyHandler", null, panelEditEmail, null);
		panelEditEmail.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(125dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JLabel lblChangeEmailName = new JLabel("Name");
		panelEditEmail.add(lblChangeEmailName, "4, 3");
		
		textFieldNewEmailName = new JTextField();
		textFieldNewEmailName.setToolTipText("The name displayed in the \"From:\" field of all emails");
		panelEditEmail.add(textFieldNewEmailName, "6, 3, fill, default");
		textFieldNewEmailName.setColumns(10);
		
		JLabel lblNewEmail = new JLabel("Email");
		panelEditEmail.add(lblNewEmail, "4, 4");
		
		textFieldNewEmail = new JTextField();
		textFieldNewEmail.setToolTipText("The email that will be sending all notifications and reminders.");
		panelEditEmail.add(textFieldNewEmail, "6, 4, fill, default");
		textFieldNewEmail.setColumns(10);
		
		JLabel lblNewPassword = new JLabel("New Password");
		panelEditEmail.add(lblNewPassword, "4, 5");
		
		passwordNew = new JPasswordField();
		panelEditEmail.add(passwordNew, "6, 5, fill, default");
		
		JLabel lblConfirmNewPassword = new JLabel("Confirm New Password");
		panelEditEmail.add(lblConfirmNewPassword, "4, 6");
		
		passwordNewConfirm = new JPasswordField();
		panelEditEmail.add(passwordNewConfirm, "6, 6, fill, default");
		
		JButton btnSubmitEmailChanges = new JButton("Submit");
		panelEditEmail.add(btnSubmitEmailChanges, "6, 8, center, default");
		
		JScrollPane scrollPaneEditStudentInfo = new JScrollPane();
		tabbedPaneAdmin.addTab("Student Information", null, scrollPaneEditStudentInfo, null);
		
		JPanel panelStudInfoImportSave = new JPanel();
		scrollPaneEditStudentInfo.setColumnHeaderView(panelStudInfoImportSave);
		panelStudInfoImportSave.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("100dlu:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(101dlu;default):grow"),},
			new RowSpec[] {
				RowSpec.decode("26px"),}));
		
		JButton btnImport = new JButton("Import");
		btnImport.setToolTipText("Import student information from a csv file. Format: LastName,FirstName,NetID");
		panelStudInfoImportSave.add(btnImport, "1, 1, left, center");
		
		JButton btnAddStudent = new JButton("Add Student");
		btnAddStudent.setToolTipText("Save changes to student information.");
		panelStudInfoImportSave.add(btnAddStudent, "3, 1, right, center");
		
		JPanel panelStudInfoTable = new JPanel();
		scrollPaneEditStudentInfo.setViewportView(panelStudInfoTable);
		panelStudInfoTable.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		
		tableStudentInfo = new JTable();
		tableStudentInfo.setCellSelectionEnabled(true);
		panelStudInfoTable.add(tableStudentInfo, "1, 1, fill, fill");
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
