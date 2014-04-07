package view.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter.SortKey;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import util.Person;
import view.IViewToModelAdaptor;
import view.MainFrame;
import view.dialog.AddPerson;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class PanelStudentInformation extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3066818563731811034L;
	
	private MainFrame frame;
	private IViewToModelAdaptor modelAdaptor;
	
	private JScrollPane tableStudentInfoScrollPane;
	
	private JTable tableStudentInfo;
	private DefaultTableModel tableModel;
	private TableRowSorter<DefaultTableModel> sorter;
	
	private ArrayList<Person> personList;
	private JTextField filterField;

	private JPopupMenu popup;

	
	public PanelStudentInformation(MainFrame frame, IViewToModelAdaptor modelAdaptor) {
		
		this.frame = frame;
		this.modelAdaptor = modelAdaptor;
		
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		// popup menu
		popup = new JPopupMenu();
		JMenuItem editStudentItem = new JMenuItem("Edit Student");
		editStudentItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editStudent();
			}
		});
		popup.add(editStudentItem);
		JMenuItem deleteStudentItem = new JMenuItem("Delete Student");
		deleteStudentItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteStudent();
			}
		});
		popup.add(deleteStudentItem);
		
		JLabel lblSearch = new JLabel("Search:");
		add(lblSearch, "3, 2, right, default");
		
		filterField = new JTextField();
		filterField.getDocument().addDocumentListener(
                new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        newFilter();
                    }
                    public void insertUpdate(DocumentEvent e) {
                        newFilter();
                    }
                    public void removeUpdate(DocumentEvent e) {
                        newFilter();
                    }
                });
		add(filterField, "5, 2, 4, 1, fill, default");
		filterField.setColumns(10);
		
		tableStudentInfo = new JTable();
		tableStudentInfo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sorter = new TableRowSorter<DefaultTableModel>();
		tableStudentInfo.setRowSorter(sorter);
		
		tableModel = new DefaultTableModel() {
			private static final long serialVersionUID = -8300662810704394429L;
			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		tableStudentInfo.setModel(tableModel);
		
		tableStudentInfoScrollPane = new JScrollPane(tableStudentInfo);
		tableStudentInfo.setFillsViewportHeight(true);
		tableStudentInfo.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int r = tableStudentInfo.rowAtPoint(e.getPoint());
		        if (r >= 0 && r < tableStudentInfo.getRowCount()) {
		            tableStudentInfo.setRowSelectionInterval(r, r);
		        } else {
		            tableStudentInfo.clearSelection();
		        }

		        int rowindex = tableStudentInfo.getSelectedRow();
		        if (rowindex < 0)
		            return;
		        if (e.isPopupTrigger()) {
		        	popup.show(e.getComponent(), e.getX(), e.getY());
		        }
			}
		});

		
		add(tableStudentInfoScrollPane, "3, 4, 6, 7, fill, fill");
		
		JButton btnImport = new JButton("Import");
		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				importCSV();
			}
		});
		add(btnImport, "3, 12, 3, 1, left, bottom");
		btnImport.setToolTipText("Import student information from a csv file. Format: LastName,FirstName,NetID");
		
		JButton btnAddStudent = new JButton("Add");
		btnAddStudent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addStudent();
			}
		});
		add(btnAddStudent, "8, 12, right, default");
		btnAddStudent.setToolTipText("Add a new person to the system.");
	}

	/**
	 * Initializes the panel
	 */
	public void init() {
		generateTable();
	}
	
	/**
	 * Generates a table from the database with filter supplied from buildFilter and default
	 * sorting options
	 * Rows of the table are as follows:
	 * 		LastName, FirstName, EmailAddress, netID,
	 */
	private void generateTable() {
		// get a list of people currently in the database
		personList = modelAdaptor.getPersonList("");
		
		// sort by last name, then first name, then netID
		Collections.sort(personList, new Comparator<Person>() {
			public int compare(Person p1, Person p2) {
				// last name
				if(p1.getLastName().toLowerCase() != p2.getLastName().toLowerCase()) {
					return p1.getLastName().toLowerCase().compareTo(p2.getLastName().toLowerCase());
				} 
				// first name
				if (p1.getFirstName().toLowerCase() != p2.getFirstName().toLowerCase()) {
					return p1.getFirstName().toLowerCase().compareTo(p2.getFirstName().toLowerCase());
				} 
				// personID
				return p1.getPersonID().toLowerCase().compareTo(p2.getPersonID().toLowerCase());
			}
		});
		
		// create headers
		Vector<String> dataHeaders = new Vector<String>();
		dataHeaders.add("Last Name");
		dataHeaders.add("First Name");
		dataHeaders.add("NetID");
		dataHeaders.add("Email Address");
		
		// collect data
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		for(Person person: personList) {
			Vector<String> dataEntry = new Vector<String>();
			dataEntry.add(person.getLastName());
			dataEntry.add(person.getFirstName());
			dataEntry.add(person.getPersonID());
			dataEntry.add(person.getEmailAddress());
			data.add(dataEntry);
		}
		
		// get the sort and filter keys for setting the sorter after the model is set
		List<? extends SortKey> sortKeys = sorter.getSortKeys();
		RowFilter<? super DefaultTableModel, ? super Integer> rowFilter = sorter.getRowFilter();
		
		// set model for table
		tableModel.setDataVector(data, dataHeaders);
		// sort table
		sorter.setModel(tableModel);
		sorter.setSortKeys(sortKeys);
		sorter.setRowFilter(rowFilter);
		
	}

	/**
	 * Import a csv file containing information about all of the people
	 * that this system will check packages in and out for
	 */
	private void importCSV() {
		//Create a file chooser
		final JFileChooser fc = new JFileChooser();
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new FileFilter() {
			public boolean accept(File f) {
				if (f.isDirectory()) { return true; }
				if (f.getName().endsWith(".csv")) { return true; }
				return false;
			}
			public String getDescription() {
				return "CSV Files (*.csv)";
			}
		});
		
		//Open Dialog
		int returnVal = fc.showOpenDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
            File csvFile = fc.getSelectedFile();
            modelAdaptor.importPersonCSV(csvFile.getAbsolutePath());
        }
		
		// regenerate the table
		generateTable();
	}
	
	/** 
     * Update the row filter regular expression from the expression in
     * the text box.
     */
    private void newFilter() {
    	RowFilter<DefaultTableModel, Object> rf = null;
        //If current expression doesn't parse, don't update.
        String filtText = filterField.getText();
        filtText = filtText.replaceAll("[^a-zA-Z 0-9._]", "");
        String[] filters = filtText.split(" ");
        ArrayList<RowFilter<Object, Object>> rowFilters = 
        		new ArrayList<RowFilter<Object, Object>>();
        try {
        	for (String filter: filters) {
        		rowFilters.add(RowFilter.regexFilter("(?i)" + filter));
        	}
        	rf = RowFilter.andFilter(rowFilters);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
    
    /**
     * Creates a dialog that adds a student to the database
     */
	private void addStudent() {
		AddPerson addDlg = new AddPerson(frame,null,null,null,null,AddPerson.Predicate.ADD_PERSON);
		String[] result = addDlg.showDialog();
		if(result != null) {
			String lastName = result[0];
			String firstName = result[1];
			String netID = result[2];
			String emailAddress = result[3];
			if (modelAdaptor.addPerson(netID, firstName, lastName, emailAddress)) {
				JOptionPane.showMessageDialog(frame, 
						firstName + " " + lastName + " (" + netID + ") "
								+ "was successfully added.", 
								null, JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(frame, 
						firstName + " " + lastName + " (" + netID + ") "
								+ "is already in the system and cannot be added again. \n"
								+ "Please delete the old entry before adding a new student.", 
								null, JOptionPane.WARNING_MESSAGE);
			}
		}
		
		generateTable();
	}
	
	
	/**
	 * Creates a dialog that edits a person in the database
	 */
	private void editStudent() {
		
		int row = tableStudentInfo.getSelectedRow();
		String oldLastName = (String) tableStudentInfo.getValueAt(row, 0);
		String oldFirstName = (String) tableStudentInfo.getValueAt(row, 1);
		String netID = (String) tableStudentInfo.getValueAt(row, 2);
		String oldEmail = (String) tableStudentInfo.getValueAt(row, 3);
		
		AddPerson editDlg = new AddPerson(frame, oldLastName, oldFirstName, 
				netID, oldEmail, AddPerson.Predicate.EDIT_PERSON);
		String[] result = editDlg.showDialog();
		if(result != null) {
			String lastName = result[0];
			String firstName = result[1];
			String emailAddress = result[3];
			if (modelAdaptor.editPerson(netID, firstName, lastName, emailAddress)) {
				JOptionPane.showMessageDialog(frame, 
						firstName + " " + lastName + " (" + netID + ") "
								+ "was successfully edited.", 
								null, JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(frame, 
						firstName + " " + lastName + " (" + netID + ") "
								+ "was not found. \n", 
								null, JOptionPane.WARNING_MESSAGE);
			}
		}
		
		generateTable();
	}
	
	/**
	 * Creates a dialog that confirms the deletion of the student 
	 * from the database and does so.
	 */
	private void deleteStudent() {
		
		int row = tableStudentInfo.getSelectedRow();
		String lastName = (String) tableStudentInfo.getValueAt(row, 0);
		String firstName = (String) tableStudentInfo.getValueAt(row, 1);
		String netID = (String) tableStudentInfo.getValueAt(row, 2);
		String message = "Are you sure you want to delete " + firstName + " " + lastName + 
				" (" + netID + ")? \n";
		
		if(JOptionPane.showConfirmDialog(frame, message, "Delete Student", 
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			if(modelAdaptor.deletePerson(netID)) {
			JOptionPane.showMessageDialog(frame, 
					firstName + " " + lastName + " (" + netID + ") "
							+ "was successfully deleted.", 
							null, JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(frame, 
						firstName + " " + lastName + " (" + netID + ") "
								+ "was not deleted.", 
								null, JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
		generateTable();
	}
	

}
