package view.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

import view.IViewToModelAdaptor;
import view.MainFrame;
import view.dialog.ChangeEmail;
import view.dialog.EditTemplate;
import view.dialog.EditTemplateEasy;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class PanelEditEmailPrinter extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1347840240775088592L;

	public PanelEditEmailPrinter(final MainFrame frame, final IViewToModelAdaptor modelAdaptor) {
		setLayout(new FormLayout(new ColumnSpec[] {
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
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JButton btnChangeEmail = new JButton("Change Email Account");
		btnChangeEmail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
							
				// open the change email dialog
				String oldEmail = modelAdaptor.getEmailAddress();
				String oldAlias = modelAdaptor.getEmailAlias();
				
				ChangeEmail emailDlg = new ChangeEmail(frame,oldAlias,oldEmail);
				String newEmail[] = emailDlg.showDialog();
				
				// make the changes in the model
				if(newEmail != null) {
					modelAdaptor.changeEmail(newEmail[0], newEmail[1], newEmail[2]);
				}
				
			}
		});
		add(btnChangeEmail, "4, 4, default, fill");
		
		JButton btnChangeEmailTemplate = new JButton("Change Email Template");
		btnChangeEmailTemplate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		
				// open the change email dialog				
				Map<String,String> templateMap = modelAdaptor.getEmailTemplates(false, true);
				
				EditTemplateEasy emailDlg = new EditTemplateEasy(frame, templateMap);
				Map<String, String> newTemplate = emailDlg.showDialog();
				
				// make the changes in the model
				if (newTemplate != null) {
					modelAdaptor.changeEmailTemplate(newTemplate);
				} 
				
			}
		});
		add(btnChangeEmailTemplate, "4, 6, default, fill");	
			
		JButton btnSelectPrinter = new JButton("Select Printer");
		btnSelectPrinter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String printerName = frame.getPrinterName(modelAdaptor.getPrinterNames());
				modelAdaptor.setPrinter(printerName);
			}
		});
		add(btnSelectPrinter, "4, 8, default, fill");	
	}
}
