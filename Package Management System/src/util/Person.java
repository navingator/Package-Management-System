package util;

/*
 * Package class contains variables necessary to define a package
 */

public class Person {
	private String lastName;
	private String firstName;
	private String emailAddress;
	private final String personID;
	
	public Person(String lastName, String firstName, String emailAddress, String personID) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.emailAddress = emailAddress;
		this.personID = personID;
	}
	
	/*
	 * Getters:
	 * 	LastFirstName
	 * 	fullName
	 * 	lastName
	 * 	firstName
	 * 	emailAddress
	 * 	personID
	 */
	
	/**
	 * Function returns lastName, firstName
	 * @return				
	 */
	public String getLastFirstName() {
		return lastName + ", " + firstName;
	}
	
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
	public String getPersonID() {
		return personID;
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

	/**
	 * Attempts to generate an email address in the rice domain
	 * @param netID			Rice netID		
	 * @return				Rice Email address
	 */
	public static String generateEmail(String netID) {
		return netID + "@rice.edu";
	}

}
