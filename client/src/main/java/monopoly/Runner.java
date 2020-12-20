package monopoly;

import javafx.application.Application;
import monopoly.ui.ClientApplication;

public class Runner {
	public static void main(String[] args) {
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG");
		Application.launch(ClientApplication.class);
		System.exit(0);
	}
}
