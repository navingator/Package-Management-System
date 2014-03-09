package util;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;

public class JSONFileIO {
	
	//TODO rewrite with map
	public static void writeDatabaseJSONFile(Person person, String fileName) throws IOException {
		
		// initialize gson object
		Gson gson = new Gson();
		
		// open file
		FileOutputStream outfile = new FileOutputStream(fileName);
		DataOutputStream outStream = new DataOutputStream(outfile);
		
		String json = gson.toJson(person); // serialize output
		outStream.writeUTF(json); // write output to file
		
		outfile.close();
		
	}
	
	public static Person readDatabaseJSONFile(String fileName) throws IOException {
		
		// initialize gson object
		Gson gson = new Gson();

		// open file to read
		FileInputStream infile = new FileInputStream(fileName);
		DataInputStream inStream = new DataInputStream(infile);
		
		// read database file
		String json = inStream.readUTF();
		
		inStream.close();
		
		return gson.fromJson(json, Person.class); // return deserialized output
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
		
		Person navin = new Person("Pathak", "Navin", "np8@rice.edu");
		Person chris = new Person("Henderson", "Chris", "cwh1@rice.edu");
		
		try {
			// test writing to JSON file
			String fileName = "F:/navin/Documents/Eclipse/workspace/YGP/testFiles/test.txt";
			writeDatabaseJSONFile(navin,fileName);
			
			// test reading from JSON file
			Person testPerson = readDatabaseJSONFile(fileName);  
			
			System.out.println(testPerson.getEmailAddress());
			System.out.println(PPMap.getPackages(testPerson).toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
