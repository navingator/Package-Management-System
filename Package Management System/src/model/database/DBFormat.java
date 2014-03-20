package model.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import util.Pair;
import util.Person;
import util.Package;

public class DBFormat {
	
	/*
	 * Filters
	 */
	
	/**
	 * Filter function that calls all of the other filter functions
	 * 
	 * filterString should be written with all filters in the format
	 * 		field1=value1:field2=value2:field3=value3
	 * etc., where fields and values are defined as follows
	 * 	Fields with accompanying values:
	 * 		checked_in			
	 * 			<boolean>		Include only packages that have not been checked out
	 * 		person_name
	 * 			<String>		User input string to search Person firstName and LastName		
	 * 		on_date
	 * 			<String>		YYYYMMDD Date to get entries checked-in on
	 *		before_date			
	 * 			<String>		YYYYMMDD Date to get entries checked-in before
	 * 		after_date
	 * 			<String>		YYYYMMDD Date get entries checked-in after
	 * 			
	 * 		
	 * 
	 * @param dbEntries			Database ArrayList<Pair<Person,Package>> entries
	 * @param filterString		String containing filters to be applied, in order
	 * @return					Filtered ArrayList<Pair<Person,Package>> entries
	 */
	public static void filter(
			ArrayList<Pair<Person,Package>> dbEntries,
			String filterString) {
		
		//Do nothing if filterString is empty
		if(filterString == null || filterString.isEmpty()) {
			return;
		}
		
		// parse the string for options
		ArrayList<Pair<String,String>> filtOpts = parseLang(filterString);
		for (Pair<String,String> opt: filtOpts) {
			switch (opt.first.toUpperCase()) {
			case "CHECKED_IN":
				filterCheckedIn(dbEntries,Boolean.valueOf(opt.second));
				break;
			case "PERSON_NAME":
				filterPersonName(dbEntries,opt.second);
				break;
			case "BEFORE_DATE":
				filterDate(dbEntries,opt.second,"before");
				break;
			case "ON_DATE":
				filterDate(dbEntries,opt.second,"on");
				break;
			case "AFTER_DATE":
				filterDate(dbEntries,opt.second,"after");
				break;
			}
		}
	}
	
	/**
	 * Function that returns all packages that have not been checked out
	 * @param dbEntries			Database ArrayList<Pair<Person,Package>> entries
	 * @return					Checked in ArrayList<Pair<Person,Package>> entries
	 */
	private static void filterCheckedIn(
			ArrayList<Pair<Person,Package>> dbEntries,
			boolean doFilter) {
		
		if(doFilter) {
			Iterator<Pair<Person, Package>> entryIt = dbEntries.iterator();
			
			// Iterate through entries, removing them if they have been checked out
			while(entryIt.hasNext()) {
				Pair<Person,Package> entry = entryIt.next();
				if(entry.second.getCheckOutDate() != null) {
					entryIt.remove();
				}
			}
		}
	}
	
	/**
	 * Function that returns all packages owned by persons matching searchString
	 * @param dbEntries			Database ArrayList<Pair<Person,Package>> entries
	 * @param personIDs			Array of personIDs to search for
	 * @return					Owned ArrayList<Pair<Person,Package>> entries
	 */
	private static void filterPersonName(
			ArrayList<Pair<Person,Package>> dbEntries,
			String searchString) {
		
		// make the string lower case for case insensitivity
		searchString = searchString.toLowerCase();
		
		// Iterate through all entries and remove non-matching entries
		Iterator<Pair<Person,Package>> entryIt = dbEntries.iterator();
		while(entryIt.hasNext()) {
			Pair<Person,Package> entry = entryIt.next();
			String fullName = entry.first.getFullName().toLowerCase();
			String lastFirst = entry.first.getLastFirstName().toLowerCase();
			
			// remove if neither name contains the search string
			if (!(fullName.contains(searchString) || lastFirst.contains(searchString))) {
				entryIt.remove();
			}
		}
		
		
		// check forward and reverse
	}
	
