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
	private boolean notificationSent;
	
	
	public Package(long packageID, String comment, Date checkInDate) {
		this.packageID = packageID;
		this.comment = comment;
		this.checkInDate = checkInDate;
		this.notificationSent = false;
	}
	
	
	/*
	 * Getters and Setters
	 * get:
	 *  packageID
	 *  comment
	 *  checkInDate
	 *  checkOutDate
	 *  notificationSent
	 *  
	 * set:
	 * 	comment
	 * 	checkOutDate
	 * 	notificationSent
	 */
	
	public Date getCheckOutDate() {
		return checkOutDate;
	}
	public boolean isNotificationSent() {
		return notificationSent;
	}
	public long getPackageID() {
		return packageID;
	}
	public String getComment() {
		return comment;
	}
	public Date getCheckInDate() {
		return checkInDate;
	}
	
	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public void setNotificationSent(boolean notificationSent) {
		this.notificationSent = notificationSent;
	}
}
