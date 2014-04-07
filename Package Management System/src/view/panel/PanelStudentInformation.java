package view.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
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
		
		tableModel = (DefaultTableModel) tableStudentInfo.getModel();
		
		tableStudentInfoScrollPane = new JScrollPane(tableStudentInfo);
		tableStudentInfo.setFillsViewportHeight(true);
		
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
        try {
            rf = RowFilter.regexFilter("(?i)" + filterField.getText(), 0,1,2);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
    
	private void addStudent() {
		AddPerson addDlg = new AddPerson(frame,null,null,null,null,AddPerson.Predicate.ADD_PERSON);
		String[] result = addDlg.showDialog();
		if(result != null) {
			modelAdaptor.addPerson(result[2], result[1], result[0], result[3]);
		}
	}
	
	private void editStudent(){
		AddPerson editDlg = new AddPerson(frame,null,null,null,null,AddPerson.Predicate.ADD_PERSON);
		String[] result = editDlg.showDialog();
		modelAdaptor.editPerson(result[2], result[1], result[0], result[3]);
	}
}
