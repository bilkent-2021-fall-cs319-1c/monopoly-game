package monopoly.ui.gameplay;

import java.io.IOException;
import java.util.stream.Stream;

import javax.sound.sampled.LineUnavailableException;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.jpro.webapi.WebAPI;

import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import monopoly.network.Client;
import monopoly.ui.ClientApplication;
import monopoly.ui.UIUtil;

/**
 * Displays a player's container in gameplay view
 * 
 * @author Ziya Mukhtarov
 * @version Nov 30, 2020
 */
public class PlayerPane extends MigPane {
	@FXML
	private Label playerNameLabel;
	@FXML
	private Label moneyLabel;
	@FXML
	private Button tradeButton;
	@Getter
	@FXML
	private ImageView playerImage;
	@FXML
	private ImageView webcamIcon;
	@FXML
	private ImageView micIcon;
	@FXML
	private Group playerNameLabelWrapper;

	private ClientApplication app;

	private String side;
	private String player;
	private String username;

	private MicSender micSender;
	private WebcamSender webcamSender;

	/**
	 * @param side     "right" for username to be on the right, or it will on the
	 *                 left
	 * @param player   "self" for webcam/audio controls, or trade button will be
	 *                 displayed
	 * @param username Username to display
	 */
	public PlayerPane(@NamedArg("side") String side, @NamedArg("player") String player, String username,
			ClientApplication app) {
		this.app = app;
		this.side = side;
		this.player = player;
		this.username = username;

		FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/PlayerPane.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Stream.of(widthProperty(), heightProperty())
				.forEach(p -> p.addListener((observable, oldVal, newVal) -> adjustSize()));

		if ("self".equals(player)) {
			Client client = app.getNetworkManager().getClient();
			webcamSender = new WebcamSender(client, Webcam.getDefault());

			webcamSender.getWebcam().addWebcamListener(new WebcamListener() {
				@Override
				public void webcamOpen(WebcamEvent we) {
					// Do nothing
				}

				@Override
				public void webcamImageObtained(WebcamEvent we) {
					Platform.runLater(() -> playerImage.setImage(SwingFXUtils.toFXImage(we.getImage(), null)));
				}

				@Override
				public void webcamDisposed(WebcamEvent we) {
					// Do nothing
				}

				@Override
				public void webcamClosed(WebcamEvent we) {
					// Do nothing
				}
			});

			try {
				micSender = new MicSender(client);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		}
	}

	private void adjustSize() {
		double width = getWidth();
		double height = getHeight();

		playerNameLabel.setFont(UIUtil.calculateFittingFont(height, width * 0.09, playerNameLabel.getText()));

		moneyLabel.setFont(UIUtil.calculateFittingFont(width * 0.84 * 0.49, height * 0.14, moneyLabel.getText()));
		tradeButton.setFont(
				UIUtil.calculateFittingFont(width * 0.84 * 0.49 - 10, height * 0.14 - 10, tradeButton.getText()));

		playerImage.setFitHeight(height * 0.84);
		playerImage.setFitWidth(width * 0.89);

		double webcamMicIconSize = Math.min(height * 0.14, width * 0.88 * 0.49 / 2);
		webcamIcon.setFitHeight(webcamMicIconSize);
		webcamIcon.setFitWidth(webcamMicIconSize);
		micIcon.setFitHeight(webcamMicIconSize);
		micIcon.setFitWidth(webcamMicIconSize);

		Image playerIconImg = ("right".equals(side) ? UIUtil.DEFAULT_PLAYER_IMAGE_LOOKING_LEFT
				: UIUtil.DEFAULT_PLAYER_IMAGE);
		playerImage.setImage(playerIconImg);

		tradeButton.getStyleClass().add("buttonRegular");
		tradeButton.getStyleClass().add("tradeButton");
		playerNameLabel.getStyleClass().add("playerNameLabel");
	}

	@FXML
	private void initialize() {
		if ("right".equals(side)) {
			setCols("0[grow]1%:1%:1%[10%:10%:10%]0");

			remove(playerNameLabelWrapper);
			add(playerNameLabelWrapper, "spany 2, cell 1 0");
			playerNameLabelWrapper.getChildren().get(0).setStyle("-fx-rotate: 90");
		} else
			setCols("0[10%:10%:10%]1%:1%:1%[grow]0");

		if ("self".equals(player)) {
			tradeButton.setVisible(false);
			webcamIcon.setVisible(true);
			micIcon.setVisible(true);

			try {
				WebAPI.getWebAPI(app.getScene());

				// Running on a browser, disable webcam and mic
				webcamIcon.setDisable(true);
				micIcon.setDisable(true);
			} catch (RuntimeException e) {
				// Running as a Java application
			}
		}

		playerNameLabel.setText(username);
	}

	@FXML
	private void toggleMicrophone() {
		if (micSender == null) {
			// TODO SHOW ERROR
			return;
		}

		if (micIcon.getImage() == UIUtil.MICROPHONE_CROSSED_ICON) {
			micIcon.setImage(UIUtil.MICROPHONE_ICON);
			micSender.start();
		} else {
			micIcon.setImage(UIUtil.MICROPHONE_CROSSED_ICON);
			micSender.stop();
		}
	}

	@FXML
	private void toggleWebcam() {
		if (webcamIcon.getImage() == UIUtil.WEBCAM_CROSSED_ICON) {
			webcamIcon.setImage(UIUtil.WEBCAM_ICON);
			webcamSender.start();
		} else {
			webcamIcon.setImage(UIUtil.WEBCAM_CROSSED_ICON);
			webcamSender.stop();

			playerImage.setImage(
					"right".equals(side) ? UIUtil.DEFAULT_PLAYER_IMAGE_LOOKING_LEFT : UIUtil.DEFAULT_PLAYER_IMAGE);
		}
	}
}
