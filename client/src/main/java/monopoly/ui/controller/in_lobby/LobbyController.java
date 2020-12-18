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
import monopoly.network.packet.important.packet_data.UserListPacketData;
import monopoly.network.packet.important.packet_data.UserPacketData;
import monopoly.ui.ClientApplication;
import monopoly.ui.UIUtil;
import monopoly.ui.controller.MonopolyUIController;

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
	private Map<Integer, UserLobbyPane> userMap;

	public LobbyController() {
		userMap = Collections.synchronizedMap(new HashMap<Integer, UserLobbyPane>());
	}

	@FXML
	private void initialize() {
		UserListPacketData users = app.getNetworkManager().getUsersInLobby();
		if (users != null) {
			users.getUsers().forEach(this::userJoined);
		}
	}

	/**
	 * Displays a new player that joined this lobby
	 * 
	 * @param user The player joining this lobby
	 */
	public void userJoined(UserPacketData user) {
		Platform.runLater(() -> {
			UserLobbyPane playerPane = new UserLobbyPane(user.isOwner() ? "admin" : "other", user.getUsername());
			playerPane.setUserData(user);
			MigPane.setCc(playerPane, "grow, hmax 16%, wmax 100%");
			players.getChildren().add(playerPane);
			userMap.put(user.getConnectionId(), playerPane);
		});
	}

	/**
	 * Changes the player's readiness status
	 * 
	 * @param user The player data that identifies the player and contains the
	 *             readiness to set
	 */
	public void userReadyChange(UserPacketData user) {
		userMap.get(user.getConnectionId()).changeReady(user.isReady());
	}

	@FXML
	private void changeReady() {
		if (readyButton.getText().equals("Ready")) {
			readyButton.setText("Not Ready");
			readyButton.getStyleClass().set(1, "buttonDanger");
			app.getNetworkManager().setReady(true);
		} else {
			readyButton.setText("Ready");
			readyButton.getStyleClass().set(1, "buttonRegular");
			app.getNetworkManager().setReady(false);
		}
	}

	/**
	 * Switches the screen to gameplay screen
	 */
	public void gameStart() {
		Platform.runLater(() -> app.switchToView("fxml/Gameplay.fxml"));
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