	/**
	 * Function that returns all packages with specified check-in dates
	 * @param dbEntries			Database ArrayList<Pair<Person,Package>> entries
	 * @param date				(YYYYMMDD) date to search around
	 * @param predicate			"on," "before," or "after" specific date
	 * @return					Matching ArrayList<Pair<Person,Package>> entries
	 */
	
	private static void filterDate(
			ArrayList<Pair<Person,Package>> dbEntries,
			String date, 
			String predicate) {
		
		// Iterate through entries and remove if in the wrong category
		int setCalendarDate = Integer.valueOf(date);
		Iterator<Pair<Person,Package>> entryIt = dbEntries.iterator();
		SimpleDateFormat ft = new SimpleDateFormat("YYYYMMdd");
		while(entryIt.hasNext()) {
			Pair<Person,Package> entry = entryIt.next();
			int checkInCalendarDate = Integer.valueOf(ft.format(entry.second.getCheckInDate()));
			switch (predicate.toLowerCase()) {
			case "on":
				if(checkInCalendarDate != setCalendarDate) {entryIt.remove();}
				break;
			case "before":
				if(checkInCalendarDate >= setCalendarDate) {entryIt.remove();}
				break;
			case "after":
				if(checkInCalendarDate <= setCalendarDate) {entryIt.remove();}
				break;
			}
		}
		
	}
	
	/**
	 * Sort function that calls all of the other sort functions
	 * 
	 * sortString should be written with highest priority sorts first
	 * in the format
	 * 		field1=value1:field2=value2:field3=value3
	 * 
	 * Where fields and values are defined as follows
	 * 	Fields:
	 * 		last_name			Person last name
	 * 		first_name			Person first name
	 * 		person_ID			Person personID
	 * 		package_ID			Package packageID
	 * 		check_in_date		Package check in date
	 * 		check_out_date		Package check out date
	 * 	Values:
	 * 		ASCENDING
	 * 		DESCENDING
	 * 
	 * @param dbEntries			Database ArrayList<Pair<Person,Package>> entries
	 * @param sortString		String containing sort to be applied in priority order
	 * @return					Sorted ArrayList<Pair<Person,Package>> entries
	 */
	public static void sort(
			ArrayList<Pair<Person,Package>> dbEntries,
			String sortString) {
		
		// Do nothing if the string is empty
		if (sortString == null || sortString.isEmpty()) {
			return;
		}
		
		// Parse options
		ArrayList<Pair<String,String>> options = parseLang(sortString);
		
		// Make an ArrayList of sort options
		ArrayList<Pair<DBComparator,SortType>> sortOpts = new ArrayList<Pair<DBComparator,SortType>>();
		for (Pair<String,String> opt : options) {
			sortOpts.add(new Pair<DBComparator,SortType>(
					DBComparator.valueOf(opt.first.toUpperCase()),
					SortType.valueOf(opt.second.toUpperCase())));
		}
		
		Collections.sort(dbEntries,DBComparator.getComparator(sortOpts));
		
	}
	
	private enum SortType {
		ASCENDING,DESCENDING;
	}
	
	private enum DBComparator implements Comparator<Pair<Person,Package>> {
		LAST_NAME {
			public int compare(Pair<Person,Package> entry1, 
					Pair<Person,Package> entry2) {
				return entry1.first.getLastName().toLowerCase().compareTo(
						entry2.first.getLastName().toLowerCase());
			}
		},
		FIRST_NAME {
			public int compare(Pair<Person,Package> entry1, 
					Pair<Person,Package> entry2) {
				return entry1.first.getFirstName().toLowerCase().compareTo(
						entry2.first.getFirstName().toLowerCase());
			}
		},
		PERSON_ID {
			public int compare(Pair<Person,Package> entry1, 
					Pair<Person,Package> entry2) {
				return entry1.first.getPersonID().toLowerCase().compareTo(
						entry2.first.getPersonID().toLowerCase());
			}
		},
		PACKAGE_ID {
			public int compare(Pair<Person,Package> entry1, 
					Pair<Person,Package> entry2) {
				return Long.valueOf(entry1.second.getPackageID()).compareTo(
						Long.valueOf(entry2.second.getPackageID()));
			}
		},
		CHECK_IN_DATE {
			public int compare(Pair<Person,Package> entry1,
					Pair<Person,Package> entry2) {
				return entry1.second.getCheckInDate().compareTo(
						entry2.second.getCheckInDate());
			}
		},
		CHECK_OUT_DATE {
			public int compare(Pair<Person,Package> entry1,
					Pair<Person,Package> entry2) {
				//special cases for null
				Date e1CODate = entry1.second.getCheckOutDate();
				Date e2CODate = entry2.second.getCheckOutDate();
				if(e1CODate == null && e2CODate == null) {return 0;} 
				else if(e1CODate == null) {return 1;} 
				else if(e2CODate == null) {return -1;}
				else {return e1CODate.compareTo(e2CODate);}
			}
		};
		
