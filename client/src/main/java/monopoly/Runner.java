package monopoly;

import javafx.application.Application;
import monopoly.ui.ClientApplication;

public class Runner {
	public static void main(String[] args) {
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG");
		System.setProperty("org.slf4j.simpleLogger.log.com.github.sarxos.webcam.ds.buildin.WebcamDefaultDriver",
				"ERROR");
		Application.launch(ClientApplication.class);
		System.exit(0);
	}
}
