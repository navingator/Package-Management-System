package model;

/*
 * Interface outlining the operations that can be performed by the model (model.database)
 * to the view (MainFrame)
 */

public interface IModelToViewAdaptor {

	/**
	 * Displays a message in a new popup window
	 * @param message			String containing message to be sent
	 */
	public void displayMessage(String message);
	
	/**
	 * Displays an error in a new popup window
	 * @param error				String containing error to be sent
	 */
	public void displayError(String error);
	
	
	/**
	 * Returns a choice from an array of strings
	 * @param choices			Array of strings containing choices
	 * @return
	 */
	public String getChoiceFromList(String[] choices);
	
	/**
	 * Returns new email settings 
	 * @param senderAddress		Old email address
	 * @param senderPassword	Old email password
	 * @param senderAlias		Old email name
	 * @return					Array of strings containing new information
	 */
	public String[] changeEmail(String senderAddress, String senderPassword, String senderAlias);
}
