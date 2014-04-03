package model;

/*
 * Interface outlining the operations that can be performed by the model (model.database)
 * to the view (MainFrame)
 */

public interface IModelToViewAdaptor {

	/**
	 * Displays a message in a new popup window
	 * @param message			String containing message to be sent
	 * @param title				Title to be shown on the dialog box
	 */
	public void displayMessage(String message, String title);
	
	/**
	 * Displays an error in a new popup window
	 * @param error				String containing error to be sent
	 * @param title				Title to be shown on the dialog box
	 */
	public void displayError(String error, String title);
	
	/**
	 * Displays a warning in a new popup window
	 * @param warning			String containing warning to be sent
	 * @param string			Title to be shown on the dialog box
	 */
	public void displayWarning(String warning, String title);
	
	/**
	 * Returns a choice from an array of strings
	 * @param message			Message to display when asking for choice
	 * @param title				Title to be shown on the dialog box
	 * @param choices			Array of strings containing choices
	 * @return
	 */
	public String getChoiceFromList(String message, String title, String[] choices);
	
	/**
	 * Returns a choice of printer from an array of printer name strings
	 * @param printerNames		Array of strings containing available printer names
	 * @return					User choice of printer
	 */
	public String getPrinterNames(String[] printerNames);
	
	/**
	 * Returns new email settings 
	 * @param senderAddress		Old email address
	 * @param senderPassword	Old email password
	 * @param senderAlias		Old email name
	 * @return					Array of strings containing new information
	 */
	public String[] changeEmail(String senderAddress, String senderPassword, String senderAlias);

	/**
	 * Get a yes or no response from the user
	 * @param message			Message to be sent with the response
	 * @param title				Title of the message
	 * @param options			Options to be displayed e.g. {"Yes","No"}
	 * @return
	 */
	public boolean getBooleanInput(String message, String title,
			String[] options);
}
