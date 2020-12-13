package monopoly.ui.gameplay;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.LineUnavailableException;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import lombok.Setter;
import monopoly.network.packet.important.packet_data.PlayerPacketData;
import monopoly.network.packet.realtime.BufferedImagePacket;
import monopoly.network.packet.realtime.MicSoundPacket;
import monopoly.network.packet.realtime.RealTimeNetworkPacket;
import monopoly.ui.ClientApplication;
import monopoly.ui.MonopolyUIController;
import monopoly.ui.in_lobby.PlayerLobbyPane;

/**
 * Controls the base gameplay screen
 * 
 * @author Ziya Mukhtarov
 * @version Dec 13, 2020
 */
public class GameplayController implements MonopolyUIController {
	@Setter
	private ClientApplication app;

	@FXML
	private StackPane stackPane;
	@FXML
	private MigPane playersLeft;
	@FXML
	private MigPane playersRight;
	@FXML
	private ImageView board;
	@FXML
	private ImageView rotateCWIcon;
	@FXML
	private ImageView rotateCCWIcon;
	@FXML
	private ImageView chatIcon;
	@FXML
	private MigPane chatPane;

	private TranslateTransition openChatPane;
	private TranslateTransition closeChatPane;

	private RotateTransition boardRotateTransition;
	private ScaleTransition boardScaleTransition;
	private ParallelTransition boardRoateAndScaleTransition;

	private boolean chatOpen;
	private boolean boardRotating;
	private int currentBoardAngle;

	private Map<Integer, PlayerPane> playerMap;

	public GameplayController() {
		playerMap = Collections.synchronizedMap(new HashMap<Integer, PlayerPane>());

		currentBoardAngle = 0;
		chatOpen = false;
		boardRotating = true;

		openChatPane = new TranslateTransition(Duration.millis(500));
		openChatPane.setToX(0);
		closeChatPane = new TranslateTransition(Duration.millis(500));

		openChatPane.setOnFinished(e -> chatOpen = true);
		closeChatPane.setOnFinished(e -> chatOpen = false);

		boardRotateTransition = new RotateTransition(Duration.millis(1000));
		boardScaleTransition = new ScaleTransition(Duration.millis(500));
		boardScaleTransition.setCycleCount(2);
		boardScaleTransition.setAutoReverse(true);
		boardScaleTransition.setToX(0.65);
		boardScaleTransition.setToY(0.65);
		boardRoateAndScaleTransition = new ParallelTransition(boardRotateTransition, boardScaleTransition);
		boardRoateAndScaleTransition.setOnFinished(e -> boardRotating = false);
	}

	/**
	 * Adds the players in this lobby to this view. Creates a new audio channel per
	 * each player for voice chat
	 * 
	 * @param playerCollection The players to add
	 */
	public void addPlayers(Collection<PlayerLobbyPane> playerCollection) {
		Platform.runLater(() -> {
			PlayerLobbyPane[] players = playerCollection.toArray(new PlayerLobbyPane[0]);
			for (int i = 0; i < players.length; i++) {
				int connectionId = ((PlayerPacketData) players[i].getUserData()).getConnectionId();

				boolean self = false;
				if (connectionId == app.getNetworkManager().getSelfConnectionId()) {
					self = true;
				}

				PlayerPane playerPane;
				if (i % 2 == 0) {
					playerPane = new PlayerPane(true, self, players[i].getName(), app);
					playersLeft.add(playerPane, "grow, hmax 30%, wmax 100%");
				} else {
					playerPane = new PlayerPane(false, self, players[i].getName(), app);
					playersRight.add(playerPane, "grow, hmax 30%, wmax 100%");
				}

				AudioChannel audioChannel = null;
				try {
					audioChannel = new AudioChannel();
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				}
				playerPane.setUserData(audioChannel);

				playerMap.put(connectionId, playerPane);
			}
		});
	}

	/**
	 * Handles the received realtime packet - webcam or microphone input
	 * 
	 * @param packet The packet received
	 */
	public void realTimePacketReceived(RealTimeNetworkPacket packet) {
		int sourceId = packet.getSourceConnectionID();
		PlayerPane pane = playerMap.get(sourceId);

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

	@FXML
	public void initialize() {
		openChatPane.setNode(chatPane);
		closeChatPane.setNode(chatPane);
		boardRoateAndScaleTransition.setNode(board);

		new Thread(this::boardRotateAndEnter).start();
	}

	/**
	 * Cool board entrance effect
	 */
	private void boardRotateAndEnter() {
		RotateTransition rotate = new RotateTransition(Duration.seconds(1), board);
		ScaleTransition scale = new ScaleTransition(Duration.seconds(1), board);
		scale.setToX(0.8);
		scale.setToY(0.8);
		rotate.setFromAngle(-720);
		rotate.setToAngle(0);

		ScaleTransition scaleToDefault = new ScaleTransition(Duration.millis(500), board);
		scaleToDefault.setToX(1);
		scaleToDefault.setToY(1);

		ParallelTransition rotateAndScale = new ParallelTransition(rotate, scale);
		PauseTransition pause = new PauseTransition(Duration.millis(500));
		SequentialTransition boardEntranceEffect = new SequentialTransition(rotateAndScale, pause, scaleToDefault);
		boardEntranceEffect.setOnFinished(e -> boardRotating = false);
		boardEntranceEffect.play();
	}

	@FXML
	private void toggleChatPane() {
		if (chatOpen) {
			closeChatPane.play();
		} else {
			openChatPane.play();
		}
	}

	@FXML
	private void rotateBoardCW() {
		if (boardRotating)
			return;

		boardRotating = true;
		currentBoardAngle = currentBoardAngle + 90;
		boardRotateTransition.setToAngle(currentBoardAngle);
		boardRoateAndScaleTransition.play();
	}

	@FXML
	private void rotateBoardCCW() {
		if (boardRotating)
			return;

		boardRotating = true;
		currentBoardAngle = currentBoardAngle - 90;
		boardRotateTransition.setToAngle(currentBoardAngle);
		boardRoateAndScaleTransition.play();
	}

	@Override
	public void sizeChanged(double width, double height) {
		double boardSize = Math.min(width * 0.5, height * 0.9);
		board.setFitWidth(boardSize);
		board.setFitHeight(boardSize);

		double iconWidth = width * 0.025;
		chatIcon.setFitWidth(iconWidth);
		rotateCWIcon.setFitWidth(iconWidth);
		rotateCCWIcon.setFitWidth(iconWidth);

		double iconHeight = height * 0.05;
		chatIcon.setFitHeight(iconHeight);
		rotateCWIcon.setFitHeight(iconHeight);
		rotateCCWIcon.setFitHeight(iconHeight);

		double chatPaneWidth = width * 0.2;
		chatPane.setMinWidth(chatPaneWidth);
		chatPane.setPrefWidth(chatPaneWidth);
		chatPane.setMaxWidth(chatPaneWidth);

		chatPane.setTranslateX(chatPane.getPrefWidth());
		closeChatPane.setToX(chatPane.getPrefWidth());
	}
}
