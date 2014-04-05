package view.panel;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import view.IViewToModelAdaptor;
import view.MainFrame;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class PanelEditPackages extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2711122506103907410L;
	private JTextField studentSearchBox;
	private JTable tableActivePackages;

	public PanelEditPackages(MainFrame frame, IViewToModelAdaptor modelAdaptor) {
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
		
		studentSearchBox = new JTextField();
		add(studentSearchBox, "4, 2");
		studentSearchBox.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		add(btnSearch, "6, 2, left, default");
		
		tableActivePackages = new JTable();
		tableActivePackages.setCellSelectionEnabled(true);
		add(tableActivePackages, "4, 4, 3, 1, fill, fill");
		
		JCheckBox chckbxActivePackagesOnly = new JCheckBox("Active Packages Only");
		chckbxActivePackagesOnly.setSelected(true);
		add(chckbxActivePackagesOnly, "4, 6, left, default");
	}

}
