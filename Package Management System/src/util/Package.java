package util;
import java.util.Date;

/*
 * Package class contains variables necessary to define a package
 */

public class Package {
	private final long packageID;
	private String comment;
	private final Date checkInDate;
	private Date checkOutDate;
	
	
	public Package(long packageID, String comment, Date checkInDate) {
		this.packageID = packageID;
		this.comment = comment;
		this.checkInDate = checkInDate;
	}
	
	
	/*
	 * Getters and Setters
	 * get:
	 *  packageID
	 *  comment
	 *  checkInDate
	 *  checkOutDate
	 *  
	 * set:
	 * 	comment
	 * 	checkOutDate
	 */
	
	public Date getCheckOutDate() {
		return checkOutDate;
	}
	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
	}
	public long getPackageID() {
		return packageID;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getCheckInDate() {
		return checkInDate;
	}
}
