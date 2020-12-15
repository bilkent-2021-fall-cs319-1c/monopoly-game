package monopoly.ui.controller.in_lobby;

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
import monopoly.ui.UIUtil;
import monopoly.ui.controller.MonopolyUIController;
import monopoly.ui.controller.gameplay.GameplayController;

/**
 * Controls the lobby UI (the screen after joining a lobby and waiting for the
 * game start)
 * 
 * @author Ziya Mukhtarov
 * @version Dec 13, 2020
 */
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

	/**
	 * Displays a new player that joined this lobby
	 * 
	 * @param player The player joining this lobby
	 */
	public void playerJoined(PlayerPacketData player) {
		Platform.runLater(() -> {
			PlayerLobbyPane playerPane = new PlayerLobbyPane(player.isAdmin() ? "admin" : "other",
					player.getUsername());
			playerPane.setUserData(player);
			MigPane.setCc(playerPane, "grow, hmax 16%, wmax 100%");
			players.getChildren().add(playerPane);
			playerMap.put(player.getConnectionId(), playerPane);
		});
	}

	/**
	 * Changes the player's readiness status
	 * 
	 * @param player The player data that identifies the player and contains the
	 *               readiness to set
	 */
	public void playerReady(PlayerPacketData player) {
		Platform.runLater(() -> playerMap.get(player.getConnectionId()).setStyle("-fx-background-color: lightgreen"));
	}

	@FXML
	private void iAmReady() {
		readyButton.setDisable(true);
		app.getNetworkManager().setReady(true);
	}

	/**
	 * Switches the screen to gameplay screen
	 */
	public void gameStart() {
		Platform.runLater(() -> {
			app.switchToView("fxml/Gameplay.fxml");
			((GameplayController) app.getMainController()).addPlayers(playerMap.values());
		});
	}

	@Override
	public void sizeChanged(double width, double height) {
		setFontSizes(width, height);
	}

	/**
	 * Called when the bounds of this pane is changed. Responsive UIs should use
	 * this method to update their font sizes and scaling
	 * 
	 * @param width  The total width of this container
	 * @param height The total height of this container
	 */
	private void setFontSizes(double width, double height) {
		UIUtil.fitFont(lobbyText, width * 0.32, height * 0.1);

		UIUtil.fitFont(nameText, width * 0.2, height * 0.09);
		UIUtil.fitFont(nameField, width * 0.15, height * 0.09);
		UIUtil.fitFont(passwordText, width * 0.25, height * 0.09);
		UIUtil.fitFont(passwordField, width * 0.15, height * 0.09);
		UIUtil.fitFont(waitingText, width * 0.25, height * 0.09);
	}
}