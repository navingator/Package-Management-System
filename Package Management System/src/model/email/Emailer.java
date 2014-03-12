package model.email;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import util.Package;
import util.Person;

/*
 * Class that handles sending notification and reminder emails to students through
 * SMTP to the Gmail mail server.
 */

public class Emailer {
	
	private Session session;
	private Transport transport;
	private String senderEmail;
	
	private static final String senderAlias = "Jones Mail Room";
	
	
	// connect to the mail server
	public void connect(String userEmail, String password) 
			throws NoSuchProviderException, MessagingException {

		senderEmail = userEmail;
		Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", senderEmail);
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        session = Session.getDefaultInstance(props);
        
		transport = session.getTransport("smtp");
		transport.connect(host, senderEmail, password);
		
	}
	
	// send an email through the mail server
	private void sendEmail(String recipientEmail, String recipientAlias, String subject,
			String body) throws UnsupportedEncodingException, MessagingException {
		
        MimeMessage message = new MimeMessage(session);
        
        message.setFrom(new InternetAddress(senderEmail, senderAlias));
        message.addRecipient(Message.RecipientType.TO, 
        		new InternetAddress(recipientEmail, recipientAlias));
        
        message.setSubject(subject);
        message.setText(body);
        transport.sendMessage(message, message.getAllRecipients());
	}

	// close the connection to the mail server
	public void closeConnection() throws MessagingException {
        transport.close();
	}

	/*
	 * Sends a notification email to the recipient informing them that they 
	 * have a new package 
	 */
	public void sendPackageNotification(Person recipient) 
			throws UnsupportedEncodingException, MessagingException {
		
		String subject = "[Test][Package Notification] New Package for " + recipient.getFullName();
		String body = "Hello " + recipient.getFirstName() + ",\n\n"
				+ "You have a new package in the Jones Mail Room. "
				+ "Please pick it up at your earliest convenience.\n\n"
				+ "Jones Mail Room";
		sendEmail(recipient.getEmailAddress(), recipient.getFullName(), subject, body);
		
	}
	
	/*
	 * Sends a reminder email to the recipient reminding them of each package that
	 * is returned by recipient.getPackageList()
	 */
	public void sendPackageReminder(Person recipient, ArrayList<Package> packages) 
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
	
	public void main(String[] args) {
		Emailer emailer = new Emailer();
		
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
			emailer.sendPackageNotification(navin);
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

	public void start() {
		// TODO Auto-generated method stub
		
	}

}