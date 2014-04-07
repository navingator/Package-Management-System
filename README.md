Package-Management-System
=========================
Author: Navin Pathak

Package management utility for the Jones College Mail Room.

Installation instructions
-------------------------

1. [Install Java SE 7+](https://www.java.com/en/download/)
2. Download the Package Management System.jar file
3. Open the Package Management System
4. Setup email with a [Gmail](mail.google.com) account.
    - Name - the name that will be sending all of the emails. Example - Jones College Mail Room, Mail Room, etc.
    - Email - your Gmail email address
    - Password - your Gmail password
5. Select a printer from the dropdown menu. Make sure that the drivers are installed.
6. Import a CSV file containing student information (Admin -> Student Information -> Import)
   Each line represents a student, and the CSV must have a header that reads:  
   **Last Name,First Name,NetID,Email Address**  
   CSV files can be generated from [Microsoft Excel](http://office.microsoft.com/en-us/excel-help/import-or-export-text-txt-or-csv-files-HP010099725.aspx) 
   or any [other spreadsheet program] (http://www.computerhope.com/issues/ch001356.htm).

Usage instructions
------------------

### Start up
1. Open the Package Management System.
2. Wait for the pick up screen to appear. This may take a couple of minutes every day, 
   as the system sends out reminder emails upon startup.

### Check In
For each package you want to check in,

1. Select the tab labelled "Check In" on the left hand menu.
2. Begin typing the student's first or last name in the student box  
**Make sure that you have already imported all students (see step 4 of installation).**
3. Select the student from the dropdown menu that appears.
4. (Optional) Enter any comments you have for the package in the comment menu.
5. Push the submit button.
6. Attach the printed barcode to a package.


### Check Out
For each package you want to check out,

1. Select the tab labelled "Pick Up" on the left hand menu.
2. Scan a barcode or type in the barcode into the text box and press enter.
3. Confirm that you are the person indicated in the popup.

Admin Operations
----------------

### Package Operations
#### Check Out Package
If someone has accidentally picked up a package and forgotten to check it out, they will continue
to recieve notifications about the package. To check out this package,

1. Go to the Packages tab (Admin -> Packages)
2. (Optional) Search with the search bar or sort by clicking on any of the column headers. 
   Separate the search terms with a space.3. Right click on the package to be checked out.
4. Click on "Check Out Package" in the popup menu.
5. Confirm that you want to check out the package.

#### Reprint Label
If the printer runs out of ink or there's any printer issue, you may want to reprint the label. To do so,

1. Go to the Packages tab (Admin -> Packages)
2. (Optional) Search with the search bar or sort by clicking on any of the column headers. 
   Separate the search terms with a space.3. Right click on the package for which the label will be printed.
4. Click on "Reprint Label" in the popup menu.
5. Confirm that you want to reprint the label.

#### Resend Package Notification
If the internet was disconnected when a package was checked out, a notification will not be sent.
To resend a notification,

1. Go to the Packages tab (Admin -> Packages)
2. (Optional) Search with the search bar or sort by clicking on any of the column headers. 
   Separate the search terms with a space.
3. Right click on the package for which the notification will be resent.
4. Click on "Resend Notification" in the popup menu.
5. Confirm that you want to send a notification.

### Student Operations

#### Import New Students
At the start of the new semester, or upon opening the program, you will want to import
all new students instead of adding them individually. To do so,

1. Go to the Student Information tab (Admin -> Student Information)
2. Click on the Import button at the lower left hand corner.
3. Navigate to the CSV file that you have prepared. For this file,
   each line represents a student, and the CSV must have a header that reads:  
   **Last Name,First Name,NetID,Email Address**  
   CSV files can be generated from [Microsoft Excel](http://office.microsoft.com/en-us/excel-help/import-or-export-text-txt-or-csv-files-HP010099725.aspx) 
   or any [other spreadsheet program] (http://www.computerhope.com/issues/ch001356.htm).
4. Open the file and follow any instructions that pop up.

Note: This will archive all students not in the CSV file. To re add them, use the add button
on the same tab.

#### Add New Student
If a student was not in the CSV file, you can add them:

1. Go to the Student Information tab (Admin -> Student Information)
2. Click on the Add button at the lower right hand corner.
3. Enter all of the student's information.
4. Press submit.


#### Edit Student
If a student wants to change their name or email,

1. Go to the Student Information tab (Admin -> Student Information)
2. (Optional) Search with the search bar or sort by clicking on any of the column headers. 
   Separate the search terms with a space.
3. Right click on the student to be edited.
4. Select "Edit Student" from the popup menu.
5. Enter all of the student's information. Note that the netID cannot be changed.
6. Press submit.

#### Delete Student
If a student was added by mistake, they can be easily removed,

1. Go to the Student Information tab (Admin -> Student Information)
2. (Optional) Search with the search bar or sort by clicking on any of the column headers. 
   Separate the search terms with a space.
3. Right click on the student to be deleted.
4. Select "Delete Student" from the popup menu.
5. Confirm that you want to delete the student.

Acknowledgements
================
First and foremost, I'd like to thank Christopher Henderson for all of the advice that he gave throughout the project.
Without him, this project would not have been possible.

Second, I would like to thank the members of Jones 3rd South who contributed to the testing of this project:
Ambi Bobmanuel  
Christopher Henderson  
Keiko Kaplan  
Avinash Shivakumar  s
Lucas Shumaker  
Mitch Torczon  
Blane Townsend

Last, but not least, I would like to thank Michelle Bennack for being the most awesome coordinator Jones has ever seen!
