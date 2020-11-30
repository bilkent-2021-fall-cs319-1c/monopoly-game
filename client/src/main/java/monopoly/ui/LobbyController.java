package monopoly.ui;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import monopoly.network.packet.important.packet_data.PlayerPacketData;

public class LobbyController {
	@FXML
	private MigPane root;
	@FXML
	private MigPane players;
	@FXML
	private Text lobbyText;
	@FXML
	private Text nameText;
	@FXML
	private Text nameField;
	@FXML
	private Text passwordText;
	@FXML
	private Text passwordField;
	@FXML
	private Text waitingText;
	@FXML
	private Button readyButton;

	private ChangeListener<Number> widthListener;
	private ChangeListener<Number> heightListener;

	private Map<Integer, PlayerLobbyPane> playerMap;

	public LobbyController() {
		widthListener = (observable, oldValue, newValue) -> windowWidthChanged();
		heightListener = (observable, oldValue, newValue) -> windowHeightChanged();

		playerMap = Collections.synchronizedMap(new HashMap<Integer, PlayerLobbyPane>());
	}

	@FXML
	public void initialize() {
		root.sceneProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue != null) {
				oldValue.widthProperty().removeListener(widthListener);
				oldValue.heightProperty().removeListener(heightListener);
			}
			if (newValue != null) {
				newValue.widthProperty().addListener(widthListener);
				newValue.heightProperty().addListener(heightListener);

				windowHeightChanged();
				windowWidthChanged();
			}
		});
	}

	public void playerJoined(PlayerPacketData player) {
		Platform.runLater(() -> {
			try {
				PlayerLobbyPane playerPane = new PlayerLobbyPane(player.isAdmin() ? "admin" : "other",
						player.getUsername());
				MigPane.setCc(playerPane, "grow, hmax 16%, wmax 100%");
				players.getChildren().add(playerPane);
				playerMap.put(player.getConnectionId(), playerPane);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public void playerReady(PlayerPacketData player) {
		Platform.runLater(() -> playerMap.get(player.getConnectionId()).setStyle("-fx-background-color: lightgreen"));
	}

	@FXML
	private void iAmReady() {
		readyButton.setDisable(true);
		ClientApplication.getInstance().getNetworkManager().setReady(true);
	}

	private void windowHeightChanged() {
		double windowHeight = root.getScene().getHeight();
		double windowWidth = root.getScene().getWidth();

		setFontSizes(windowWidth, windowHeight);
	}

	private void windowWidthChanged() {
		double windowHeight = root.getScene().getHeight();
		double windowWidth = root.getScene().getWidth();

		setFontSizes(windowWidth, windowHeight);
	}

	private void setFontSizes(double width, double height) {
		lobbyText.setFont(UIUtil.calculateFittingFont(width * 0.32, height * 0.10, "Recoleta Alt", lobbyText.getText()));

		String avenirFontFamily = "Avenir Next";
		nameText.setFont(
				UIUtil.calculateFittingFont(width * 0.20, height * 0.09, avenirFontFamily, nameText.getText()));
		nameField.setFont(
				UIUtil.calculateFittingFont(width * 0.15, height * 0.09, avenirFontFamily, nameText.getText()));
		passwordText.setFont(
				UIUtil.calculateFittingFont(width * 0.25, height * 0.09, avenirFontFamily, passwordText.getText()));
		passwordField.setFont(
				UIUtil.calculateFittingFont(width * 0.15, height * 0.09, avenirFontFamily, nameText.getText()));
		waitingText.setFont(
				UIUtil.calculateFittingFont(width * 0.25, height * 0.09, avenirFontFamily, waitingText.getText()));
	}
}
