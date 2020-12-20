package monopoly.ui.controller.gameplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.LineUnavailableException;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.paint.Color;
import lombok.Getter;
import monopoly.network.packet.important.packet_data.UserPacketData;
import monopoly.network.packet.important.packet_data.gameplay.BoardPacketData;
import monopoly.network.packet.important.packet_data.gameplay.DicePacketData;
import monopoly.network.packet.important.packet_data.gameplay.PlayerListPacketData;
import monopoly.network.packet.important.packet_data.gameplay.PlayerPacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.StreetTitleDeedPacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.TilePacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.TileType;
import monopoly.network.packet.realtime.BufferedImagePacket;
import monopoly.network.packet.realtime.MicSoundPacket;
import monopoly.network.packet.realtime.RealTimeNetworkPacket;
import monopoly.ui.controller.gameplay.board.Token;
import monopoly.ui.controller.gameplay.titledeed.DeedCard;
import monopoly.ui.controller.gameplay.titledeed.RailroadTitleDeedPane;
import monopoly.ui.controller.gameplay.titledeed.StreetTitleDeedPane;
import monopoly.ui.controller.gameplay.titledeed.UtilitiesTileDeedPane;

public class GameplayDataManager {
	private static final Color[] playerColors = { Color.web("#ff4646"), Color.web("#7579e7"), Color.web("#ffdf5e"),
			Color.web("#ec8cff"), Color.web("#d0f4ff"), Color.web("#fc8621") };

	private GameplayController gameplayController;
	private PlayerListPacketData playerData;
	@Getter
	private BoardPacketData boardData;

	@Getter
	private List<PlayerPane> playerPanes;
	private Map<Integer, PlayerPane> idToPlayerPaneMap;

	private boolean diceRolling;
	private boolean tokenMoving;

	/**
	 * Holds the player that should play now
	 */
	private PlayerPane turn;

	public GameplayDataManager(PlayerListPacketData playerData, BoardPacketData boardData,
			GameplayController gameplayController) {
		this.playerData = playerData;
		this.boardData = boardData;
		this.gameplayController = gameplayController;

		diceRolling = false;
		idToPlayerPaneMap = new HashMap<>();
		createPlayerPanes();

		changePlayerTurn(playerData.getPlayers().get(0));
	}

	private void createPlayerPanes() {
		List<PlayerPacketData> players = playerData.getPlayers();
		playerPanes = new ArrayList<>();

		for (int i = 0; i < players.size(); i++) {
			// Half of the players on the right, half in the left
			playerPanes.add(createPlayerPane(players.get(i), i >= players.size() / 2, playerColors[i]));
		}
	}

	private PlayerPane createPlayerPane(PlayerPacketData player, boolean onLeft, Color color) {
		UserPacketData userData = player.getUserData();
		int connectionId = userData.getConnectionId();

		boolean self = false;
		if (connectionId == getSelfConnectionId()) {
			self = true;
		}

		PlayerPane playerPane = new PlayerPane(onLeft, self, player.getUserData().getUsername(), color,
				gameplayController.getApp());
		idToPlayerPaneMap.put(connectionId, playerPane);

		// Create player's audio channel for microphone
		AudioChannel audioChannel = null;
		try {
			audioChannel = new AudioChannel();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		// Store AudioChannel of the user in his/her pane's data
		playerPane.setUserData(audioChannel);

		return playerPane;
	}

	/**
	 * Handles the received real-time packet - webcam or microphone input, resolving
	 * its source player and playing the audio or showing the webcam image in the
	 * right pane
	 * 
	 * @param packet The packet received
	 */
	public void resolveRealTimePacketSourceAndPlay(RealTimeNetworkPacket packet) {
		int sourceId = packet.getSourceConnectionID();
		PlayerPane pane = idToPlayerPaneMap.get(sourceId);

		if (pane == null) {
			return;
		}

		if (packet instanceof MicSoundPacket) {
			((AudioChannel) pane.getUserData()).addToQueue((MicSoundPacket) packet);
		} else {
			Platform.runLater(() -> pane.getPlayerImage()
					.setImage(SwingFXUtils.toFXImage(((BufferedImagePacket) packet).getImg(), null)));
		}
	}

	public void changePlayerTurn(PlayerPacketData playerPacketData) {
		if (turn != null) {
			turn.setPlayerTurn(false);
			if (turn.isSelf())
				gameplayController.setDiceDisable(true);
		}

		turn = getPlayerPaneOfPlayer(playerPacketData);
		turn.setPlayerTurn(true);
		if (turn.isSelf())
			gameplayController.setDiceDisable(false);
	}

	public void moveToken(PlayerPacketData player, TilePacketData destination) {
		tokenMoving = true;
		Token token = getPlayerPaneOfPlayer(player).getToken();

		new Thread(() -> {
			waitWhileDiceRolling();

			while (token.getCurrentTile().getTileIndex() != destination.getIndex()) {
				token.moveToNext();
				try {
					Thread.sleep((long) Token.MOVE_ANIMATION_DURATION.toMillis());
				} catch (InterruptedException e) {
					e.printStackTrace();
					Thread.currentThread().interrupt();
				}
			}

			tokenMoving = false;
		}).start();
	}

	public void rollDiceAndshowResult(DicePacketData dicePacketData) {
		diceRolling = true;
		new Thread(() -> {
			gameplayController.getDice().rollAndShowResult(dicePacketData);
			diceRolling = false;
		}).start();
	}

	private PlayerPane getPlayerPaneOfPlayer(PlayerPacketData player) {
		return idToPlayerPaneMap.get(player.getUserData().getConnectionId());
	}

	private int getSelfConnectionId() {
		return gameplayController.getApp().getNetworkManager().getSelfConnectionId();
	}

	private void waitWhileDiceRolling() {
		while (diceRolling) {
			sleep100ms();
		}
	}

	private void waitWhileTokenMoving() {
		while (tokenMoving) {
			sleep100ms();
		}
	}

	private void sleep100ms() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}

	public void displayBuyOrAuctionPane(TilePacketData tilePacketData) {
		DeedCard deedCard;
		TileType tileType = tilePacketData.getType();
		if (tileType == TileType.RAILROAD) {
			deedCard = new RailroadTitleDeedPane(tilePacketData.getTitleDeed());
		} else if (tileType == TileType.UTILITY) {
			deedCard = new UtilitiesTileDeedPane(tilePacketData.getTitleDeed());
		} else {
			deedCard = new StreetTitleDeedPane((StreetTitleDeedPacketData) tilePacketData.getTitleDeed());
		}

		new Thread(() -> {
			waitWhileDiceRolling();
			waitWhileTokenMoving();
			gameplayController.showPopup(new BuyOrAuctionPane(deedCard, gameplayController.getApp()));
		}).start();
	}
}
