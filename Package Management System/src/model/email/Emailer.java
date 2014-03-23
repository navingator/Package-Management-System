package model.email;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Date;
import java.util.logging.Logger;

import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import model.IModelToViewAdaptor;
import util.Package;
import util.Pair;
import util.Person;
import util.PropertyHandler;

/*
 * Class that handles sending notification and reminder emails to students through
 * SMTP to the Gmail mail server.
 */

public class Emailer {
	
	private PropertyHandler propHandler;
	private String senderAddress;
	private String senderPassword;
	private String senderAlias;
	
	private String host;
	private Session session;
	private Transport transport;
	
	private Logger logger;
	private IModelToViewAdaptor viewAdaptor;
	
	public Emailer(IModelToViewAdaptor viewAdaptor) {
		// get PropertyHandler instance
		this.propHandler = PropertyHandler.getInstance();
		this.logger = Logger.getLogger(Emailer.class.getName());
		this.viewAdaptor = viewAdaptor;
	}
	
	//TODO finish function
	public void start() {
		// get properties from PropertyHandler
		this.senderAddress = propHandler.getProperty("email.email_address");
		this.senderPassword = propHandler.getProperty("email.password");
		this.senderAlias = propHandler.getProperty("email.alias");
		
		while(this.senderAddress == null || this.senderPassword == null || this.senderAlias == null) {
			logger.warning("Failed to load email properties.");
			viewAdaptor.displayMessage("Email information was not loaded from file.\n"
					+ "Please change email information in the next window", 
					"Email Not Loaded");			
			changeEmail();
		}
		
		// attempt to connect to the mail server and alert user if it fails
		attemptConnection();
	}
	
	/**
	 * Function changes email, password, and alias to passed values
	 * @param newAddress		New Email address
	 * @param newPassword		New Password to email address
	 * @param newAlias			New Alias for sender
	 */
	public void setEmailProperties(String newAddress, String newPassword, String newAlias) {

		propHandler.setProperty("email.email_address",newAddress);
		propHandler.setProperty("email.password",newPassword);
		propHandler.setProperty("email.alias",newAlias);
		
		this.senderAddress = newAddress;
		this.senderPassword = newPassword;
		this.senderAlias = newAlias;
		
	}

	//TODO
	public void sendAllReminders(ArrayList<Pair<Person,Package>> allEntriesSortedByPerson) {
		
		//collect ArrayList of pairs of person,ArrayList<Package>
		
		try {
			//TODO Change to attempt connection?
			connect(senderAddress,senderPassword);
			//iterate through ArrayList, sending emails if the person has packages
			closeConnection();
		} catch(NoSuchProviderException e) {
			//TODO
		} catch(MessagingException e) {
			//TODO
		}
	}

	/*
	 * Sends a notification email to the recipient informing them that they 
	 * have a new package 
	 */
	//TODO If notification sending fails, add to a list of emails to send
	//TODO Change input to Pair<Person,Package>
	public void sendPackageNotification(Person recipient, Package pkg) 
			throws UnsupportedEncodingException, MessagingException {
		
		String subject = "[Test][Package Notification] New Package for " + recipient.getFullName();
		String body = new String();
//		body += "Hello " + recipient.getFirstName() + ",\n\n"
//				+ "You have a new package in the Jones Mail Room.\n ";
		
		String comment = pkg.getComment();
		if(comment != null && !comment.isEmpty()) {
			body += "Comment: " + comment + "\n\n";
		}
		
//		body += "Please pick it up at your earliest convenience.\n \n" 
		body += "Jones Mail Room";
		sendEmail(recipient.getEmailAddress(), recipient.getFullName(), subject, body);
		
	}
	
	public String getSenderAddress() {
		return senderAddress;
	}

	public String getSenderAlias() {
		return senderAlias;
	}
	
	// connect to the mail server
	//TODO change class to reflect private variables
	private void connect(String userEmail, String password) 
			throws NoSuchProviderException, MessagingException {
		
		Properties props = System.getProperties();
        this.host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", userEmail);
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        session = Session.getDefaultInstance(props);
		transport = session.getTransport("smtp");
		transport.connect(host, senderAddress, password);
		
	}
	
