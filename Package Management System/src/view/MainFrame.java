package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

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
import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JPasswordField;

import controller.Controller;

public class MainFrame extends JFrame {

	private IViewToModelAdaptor modelAdaptor;
	
	private JPanel contentPane;
	private JTextField textField;
	private JTable tableActivePackages;
	private JTable tableStudentInfo;
	private JTextField textFieldNewEmail;
	private JPasswordField passwordNewConfirm;
	private JPasswordField passwordNew;

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
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(30dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(30dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JLabel lblPleaseScanA = new JLabel("Please Scan a Package");
		lblPleaseScanA.setFont(new Font("Dialog", Font.BOLD, 24));
		panelPickUp.add(lblPleaseScanA, "4, 4, 3, 1, center, default");
		
		JLabel lblNewLabel_1 = new JLabel("Christopher Henderson (cwh1)");
		lblNewLabel_1.setForeground(new Color(0, 0, 153));
		lblNewLabel_1.setFont(new Font("Dialog", Font.PLAIN, 32));
		panelPickUp.add(lblNewLabel_1, "4, 8, 3, 1, center, default");
		
		JLabel lblPleaseConfirmThat = new JLabel("Please confirm that you have picked up the correct package");
		lblPleaseConfirmThat.setFont(new Font("Dialog", Font.BOLD, 16));
		panelPickUp.add(lblPleaseConfirmThat, "4, 12, 3, 1, center, default");
		
		JButton btnConfirm_1 = new JButton("Confirm");
		panelPickUp.add(btnConfirm_1, "4, 14, right, default");
		
		JButton btnCancel = new JButton("Cancel");
		panelPickUp.add(btnCancel, "6, 14, left, default");
		
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
		
		JButton btnConfirm = new JButton("Confirm");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		panelCheckIn.add(btnConfirm, "3, 6, center, default");
		
		String adminPanelName = "<html><body><table width='80'><tr><td><center>Admin</center></td></tr></table></body></html>";
		JPanel panelAdmin = new JPanel();
		tabbedPane.addTab(adminPanelName, null, panelAdmin, null);
		panelAdmin.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPaneAdmin = new JTabbedPane(JTabbedPane.TOP);
		panelAdmin.add(tabbedPaneAdmin, BorderLayout.CENTER);
		
		JPanel panelActivePackages = new JPanel();
		tabbedPaneAdmin.addTab("Active Packages", null, panelActivePackages, null);
		panelActivePackages.setLayout(new FormLayout(new ColumnSpec[] {
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
		panelActivePackages.add(tableActivePackages, "4, 2, fill, fill");
		
		JPanel panelChangeEmailPassword = new JPanel();
		tabbedPaneAdmin.addTab("Change Email/Password", null, panelChangeEmailPassword, null);
		panelChangeEmailPassword.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(125dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JLabel lblNewEmail = new JLabel("Email");
		panelChangeEmailPassword.add(lblNewEmail, "4, 5");
		
		textFieldNewEmail = new JTextField();
		panelChangeEmailPassword.add(textFieldNewEmail, "6, 5, fill, default");
		textFieldNewEmail.setColumns(10);
		
		JLabel lblNewPassword = new JLabel("New Password");
		panelChangeEmailPassword.add(lblNewPassword, "4, 6");
		
		passwordNew = new JPasswordField();
		panelChangeEmailPassword.add(passwordNew, "6, 6, fill, default");
		
		JLabel lblConfirmNewPassword = new JLabel("Confirm New Password");
		panelChangeEmailPassword.add(lblConfirmNewPassword, "4, 7");
		
		passwordNewConfirm = new JPasswordField();
		panelChangeEmailPassword.add(passwordNewConfirm, "6, 7, fill, default");
		
		JButton btnSubmitEmailChanges = new JButton("Submit");
		panelChangeEmailPassword.add(btnSubmitEmailChanges, "6, 9, center, default");
		
		JScrollPane scrollPaneEditStudentInfo = new JScrollPane();
		tabbedPaneAdmin.addTab("Edit Student Information", null, scrollPaneEditStudentInfo, null);
		
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
		
		JButton btnSave = new JButton("Save");
		btnSave.setToolTipText("Save changes to student information.");
		panelStudInfoImportSave.add(btnSave, "3, 1, right, center");
		
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
}
