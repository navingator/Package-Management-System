package view.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import view.IViewToModelAdaptor;
import view.MainFrame;
import view.dialog.ChangeEmail;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class TabbedPaneAdmin extends JTabbedPane {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5846891962841200928L;
	public TabbedPaneAdmin(final MainFrame frame, final IViewToModelAdaptor modelAdaptor) {
		
		super(JTabbedPane.TOP);
		
		PanelEditPackages panelEditPackages = new PanelEditPackages(frame, modelAdaptor);
		addTab("Packages", null, panelEditPackages, null);
		
		PanelStudentInformation panelStudInfo = new PanelStudentInformation(frame, modelAdaptor);
		addTab("Student Information", null, panelStudInfo, null);
		
		PanelEditEmailPrinter panelEditEmailPrinter = new PanelEditEmailPrinter(frame, modelAdaptor);
		addTab("Email and Printer", null, panelEditEmailPrinter, null);
	}
}