	// send an email through the mail server
	private void sendEmail(String recipientEmail, String recipientAlias, String subject,
			String body) throws UnsupportedEncodingException, MessagingException {
		
        MimeMessage message = new MimeMessage(session);
        
        message.setFrom(new InternetAddress(senderAddress, senderAlias));
        message.addRecipient(Message.RecipientType.TO, 
        		new InternetAddress(recipientEmail, recipientAlias));
        
        message.setSubject(subject);
        message.setText(body);
        transport.sendMessage(message, message.getAllRecipients());
	}

	// close the connection to the mail server
	private void closeConnection() throws MessagingException {
        transport.close();
	}
	
	/*
	 * Sends a reminder email to the recipient reminding them of each package that
	 * is returned by recipient.getPackageList()
	 */
	private void sendPackageReminder(Person recipient, ArrayList<Package> packages) 
			throws UnsupportedEncodingException, MessagingException {
		
		String subject = "[Test][Package Reminder] You have " + packages.size() + " package";
		if (packages.size() != 1) {
			subject += 's';
		}
		subject += " in the mail room";
		String body = "Hello " + recipient.getFirstName() + ",\n\nYour packages include: ";
		
		for (int i=0; i<packages.size(); i++) {
			Package pkg = packages.get(i);
			body += "\n\tPackage " + (i+1) + ":";
			body += "\n\t\tCheck-In Date: " + pkg.getCheckInDate().toString();
			if(pkg.getComment() != "") {
				body += "\n\t\tComment: " + pkg.getComment();
			}
		}
		body += "\n\nPlease retrieve your packages as soon as possible.\n\n" + 
				"Jones Mail Room";
		sendEmail(recipient.getEmailAddress(), recipient.getFullName(), subject, body);
	}
	
	/**
	 * Function requests the view to get user input for new email information
	 */
	private void changeEmail() {
		String[] newEmail = viewAdaptor.changeEmail(senderAddress,senderPassword,senderAlias);
		if(newEmail != null) {
			setEmailProperties(newEmail[0],newEmail[1],newEmail[2]);
		}
	}
	
	/**
	 * Attempts to connect to the Gmail server with stored credentials
	 * Sends error messages to the view if an authentication error or a 
	 * general messaging error occurs.
	 */
	//TODO replace the connect() function?
	private void attemptConnection() {
		
		boolean retry = true;
		while(retry) {
			try {
				connect(senderAddress,senderPassword);
				closeConnection();
				retry = false;
			} catch (AuthenticationFailedException e){ 
					viewAdaptor.displayMessage("Incorrect username or password.\n","");
					changeEmail();
			} catch (MessagingException e) {
				logger.warning("Failed to connect to the mail server.");
				String[] options = {"Retry", "Cancel"};
				retry = viewAdaptor.getBooleanInput("Program failed to connect to the Gmail server.\n"
						+ "Please check your internet connection and try again.", 
						"Failed Connection",options);
				if(!retry) {
					viewAdaptor.displayMessage("Emails will not be sent until a connection is established.",
							"");
				}
			}
		}
		
	}
	
	public static void main(String[] args) {
		Emailer emailer = new Emailer(null);
		
		String senderEmail = "JonesCollegeMailRoom@gmail.com";
		String password = "jonesmailroomisbadass";
		
		Person navin = new Person("Pathak", "Navin", "np8@rice.edu","np8");
		//Person chris = new Person("Henderson", "Chris", "cwh1@rice.edu");
		//Person michelle = new Person("Bennack", "Michelle", "mrb4@rice.edu");
		//Person ambi = new Person("Bobmanuel","Ambila","ajb6@rice.edu");
		
		Date now = new Date();
		Package p1 = new Package(123,"",now);
		Package p2 = new Package(234,"It's huge. Get it out now.",new Date(now.getTime()-100000000));
		Package p3 = new Package(309435,"",new Date(now.getTime()-200000000));
		
		ArrayList<Package> navinPkgs = new ArrayList<Package>();
		navinPkgs.add(p2);
		navinPkgs.add(p3);
		
		try {
			emailer.connect(senderEmail, password);
			emailer.sendPackageNotification(navin,p1);
			emailer.sendPackageNotification(navin,p2);
			//Emailer.sendPackageReminder(navin, new Package[]{p1});
			//Emailer.sendPackageReminder(navin, new Package[]{p2});
			//Emailer.sendPackageReminder(navin, new Package[]{p1,p2});
			//Emailer.sendPackageReminder(navin, new Package[]{p1,p2,p3});
			emailer.sendPackageReminder(navin,navinPkgs);
			//Emailer.sendPackageReminder(navin, new Package[]{});
			emailer.closeConnection();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
	}

}
