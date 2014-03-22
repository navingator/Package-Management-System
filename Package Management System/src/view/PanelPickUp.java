package view;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class PanelPickUp extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8107721662424122816L;
	
	private JTextField textFieldPkgInput;

	/**
	 * Create the panel.
	 */
	public PanelPickUp() {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("min:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JLabel lblScanPkg = new JLabel("Please Scan a Package");
		lblScanPkg.setFont(new Font("Dialog", Font.BOLD, 24));
		add(lblScanPkg, "3, 4, center, default");
		
		textFieldPkgInput = new JTextField();
		textFieldPkgInput.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textFieldPkgInput.setHorizontalAlignment(SwingConstants.CENTER);
		add(textFieldPkgInput, "3, 8, fill, default");
		textFieldPkgInput.setColumns(10);
		
		JButton btnSubmit = new JButton("Submit");
		add(btnSubmit, "3, 10, center, default");
	}

}
