package monopoly.ui.controller;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import lombok.Setter;
import monopoly.ui.ClientApplication;
import monopoly.ui.UIUtil;

/**
 * Controls the main menu UI
 * 
 * @author Ziya Mukhtarov
 * @version Dec 13, 2020
 */
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
	public void sizeChanged(double width, double height) {
		UIUtil.fitFont(heroMonopolyText, width * 0.33, height * 0.2);

		UIUtil.fitFont(createGameButton, width * 0.32 - 40, height * 0.05);
		UIUtil.fitFont(joinGameButton, width * 0.32 - 40, height * 0.05);
		UIUtil.fitFont(helpButton, width * 0.32 - 40, height * 0.05);
		UIUtil.fitFont(exitButton, width * 0.32 - 40, height * 0.05);
	}

	@FXML
	public void switchToCreateGameScreen() {
		app.switchToView("fxml/CreateLobby.fxml");
	}

	@FXML
	public void switchToJoinLobbyScreen() {
		app.switchToView("fxml/JoinLobby.fxml");
	}

	@FXML
	public void switchToHelpScreen() {
		app.switchToView("fxml/Help.fxml");
	}

	@FXML
	public void exitApp() {
		Platform.exit();
	}

}
