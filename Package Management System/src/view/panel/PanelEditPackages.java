package view.panel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter.SortKey;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import util.Pair;
import util.Person;
import util.Package;
import view.IViewToModelAdaptor;
import view.MainFrame;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class PanelEditPackages extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2711122506103907410L;
	private JTextField filterText;
	private JCheckBox chckbxActivePackagesOnly;
	
	private MainFrame frame;
	private IViewToModelAdaptor modelAdaptor;
	
	private JTable tableActivePackages;
	private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JPopupMenu popup;
	
	private ArrayList<Pair<Person,Package>> packages;
	private JLabel lblSearch;

	public PanelEditPackages(MainFrame frame, IViewToModelAdaptor modelAdaptor) {
		
		this.frame = frame;
		this.modelAdaptor = modelAdaptor;
		
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
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
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		
		// popup menu
		popup = new JPopupMenu();
		JMenuItem printLabelItem = new JMenuItem("Print Label");
		printLabelItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reprintLabel();
			}
		});
		popup.add(printLabelItem);
		JMenuItem resendNotificationItem = new JMenuItem("Resend Notification");
		resendNotificationItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resendNotification();
			}
		});
		popup.add(resendNotificationItem);
		JMenuItem checkOutPackageItem = new JMenuItem("Check Out Package");
		checkOutPackageItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				checkOutPackage();
			}
		});
		popup.add(checkOutPackageItem);
		
		// active packages table
		tableActivePackages = new JTable();
		tableActivePackages.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int r = tableActivePackages.rowAtPoint(e.getPoint());
		        if (r >= 0 && r < tableActivePackages.getRowCount()) {
		            tableActivePackages.setRowSelectionInterval(r, r);
		        } else {
		            tableActivePackages.clearSelection();
		        }

		        int rowindex = tableActivePackages.getSelectedRow();
		        if (rowindex < 0)
		            return;
		        if (e.isPopupTrigger()) {
		        	popup.show(e.getComponent(), e.getX(), e.getY());
		        }
			}
		});
		tableActivePackages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sorter = new TableRowSorter<DefaultTableModel>();
		tableActivePackages.setRowSorter(sorter);
		
		tableModel = new DefaultTableModel() {
			private static final long serialVersionUID = 6852954056260852138L;
			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		tableActivePackages.setModel(tableModel);
		
		// filter text field
		filterText = new JTextField();
		filterText.getDocument().addDocumentListener(
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
		
		lblSearch = new JLabel("Search:");
		add(lblSearch, "3, 2");
		add(filterText, "5, 2, 3, 1");
		filterText.setColumns(10);
		
		JScrollPane tableActivePackagesScrollPane = new JScrollPane(tableActivePackages);
		tableActivePackages.setFillsViewportHeight(true);
		
		add(tableActivePackagesScrollPane, "3, 4, 5, 1, fill, fill");
		
		chckbxActivePackagesOnly = new JCheckBox("Active Packages Only");
		chckbxActivePackagesOnly.setToolTipText("Only show packages that have not been checked out.");		
		chckbxActivePackagesOnly.setSelected(true);
		chckbxActivePackagesOnly.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				generateTable();
			}
		});
		add(chckbxActivePackagesOnly, "3, 6, 3, 1, left, default");
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
	 * 		LastName, FirstName, netID, CheckInDate, CheckOutDate, PackageID
	 */
	private void generateTable() {
		packages = modelAdaptor.getPackages(buildFilter(), 
				"last_name=ASCENDING:first_name=ASCENDING:check_in_date=ASCENDING");
		
		// create headers
		Vector<String> dataHeaders = new Vector<String>();
		dataHeaders.add("Last Name");
		dataHeaders.add("First Name");
		dataHeaders.add("NetID");
		dataHeaders.add("Check In Date");
		dataHeaders.add("Check Out Date");
		dataHeaders.add("Package ID");

		// collect data
		SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		for (Pair<Person,Package> dbEntry : packages) {
			Vector<String> dataEntry = new Vector<String>();
			dataEntry.add(dbEntry.first.getLastName());
			dataEntry.add(dbEntry.first.getFirstName());
			dataEntry.add(dbEntry.first.getPersonID());
			dataEntry.add(ft.format(dbEntry.second.getCheckInDate()));
			
			if(dbEntry.second.getCheckOutDate() != null) {
				dataEntry.add(ft.format(dbEntry.second.getCheckOutDate()));
			} else {
				dataEntry.add("");
			}
			dataEntry.add(Long.valueOf(dbEntry.second.getPackageID()).toString());
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
	 * Function will build a filter for the database depending on whether or not
	 * the checked in packages only checkbox is selected.
	 * @return
	 */
	private String buildFilter() {
		String filter = "";
		if(chckbxActivePackagesOnly.isSelected()) {
			filter += "checked_in=true";
		}
		return filter;
	}
	
	/** 
     * Update the row filter regular expression from the expression in
     * the text box.
     */
    private void newFilter() {
        RowFilter<DefaultTableModel, Object> rf = null;
        //If current expression doesn't parse, don't update.
        String filtText = filterText.getText();
        filtText = filtText.replaceAll("[^a-zA-Z 0-9:.//]", "");
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
	 * Function will reprint label after requesting confirmation
	 * from the user for the specific label
	 */
	private void reprintLabel() {
		int row = tableActivePackages.getSelectedRow();
		String lastName = (String) tableActivePackages.getValueAt(row, 0);
		String firstName = (String) tableActivePackages.getValueAt(row, 1);
		String netID = (String) tableActivePackages.getValueAt(row,2);
		String checkOut = (String) tableActivePackages.getValueAt(row,4);
		String pkgID = (String) tableActivePackages.getValueAt(row,5);
		String message = "Reprint label for package (ID:" + 
				pkgID + ") for " + firstName + " " + lastName + " (" + netID + ")?";
		
		if(!checkOut.isEmpty()) {
			JOptionPane.showMessageDialog(frame, 
					"This package was already checked out on " + checkOut + "." +
					"\nNo label will be printed.", 
					"Package Checked Out", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		//TODO Add icon
		// get confirmation and reprint the label
		if(JOptionPane.showConfirmDialog(frame, message, "Reprint Label", 
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			modelAdaptor.printLabel(Long.valueOf(pkgID));
		}
	}
	
	/**
	 * Function will resend an email notification with confirmation
	 */
	private void resendNotification() {
		int row = tableActivePackages.getSelectedRow();
		String lastName = (String) tableActivePackages.getValueAt(row, 0);
		String firstName = (String) tableActivePackages.getValueAt(row, 1);
		String netID = (String) tableActivePackages.getValueAt(row,2);
		String checkOut = (String) tableActivePackages.getValueAt(row,4);
		String pkgID = (String) tableActivePackages.getValueAt(row,5);
		String message = "Resend email notification to " + 
				firstName + " " + lastName + " (" + netID + ")" + 
				" for package (ID:" + pkgID + ")?";
		
		//TODO Add icon
		// get confirmation and send package notification
		if(!checkOut.isEmpty()) {
			JOptionPane.showMessageDialog(frame, 
					"This package was already checked out on " + checkOut + "." +
					"\nNo notification will be sent.", 
					"Package Checked Out", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if(JOptionPane.showConfirmDialog(frame, message, "Resend Package Notification", 
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			modelAdaptor.sendPackageNotification(netID, Long.valueOf(pkgID));
		}
	}
	
	/**
	 * Function checks out a package with confirmation
	 */
	private void checkOutPackage() {
		int row = tableActivePackages.getSelectedRow();
		String lastName = (String) tableActivePackages.getValueAt(row, 0);
		String firstName = (String) tableActivePackages.getValueAt(row, 1);
		String netID = (String) tableActivePackages.getValueAt(row,2);
		String checkOut = (String) tableActivePackages.getValueAt(row,4);
		String pkgID = (String) tableActivePackages.getValueAt(row,5);
		String message = "Check out package (ID:" + 
				pkgID + ") for " + firstName + " " + lastName + " (" + netID + ")? \n" +
				"Please note this cannot be undone!";
		
		if(!checkOut.isEmpty()) {
			JOptionPane.showMessageDialog(frame, 
					"This package was already checked out on " + checkOut + "." +
					"\nThe package will not be checked out again.", 
					"Package Checked Out", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		//TODO Add icon
		//get confirmation and check out package
		if(JOptionPane.showConfirmDialog(frame, message, "Check Out Package", 
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			modelAdaptor.checkOutPackage(Long.valueOf(pkgID));;
		}
		
		// regenerate the table
		generateTable();
	}

}
