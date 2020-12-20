package monopoly.ui.controller.gameplay;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.jpro.webapi.WebAPI;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import monopoly.Error;
import monopoly.network.Client;
import monopoly.ui.ClientApplication;
import monopoly.ui.UIUtil;
import monopoly.ui.controller.gameplay.board.Token;

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

	@Getter
	private boolean nameOnLeft;
	@Getter
	private boolean self;
	@Getter
	private String username;
	@Getter
	private Color color;

	@Getter
	@Setter
	private Token token;

	private MicSender micSender;
	private WebcamSender webcamSender;

	@Getter
	private int balance;

	/**
	 * Initializes the pane. Creates webcam sender and microphone sender if this
	 * pane is displaying this player
	 * 
	 * @param nameOnLeft false for username to be on the right, or it will on the
	 *                   left
	 * @param self       true for webcam/audio controls, or trade button will be
	 *                   displayed
	 * @param username   Username to display
	 */
	public PlayerPane(boolean nameOnLeft, boolean self, String username, Color color, ClientApplication app) {
		this.app = app;
		this.nameOnLeft = nameOnLeft;
		this.self = self;
		this.username = username;
		this.color = color;

		balance = 1500;

		FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/PlayerPane.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (self) {
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

	@FXML
	private void initialize() {
		if (nameOnLeft)
			setCols("0[10%:10%:10%]1%:1%:1%[grow, right]0");
		else {
			setCols("0[grow, left]1%:1%:1%[10%:10%:10%]0");

			remove(playerNameLabelWrapper);
			add(playerNameLabelWrapper, "spany 2, cell 1 0");
			playerNameLabelWrapper.getChildren().get(0).setStyle("-fx-rotate: 90");
		}

		if (self) {
			tradeButton.setVisible(false);
			webcamIcon.setVisible(true);
			micIcon.setVisible(true);

			try {
				WebAPI.getWebAPI(app.getScene());

				// Running on a browser, disable webcam and mic
				webcamIcon.setDisable(true);
				micIcon.setDisable(true);
			} catch (RuntimeException e) {
				// Running as a Java Desktop application
			}
		}

		playerNameLabel.setText(username);

		Image playerIconImg = (nameOnLeft ? UIUtil.DEFAULT_PLAYER_IMAGE : UIUtil.DEFAULT_PLAYER_IMAGE_LOOKING_LEFT);
		playerImage.setImage(playerIconImg);

		setBackground(new Background(new BackgroundFill(color, null, null)));

		layoutBoundsProperty().addListener((observable, oldVal, newVal) -> Platform.runLater(this::adjustSize));
	}

	@FXML
	private void toggleMicrophone() {
		if (micSender == null) {
			app.displayError(new Error("Unable to Access a Microphone Device",
					"We could not identify any available microphone devices. Please check whether you have given access to a microphone device."));
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
		}
	}

	public void setPlayerTurn(boolean isTurn) {
		Platform.runLater(() -> {
			if (isTurn) {
				getStyleClass().removeAll("bordered");
				getStyleClass().add("playerTurn");
			} else {
				getStyleClass().removeAll("playerTurn");
				getStyleClass().add("bordered");
			}
		});
	}

	/**
	 * Adjusts the size of inner components based on the outer bounds
	 */
	private void adjustSize() {
		double width = getWidth();
		double height = getHeight();

		UIUtil.fitFont(playerNameLabel, height, width * 0.09);
		UIUtil.fitFont(moneyLabel, width * 0.84 * 0.49, height * 0.14);
		UIUtil.fitFont(tradeButton, width * 0.84 * 0.49 - 10, height * 0.14 - 10);

		playerImage.setFitHeight(height * 0.84);
		playerImage.setFitWidth(width * 0.89);

		double webcamMicIconSize = Math.min(height * 0.14, width * 0.88 * 0.49 / 2);
		webcamIcon.setFitHeight(webcamMicIconSize);
		webcamIcon.setFitWidth(webcamMicIconSize);
		micIcon.setFitHeight(webcamMicIconSize);
		micIcon.setFitWidth(webcamMicIconSize);
	}

	public void balanceChange(int balance) {
		Platform.runLater(() -> {
			moneyLabel.setText(balance + "M");
			if (balance > this.balance) {
				moneyLabel.setTextFill(Color.LAWNGREEN);
			} else {
				moneyLabel.setTextFill(Color.RED);
			}
			this.balance = balance;

			new Thread(() -> {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					Thread.currentThread().interrupt();
				}
				Platform.runLater(() -> moneyLabel.setTextFill(Color.BLACK));
			}).start();
		});
	}
}
