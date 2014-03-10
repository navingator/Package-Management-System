package util;

import java.util.ArrayList;

/*
 * Package class contains variables necessary to define a package
 */

public class Person {
	private String lastName;
	private String firstName;
	private String emailAddress;
	private String netID;
	
	public Person(String lastName, String firstName, String emailAddress) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.emailAddress = emailAddress;
	}
	
	/*
	 * Getters:
	 * 	fullName
	 * 	lastName
	 * 	firstName
	 * 	emailAddress
	 * 	netID
	 */
	public String getFullName() {
		return firstName + ' ' + lastName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getEmailAddress(){
		return emailAddress;
	}
	public String getNetID() {
		return netID;
	}

	/*
	 * Setters:
	 *  lastName
	 *  firstName
	 *  emailAddress
	 */

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setEmailAddress(String email) {
		this.emailAddress = email;
	}
	

	
}
