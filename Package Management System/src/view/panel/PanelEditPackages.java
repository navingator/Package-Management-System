package view.panel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
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

	public PanelEditPackages(MainFrame frame, IViewToModelAdaptor modelAdaptor) {
		
		this.frame = frame;
		this.modelAdaptor = modelAdaptor;
		
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.MIN_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.MIN_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		
		popup = new JPopupMenu();
		JMenuItem printLabel = new JMenuItem("Print Label");
		popup.add(printLabel);
		JMenuItem resendNotification = new JMenuItem("Resend Notification");
		popup.add(resendNotification);
		JMenuItem checkOutPackage = new JMenuItem("Check Out Package");
		popup.add(checkOutPackage);
		
		filterText = new JTextField();
		add(filterText, "4, 2");
		filterText.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		add(btnSearch, "6, 2, left, default");
		
		tableActivePackages = new JTable();
		tableActivePackages.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});
		tableActivePackages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableActivePackages.setAutoCreateRowSorter(true);
		
		tableModel = (DefaultTableModel) tableActivePackages.getModel();
		
		JScrollPane tableScrollPane = new JScrollPane(tableActivePackages);
		tableActivePackages.setFillsViewportHeight(true);
		
		add(tableScrollPane, "4, 4, 3, 1, fill, fill");
		
		chckbxActivePackagesOnly = new JCheckBox("Active Packages Only");
		add(chckbxActivePackagesOnly, "4, 6, left, default");
	}
	
	public void init() {
		chckbxActivePackagesOnly.setSelected(true);
		generateTable();
	}
	
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
		SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
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
		
		// set model for table
		tableModel.setDataVector(data, dataHeaders);
	}

	private String buildFilter() {
		String filter = "";
		if(chckbxActivePackagesOnly.isSelected()) {
			filter += "checked_in=true";
		}
		return filter;
	}

}
