package monopoly;

public interface ErrorListener {
	/**
	 * Handles the error that was sent from the server as a response to a client
	 * action
	 * 
	 * @param error The error.
	 */
	public void errorOccured(Error error);
}
