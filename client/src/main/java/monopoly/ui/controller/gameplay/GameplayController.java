package monopoly.ui.controller.gameplay;

import java.util.List;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import monopoly.network.packet.realtime.RealTimeNetworkPacket;
import monopoly.ui.ClientApplication;
import monopoly.ui.controller.MonopolyUIController;
import monopoly.ui.controller.gameplay.board.Board;
import monopoly.ui.controller.gameplay.board.dice.Dice;

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
	private Board board;
	@FXML
	private ImageView rotateCWIcon;
	@FXML
	private ImageView rotateCCWIcon;
	@FXML
	private ImageView chatIcon;
	@FXML
	private MigPane chatPane;
	@FXML
	private Dice dice;

	private TranslateTransition openChatPane;
	private TranslateTransition closeChatPane;

	private RotateTransition boardRotateTransition;
	private ScaleTransition boardScaleTransition;
	private ParallelTransition boardRoateAndScaleTransition;

	private boolean chatOpen;
	private boolean boardRotating;

	@Getter
	private GameplayDataHolder gameData;

	public GameplayController() {
		chatOpen = false;
		boardRotating = false;

		openChatPane = new TranslateTransition(Duration.millis(500));
		openChatPane.setToX(0);
		closeChatPane = new TranslateTransition(Duration.millis(500));

		openChatPane.setOnFinished(e -> chatOpen = true);
		closeChatPane.setOnFinished(e -> chatOpen = false);

		boardRotateTransition = new RotateTransition(Duration.millis(1000));
		boardRotateTransition.setOnFinished(e -> board.setCacheHint(CacheHint.DEFAULT));

		boardScaleTransition = new ScaleTransition(Duration.millis(500));
		boardScaleTransition.setCycleCount(2);
		boardScaleTransition.setAutoReverse(true);
		boardScaleTransition.setToX(0.65);
		boardScaleTransition.setToY(0.65);

		boardRoateAndScaleTransition = new ParallelTransition(boardRotateTransition, boardScaleTransition);
		boardRoateAndScaleTransition.setOnFinished(e -> boardRotating = false);
	}

	@FXML
	public void initialize() {
		openChatPane.setNode(chatPane);
		closeChatPane.setNode(chatPane);
		boardRoateAndScaleTransition.setNode(board);

		gameData = app.getNetworkManager().getGameData(app);
		addPlayers(gameData.getPlayerPanes());
		board.buildBoard(gameData);

		sizeChanged(stackPane.getWidth(), stackPane.getHeight());
		Platform.runLater(this::boardRotateAndEnter);
	}

	/**
	 * Adds the players in this lobby to this view. Creates a new audio channel per
	 * each player for voice chat
	 * 
	 * @param playerCollection The players to add
	 */
	private void addPlayers(List<PlayerPane> players) {
		int playerCount = players.size();
		for (int i = 0; i < playerCount / 2; i++) {
			addPlayerPaneToView(players.get(i));
		}
		for (int i = playerCount - 1; i >= playerCount / 2; i--) {
			addPlayerPaneToView(players.get(i));
		}
	}

	private void addPlayerPaneToView(PlayerPane playerPane) {
		MigPane container;
		if (playerPane.isNameOnLeft())
			container = playersLeft;
		else
			container = playersRight;

		Platform.runLater(() -> container.add(playerPane, "grow, hmax 30%, wmax 100%"));
	}

	/**
	 * Handles the received realtime packet - webcam or microphone input
	 * 
	 * @param packet The packet received
	 */
	public void realTimePacketReceived(RealTimeNetworkPacket packet) {
		gameData.resolveRealTimePacketSourceAndPlay(packet);
	}

	/**
	 * Cool board entrance effect
	 */
	private void boardRotateAndEnter() {
		if (boardRotating)
			return;
		boardRotating = true;

		board.setCacheHint(CacheHint.SPEED);

		RotateTransition rotate = new RotateTransition(Duration.seconds(1), board);
		rotate.setFromAngle(-720);
		rotate.setToAngle(0);
		ScaleTransition scale = new ScaleTransition(Duration.seconds(1), board);
		scale.setFromX(0);
		scale.setFromY(0);
		scale.setToX(0.8);
		scale.setToY(0.8);

		ScaleTransition scaleToDefault = new ScaleTransition(Duration.millis(500), board);
		scaleToDefault.setToX(1);
		scaleToDefault.setToY(1);

		ParallelTransition rotateAndScale = new ParallelTransition(rotate, scale);
		PauseTransition pause = new PauseTransition(Duration.millis(500));
		SequentialTransition boardEntranceEffect = new SequentialTransition(rotateAndScale, pause, scaleToDefault);
		boardEntranceEffect.setOnFinished(e -> {
			boardRotating = false;
			board.setCacheHint(CacheHint.DEFAULT);

			FadeTransition diceFadeIn = new FadeTransition(Duration.millis(500), dice);
			diceFadeIn.setFromValue(0);
			diceFadeIn.setToValue(1);
			diceFadeIn.play();
		});
		board.setVisible(true);
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

		board.setCacheHint(CacheHint.SPEED);
		boardRotating = true;
		boardRotateTransition.setByAngle(90);
		boardRoateAndScaleTransition.play();
	}

	@FXML
	private void rotateBoardCCW() {
		if (boardRotating)
			return;

		board.setCacheHint(CacheHint.SPEED);
		boardRotating = true;
		boardRotateTransition.setByAngle(-90);
		boardRoateAndScaleTransition.play();
	}

	@Override
	public void sizeChanged(double width, double height) {
		int boardSize = ((int) Math.min(width * 0.5, height * 0.9) - 6) / 12 * 12 + 6;
		board.setMinWidth(boardSize);
		board.setMinHeight(boardSize);
		board.setPrefWidth(boardSize);
		board.setPrefHeight(boardSize);
		board.setMaxWidth(boardSize);
		board.setMaxHeight(boardSize);

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
