package view.panel;

import javax.swing.JTabbedPane;
import view.IViewToModelAdaptor;
import view.MainFrame;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class TabbedPaneAdmin extends JTabbedPane {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5846891962841200928L;
	
	final String panelEditPackagesName = "Packages";
	final String panelStudInfoName = "Student Information";
	final String panelEditEmailPrinterName = "Email and Printer";
	
	PanelEditPackages panelEditPackages;
	PanelStudentInformation panelStudInfo;
	PanelEditEmailPrinter panelEditEmailPrinter;
	
	public TabbedPaneAdmin(final MainFrame frame, final IViewToModelAdaptor modelAdaptor) {
		
		super(JTabbedPane.TOP);
		
		panelEditPackages = new PanelEditPackages(frame, modelAdaptor);
		addTab(panelEditPackagesName, null, panelEditPackages, null);
		panelStudInfo = new PanelStudentInformation(frame, modelAdaptor);
		addTab(panelStudInfoName, null, panelStudInfo, null);
		
		PanelEditEmailPrinter panelEditEmailPrinter = new PanelEditEmailPrinter(frame, modelAdaptor);
		addTab(panelEditEmailPrinterName, null, panelEditEmailPrinter, null);
				
		addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				initPanel();
			}
		});
	}
	
	private void initPanel() {
		switch(getTitleAt(getSelectedIndex())) {
		case panelEditPackagesName:
			panelEditPackages.init();
			break;
		case panelStudInfoName:
			panelStudInfo.init();
			break;
		}
	}
//	
	public void init() {
		panelEditPackages.init();
		panelStudInfo.init();
	}
}
