package view.dialog;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ConfirmPickUp extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3164801383524435324L;
	private final JPanel contentPanel = new JPanel();
	boolean result;

	/**
	 * Create the dialog.
	 */
	public ConfirmPickUp(JFrame frame, String personName, String personID) {
		super(frame,true);
		setTitle("Confirm Pick Up");
		setSize(450,300);
		setLocationRelativeTo(frame);
		
		this.result = false;
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("23px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		{
			JLabel lblStudent = new JLabel(personName + " (" + personID + ")");
			lblStudent.setForeground(new Color(0, 153, 0));
			lblStudent.setFont(new Font("Tahoma", Font.BOLD, 22));
			contentPanel.add(lblStudent, "2, 4, 3, 1, center, default");
		}
		{
			JLabel lblConfirmName = new JLabel("Please confirm that you are the person above.");
			contentPanel.add(lblConfirmName, "2, 6, 3, 1, center, default");
		}
		{
			JButton confirmButton = new JButton("Confirm");
			confirmButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					result = true;
					setVisible(false);
					dispose();
				}
			});
			contentPanel.add(confirmButton, "2, 8, left, top");
			confirmButton.setActionCommand("OK");
			getRootPane().setDefaultButton(confirmButton);
		}
		{
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					result = false;
					setVisible(false);
					dispose();
				}
			});
			contentPanel.add(cancelButton, "4, 8, left, top");
			cancelButton.setActionCommand("Cancel");
		}
	}
	
	public boolean showDialog() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
		return result;
	}

}
