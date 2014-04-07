package view.panel;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import util.Person;
import util.Package;
import view.IViewToModelAdaptor;
import view.dialog.ConfirmPickUp;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PanelPickUp extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8107721662424122816L;
	
	private JTextField textFieldPkgInput;
	
	IViewToModelAdaptor modelAdaptor;
	JFrame frame;

	/**
	 * Create the panel.
	 */
	public PanelPickUp(final JFrame frame, final IViewToModelAdaptor modelAdaptor) {
		frame.addWindowFocusListener(new WindowAdapter() {
		    public void windowGainedFocus(WindowEvent e) {
		        textFieldPkgInput.requestFocusInWindow();
		    }
		});
		
		this.modelAdaptor = modelAdaptor;
		this.frame = frame;
		
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
		textFieldPkgInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirmAndCheckOut();
				textFieldPkgInput.setText("");
				textFieldPkgInput.requestFocus();
			}
		});
		textFieldPkgInput.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textFieldPkgInput.setHorizontalAlignment(SwingConstants.CENTER);
		add(textFieldPkgInput, "3, 8, fill, default");
		textFieldPkgInput.setColumns(10);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				confirmAndCheckOut();
				textFieldPkgInput.setText("");
				textFieldPkgInput.requestFocus();
			}
		});
		add(btnSubmit, "3, 10, center, default");
		
	}
	
	private void confirmAndCheckOut() {
		
		String input = textFieldPkgInput.getText().replaceAll("[^0-9]", "");
		long pkgID = Long.valueOf(input);
		// Get person information and get response from dialog for confirmation
		Package pkg = modelAdaptor.getPackage(pkgID);
		Person owner = modelAdaptor.getPackageOwner(pkgID);
		
		// If the package doesn't exist, warn the user
		if(pkg == null) {
			JOptionPane.showMessageDialog(frame, "The package was not found.",
					"Package Not Found", JOptionPane.DEFAULT_OPTION);
			return;
		}
		
		// If the package is already checked out, warn the user
		if(pkg.getCheckOutDate() != null) {
			JOptionPane.showMessageDialog(frame, "The package is already checked out.",
					"Checked Out", JOptionPane.DEFAULT_OPTION);
			return;
		}
		
		// Get confirmation that the package is for the user
		ConfirmPickUp confirmPickUpDlg = new ConfirmPickUp(frame,
				owner.getFullName(),owner.getPersonID());
		
		if(confirmPickUpDlg.showDialog()) {
			if (modelAdaptor.checkOutPackage(pkgID)) {
				JOptionPane.showMessageDialog(frame, "The package was successfully checked out.",
						"Success", JOptionPane.DEFAULT_OPTION);
				
				//TODO
//				if(owner.getPersonID() == "sg35" ||
//						owner.getPersonID() == "bct2" ||
//						owner.getPersonID() == "mjt5") {
//					thankYouComeAgain();
//				}
			}
		} else {
			JOptionPane.showMessageDialog(frame, "The package was not checked out.",
					"Not Checked Out", JOptionPane.DEFAULT_OPTION);
		}
	}

//	private synchronized void thankYouComeAgain() {
//		new Thread(new Runnable() {
//			  // The wrapper thread is unnecessary, unless it blocks on the
//			  // Clip finishing; see comments.
//			    public void run() {
//			      try {
//			        Clip clip = AudioSystem.getClip();
//			        AudioInputStream inputStream = AudioSystem.getAudioInputStream(
//			          new File("audio/thank_you_come_again.mp3"));
//			        clip.open(inputStream);
//			        clip.start(); 
//			      } catch (Exception e) {
//			        System.err.println(e.getMessage());
//			      }
//			    }
//		  }).start();
//	}

}
