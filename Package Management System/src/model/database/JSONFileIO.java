package model.database;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import util.Package;
import util.Pair;
import util.Person;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class JSONFileIO {
	public void writeDatabaseJSONFile(Pair<Person,ArrayList<Package>> DBPair, String fileName) 
			throws IOException {
		
		// initialize gson object
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		// open file
		FileOutputStream outfile = new FileOutputStream(fileName);
		DataOutputStream outStream = new DataOutputStream(outfile);
		
		String json = gson.toJson(DBPair); // serialize output
		outStream.writeUTF(json); // write output to file
		
		outfile.close();
		
	}
	
	public Pair<Person,ArrayList<Package>> readDatabaseJSONFile(String fileName) throws IOException {
		
		// initialize gson object
		Gson gson = new Gson();

		// open file to read
		FileInputStream infile = new FileInputStream(fileName);
		DataInputStream inStream = new DataInputStream(infile);
		
		// read model.database file
		String json = inStream.readUTF();
		
		inStream.close();
		
		// Obtain the type of the deserialized output (see gson documentation)
		Type PairType = new TypeToken<Pair<Person,ArrayList<Package>>>(){}.getType();
		return gson.fromJson(json, PairType); // return deserialized output
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

		JSONFileIO json = new JSONFileIO();
		try {
			// test writing to JSON file
			String fileName1 = "testFiles/" + navin.getNetID() + ".txt";
			String fileName2 = "testFiles/" + chris.getNetID() + ".txt";
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
