package monopoly.ui.in_lobby;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;
import monopoly.network.packet.important.packet_data.PlayerPacketData;
import monopoly.ui.ClientApplication;
import monopoly.ui.MonopolyUIController;
import monopoly.ui.UIUtil;
import monopoly.ui.gameplay.GameplayController;

public class LobbyController implements MonopolyUIController {
	@Setter
	private ClientApplication app;

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

	@Getter
	private Map<Integer, PlayerLobbyPane> playerMap;

	public LobbyController() {
		playerMap = Collections.synchronizedMap(new HashMap<Integer, PlayerLobbyPane>());
	}

	public void playerJoined(PlayerPacketData player) {
		Platform.runLater(() -> {
			try {
				PlayerLobbyPane playerPane = new PlayerLobbyPane(player.isAdmin() ? "admin" : "other",
						player.getUsername());
				playerPane.setUserData(player);
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
		app.getNetworkManager().setReady(true);
	}

	public void gameStart() {
		Platform.runLater(() -> {
			try {
				app.switchToView("fxml/Gameplay.fxml");
				((GameplayController) app.getController()).addPlayers(playerMap.values());
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void heightChanged(double width, double height) {
		setFontSizes(width, height);
	}

	@Override
	public void widthChanged(double width, double height) {
		setFontSizes(width, height);
	}

	private void setFontSizes(double width, double height) {
		UIUtil.fitFont(lobbyText, width * 0.32, height * 0.1);

		UIUtil.fitFont(nameText, width * 0.2, height * 0.09);
		UIUtil.fitFont(nameField, width * 0.15, height * 0.09);
		UIUtil.fitFont(passwordText, width * 0.25, height * 0.09);
		UIUtil.fitFont(passwordField, width * 0.15, height * 0.09);
		UIUtil.fitFont(waitingText, width * 0.25, height * 0.09);
	}
}
