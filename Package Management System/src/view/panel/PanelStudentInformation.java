package view.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;

import view.IViewToModelAdaptor;
import view.MainFrame;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class PanelStudentInformation extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3066818563731811034L;
	private JTable tableStudentInfo;

	public PanelStudentInformation(final MainFrame frame, final IViewToModelAdaptor modelAdaptor) {
		setLayout(new FormLayout(new ColumnSpec[] {
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
		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
						return "CSV";
					}
				});
				
				
				
				//Open Dialog
				int returnVal = fc.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File csvFile = fc.getSelectedFile();
		            modelAdaptor.importPersonCSV(csvFile.getAbsolutePath());
		        }
			}
		});
		add(btnImport, "1, 1, left, bottom");
		btnImport.setToolTipText("Import student information from a csv file. Format: LastName,FirstName,NetID");
		
		JButton btnAddStudent = new JButton("Add");
		add(btnAddStudent, "2, 1");
		btnAddStudent.setToolTipText("Save changes to student information.");
		
		tableStudentInfo = new JTable();
		tableStudentInfo.setCellSelectionEnabled(true);
		add(tableStudentInfo, "1, 2, 2, 7, fill, fill");
		
		JButton btnEditStudent = new JButton("Edit");
		btnEditStudent.setToolTipText("Edit selected student");
		add(btnEditStudent, "3, 4");
		
		JButton btnDeleteStudent = new JButton("Delete");
		btnDeleteStudent.setToolTipText("Delete selected student");
		add(btnDeleteStudent, "3, 6");
	}
}
