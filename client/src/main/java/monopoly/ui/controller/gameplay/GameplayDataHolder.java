package monopoly.ui.controller.gameplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.LineUnavailableException;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import lombok.Getter;
import monopoly.network.packet.important.packet_data.UserPacketData;
import monopoly.network.packet.important.packet_data.gameplay.BoardPacketData;
import monopoly.network.packet.important.packet_data.gameplay.PlayerListPacketData;
import monopoly.network.packet.important.packet_data.gameplay.PlayerPacketData;
import monopoly.network.packet.realtime.BufferedImagePacket;
import monopoly.network.packet.realtime.MicSoundPacket;
import monopoly.network.packet.realtime.RealTimeNetworkPacket;
import monopoly.ui.ClientApplication;

public class GameplayDataHolder {
	private ClientApplication app;
	private PlayerListPacketData playerData;
	@Getter
	private BoardPacketData boardData;

	@Getter
	private List<PlayerPane> playerPanes;

	private Map<Integer, PlayerPane> idToPlayerPaneMap;

	public GameplayDataHolder(PlayerListPacketData playerData, BoardPacketData boardData, ClientApplication app) {
		this.playerData = playerData;
		this.boardData = boardData;
		this.app = app;

		idToPlayerPaneMap = new HashMap<>();
		createPlayerPanes();
	}

	private void createPlayerPanes() {
		List<PlayerPacketData> players = playerData.getPlayers();
		playerPanes = new ArrayList<>();

		for (int i = 0; i < players.size(); i++) {
			// Half of the players on the right, half in the left
			playerPanes.add(createPlayerPane(players.get(i), i >= players.size() / 2));
		}
	}

	private PlayerPane createPlayerPane(PlayerPacketData player, boolean onLeft) {
		UserPacketData userData = player.getUserData();
		int connectionId = userData.getConnectionId();

		boolean self = false;
		if (connectionId == app.getNetworkManager().getSelfConnectionId()) {
			self = true;
		}

		PlayerPane playerPane = new PlayerPane(onLeft, self, player.getUserData().getUsername(), app);
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
}
