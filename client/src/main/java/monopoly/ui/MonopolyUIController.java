package monopoly.ui;

/**
 * All the high-level monopoly controllers should implement this interface to be
 * recognized by the application.
 * 
 * @author Ziya Mukhtarov
 * @version Dec 13, 2020
 */
public interface MonopolyUIController {
	/**
	 * Sets the application this controller is controlling
	 * 
	 * @param clientApplication The application
	 */
	void setApp(ClientApplication clientApplication);

	/**
	 * Called when the bounds of this pane is changed. Responsive UIs should use
	 * this method to update their font sizes and scaling
	 * 
	 * @param width  The total width of this container
	 * @param height The total height of this container
	 */
	void sizeChanged(double width, double height);
}
