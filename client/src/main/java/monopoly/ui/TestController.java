package monopoly.ui;

import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;

public class TestController {

	@FXML
	private void buttonPressed() {
		LoggerFactory.getLogger(TestController.class).info("Button Pressed!");
	}

	@FXML
	private void quitPressed() {
		System.exit(0);
	}
}