		public static Comparator<Pair<Person,Package>> getComparator(
				final ArrayList<Pair<DBComparator,SortType>> options) {
			
			return new Comparator<Pair<Person,Package>>() {
				public int compare(Pair<Person,Package> entry1,
						Pair<Person,Package> entry2) {
					
					for(Pair<DBComparator,SortType> opt: options) {
						int result;
						if(opt.second == SortType.ASCENDING) {
							result = opt.first.compare(entry1, entry2);
						} else {
							result = -opt.first.compare(entry1, entry2);
						}
						if (result != 0) {
							return result;
						}
					}
					return 0;
				}
			};
		}
		
	}
	
	/**
	 * Parser for the simple filter and sorter language described in each public function
	 * @param input			The input string to be parsed
	 * @return
	 */
	private static ArrayList<Pair<String,String>> parseLang(String input) {
		String[] fieldsAndValues = input.split(":");
		ArrayList<Pair<String,String>> result = new ArrayList<Pair<String,String>>();
		
		for (String fvString: fieldsAndValues) {
			String[] fv = fvString.split("=", 2);
			result.add(new Pair<String,String>(fv[0],fv[1]));
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		
		Date now = new Date();
		
		Package p1 = new Package(123+now.getTime(),"",now);
		Package p2 = new Package(234+now.getTime(),"It's huge. Get it out now.",new Date(now.getTime()-100000000));
		Package p3 = new Package(309435+now.getTime(),"",new Date(now.getTime()-200000000));
		Package p4 = new Package(1+now.getTime(),"",new Date(now.getTime()-300000000));
		Package p5 = new Package(2+now.getTime(),"",new Date(now.getTime()-400000000));
		//p4.setCheckOutDate(now);
		p5.setCheckOutDate(new Date(now.getTime()-200000000));

		Person navin = new Person("Pathak", "Navin", "np8@rice.edu", "np8");
		Person chris = new Person("Henderson", "Chris", "cwh1@rice.edu", "cwh1");
		Person christopher = new Person("Henderson", "Christopher", "cwh2@rice.edu", "cwh2");
		Person ambi = new Person("Bobmanuel", "Lambi", "ajb6@rice.edu", "ajb6");
		
		ArrayList<Pair<Person,Package>> dbEntries = new ArrayList<Pair<Person,Package>>();
		dbEntries.add(new Pair<Person,Package>(navin,p1));
		dbEntries.add(new Pair<Person,Package>(chris,p4));
		dbEntries.add(new Pair<Person,Package>(christopher,p3));
		dbEntries.add(new Pair<Person,Package>(chris,p5));
		dbEntries.add(new Pair<Person,Package>(ambi,p2));
		
		String sortString = "check_out_date=DESCENDING:person_ID=ASCENDING:first_name=ASCENDING";
		sort(dbEntries,sortString);
		
		String filterString = "person_name=h";
		filter(dbEntries,filterString);
		
		for(Pair<Person,Package> entry: dbEntries) {
			System.out.println(entry.first.getFullName() + ' ' +
					entry.first.getPersonID() + ' ' +
					entry.second.getCheckInDate() + ' ' + 
					entry.second.getCheckOutDate() + ' ' + 
					entry.second.getPackageID());
		}
	}
	
}
