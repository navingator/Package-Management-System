package model.database;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Logger;

import util.Package;
import util.Pair;
import util.Person;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
/**
 * Class contains functions for writing database entries to file and 
 * pulling person objects from a csv file.
 */
public class DBFileIO {
	
	private Logger logger; 
	
	public DBFileIO() {
		this.logger = Logger.getLogger(DBFileIO.class.getName());
	}
	
	/**
	 * Function that will write a pair containing a person object and all associated packages
	 * to the specified JSON file. 
	 * 
	 * @param DBPair			Pair containing a person and ArrayList of all packages associated
	 * @param filePath			Path to the file to be written
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void writeDatabaseJSONFile(Pair<Person,ArrayList<Package>> DBPair, String filePath) 
			throws IOException,FileNotFoundException {
		
		// initialize gson object
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		// open file
		FileOutputStream outfile = new FileOutputStream(filePath);
		DataOutputStream outStream = new DataOutputStream(outfile);
		
		String json = gson.toJson(DBPair); // serialize output
		outStream.writeUTF(json); // write output to file
		
		outfile.close();
		
	}
	/**
	 * Function that will read a specified JSON file and return the person and packages
	 * contained within
	 * 
	 * @param filePath			Path to the file with the person information
	 * @return					Returns a pair of person and packages in arrayList object
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public Pair<Person,ArrayList<Package>> readDatabaseJSONFile(String filePath) 
			throws IOException,FileNotFoundException {
		
		// initialize gson object
		Gson gson = new Gson();

		// open file to read
		FileInputStream infile = new FileInputStream(filePath);
		DataInputStream inStream = new DataInputStream(infile);
		
		// read model.database file
		String json = inStream.readUTF();
		
		inStream.close();
		
		// Obtain the type of the deserialized output (see gson documentation)
		Type PairType = new TypeToken<Pair<Person,ArrayList<Package>>>(){}.getType();
		return gson.fromJson(json, PairType); // return deserialized output
	}
	
	/**
	 * Reads a CSV file and sends the list of people within the file to the database
	 * Everyone must be provided a personID, although other fields are optional.
	 * 
	 * CSV file format: LastName,FirstName,EmailAddress,personID
	 * 	LastName - Person's first name
	 * 	FirstName - Person's last name
	 *  EmailAddress - Email address of the person
	 *  	If left empty, will attempt to provide an email address from 
	 *  	a provided personID only
	 *  PersonID - Person's unique identifier - MUST BE SPECIFIED
	 * 
	 * @param filePath			Name of the CSV file to read from
	 * @param failed			ArrayList containing a pair with name of 
	 * 							the persons that failed to be read and reason
	 * @return					List of all of the person objects in the file
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws FileFormatException 
	 */
	public ArrayList<Person> readDatabaseCSVFile(String filePath, ArrayList<Pair<String,String>> failed) 
			throws IOException, FileNotFoundException, FileFormatException{
		
		ArrayList<Person> personList = new ArrayList<Person>();
		
		BufferedReader br = null;
		String line = "";
		String delimiter = ",";
		
		try {
			 
			br = new BufferedReader(new FileReader(filePath));
			HashSet<String> personIDSet = new HashSet<String> ();
			
			//handle header
			line = br.readLine();
			String[] header = line.split(delimiter,4);
			if (!(header[0].toLowerCase().contains("last")) ||
					!(header[1].toLowerCase().contains("first")) ||
					!(header[2].toLowerCase().contains("mail")) ||
					!(header[3].toLowerCase().contains("id"))) {
				throw new FileFormatException("File must be .csv with appropriate header: \n"
						+ "Last Name,First Name,Email Address,ID");
			}
			
			// loop through the file
			while ((line = br.readLine()) != null) {
				
				// ignore empty lines
				if(line == "") {
					continue;
				}
				
				// split up the line
				String[] personElements = line.split(delimiter,4);
				String lastName = personElements[0];
				String firstName = personElements[1];
				String emailAddress = personElements[2];
				String personID = personElements[3];
				
				// handle special case
				if(personID.equals("")) {
					failed.add(new Pair<String,String>(
							firstName + ' ' + lastName, "ID not provided"));
					continue;
				} else if(personIDSet.contains(personID)) { 
					failed.add(new Pair<String,String>(
							firstName + ' ' + lastName, "Duplicate ID"));
					continue;
				}
				else if(emailAddress.equals("")) {
					emailAddress = Person.generateEmail(personID);
					System.out.println("Email automatically generated for "
							+ firstName + ' ' + lastName);
				}
				
				Person person = new Person(lastName,firstName,emailAddress,personID);
				personList.add(person);
				personIDSet.add(personID);
	 
			}
	 
		} finally {
			if(br != null) {
				br.close();
			}
		}
	 
		logger.info(filePath + " was successfully read");
		return personList;
	}
	
	public static void main(String[] args) {
		Date now = new Date();
		Package p1 = new Package(123,"",now);
		Package p2 = new Package(234,"It's huge. Get it out now.",new Date(now.getTime()-100000000));
		Package p3 = new Package(309435,"",new Date(now.getTime()-200000000));

		ArrayList<Package> packages = new ArrayList<Package>();
		packages.add(p1);
		packages.add(p2);
		packages.add(p3);

		Person navin = new Person("Pathak", "Navin", "np8@rice.edu", "np8");
		Person chris = new Person("Henderson", "Chris", "cwh1@rice.edu", "cwh1");

		DBFileIO json = new DBFileIO();
		try {
			// test writing to JSON file
			String fileName1 = "testFiles/" + navin.getPersonID() + ".txt";
			String fileName2 = "testFiles/" + chris.getPersonID() + ".txt";
			json.writeDatabaseJSONFile(new Pair<Person,ArrayList<Package>>(navin,packages),fileName1);
			json.writeDatabaseJSONFile(
					new Pair<Person,ArrayList<Package>>(chris,new ArrayList<Package>()),fileName2);

			// test reading from JSON file
			Pair<Person,ArrayList<Package>> testPair1 = json.readDatabaseJSONFile(fileName1);
			Pair<Person,ArrayList<Package>> testPair2 = json.readDatabaseJSONFile(fileName2);
			
			System.out.println(testPair1.first.getEmailAddress());
			System.out.println(testPair1.second.toString());
			System.out.println(testPair2.first.getEmailAddress());
			System.out.println(testPair2.second.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
