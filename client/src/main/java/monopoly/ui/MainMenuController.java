package monopoly.ui;

import java.io.IOException;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import lombok.Setter;

public class MainMenuController implements MonopolyUIController {
	@Setter
	private ClientApplication app;

	@FXML
	private MigPane mainMigPane;
	@FXML
	private Text heroMonopolyText;
	@FXML
	private Button createGameButton;
	@FXML
	private Button joinGameButton;
	@FXML
	private Button helpButton;
	@FXML
	private Button exitButton;

	@Override
	public void widthChanged(double width, double height) {
		UIUtil.fitFont(heroMonopolyText, width * 0.33, height * 0.2);

		UIUtil.fitFont(createGameButton, width * 0.32 - 40, height * 0.05);
		UIUtil.fitFont(joinGameButton, width * 0.32 - 40, height * 0.05);
		UIUtil.fitFont(helpButton, width * 0.32 - 40, height * 0.05);
		UIUtil.fitFont(exitButton, width * 0.32 - 40, height * 0.05);
	}

	@Override
	public void heightChanged(double width, double height) {
		widthChanged(width, height);
	}

	@FXML
	public void switchToCreateGameScreen() throws IOException {
		app.switchToView("fxml/CreateLobby.fxml");
	}

	@FXML
	public void switchToJoinLobbyScreen() throws IOException {
		app.switchToView("fxml/JoinLobby.fxml");
	}

	@FXML
	public void switchToHelpScreen() throws IOException {
		app.switchToView("fxml/Help.fxml");
	}

	@FXML
	public void exitApp() {
		Platform.exit();
	}

}
