package monopoly.ui;

import java.io.IOException;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
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

	private ChangeListener<Number> widthListener;

	public MainMenuController() {
		widthListener = (observable, oldValue, newValue) -> windowWidthChanged();
	}

	private void windowWidthChanged() {
		double windowWidth = mainMigPane.getScene().getWidth();
		double windowHeight = mainMigPane.getScene().getHeight();
		mainMigPane.setMaxWidth(windowWidth);

		heroMonopolyText.setFont(UIUtil.calculateFittingFont(Math.max(windowWidth * 0.33, 200), windowHeight,
				heroMonopolyText.getFont().getFamily(), heroMonopolyText.getText()));
	}

	@FXML
	public void initialize() {
		mainMigPane.sceneProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue != null) {
				oldValue.widthProperty().removeListener(widthListener);
			}
			if (newValue != null) {
				newValue.widthProperty().addListener(widthListener);
			}
		});
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
