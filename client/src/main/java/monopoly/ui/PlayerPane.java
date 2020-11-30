package monopoly.ui;

import java.io.IOException;
import java.util.stream.Stream;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PlayerPane extends MigPane {
	@FXML
	private Label playerNameLabel;
	@FXML
	private Label moneyLabel;
	@FXML
	private Button tradeButton;
	@FXML
	private ImageView playerImage;
	@FXML
	private ImageView webcamIcon;
	@FXML
	private ImageView micIcon;
	@FXML
	private Group playerNameLabelWrapper;

	private String side;
	private String player;

	public PlayerPane(@NamedArg("side") String side, @NamedArg("player") String player) throws IOException {
		this.side = side;
		this.player = player;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/PlayerPane.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		loader.load();

		Stream.of(widthProperty(), heightProperty())
				.forEach(p -> p.addListener((observable, oldVal, newVal) -> adjustSize()));
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
		}
	}

	@FXML
	private void toggleMicrophone() {
		if (micIcon.getImage() == UIUtil.MICROPHONE_CROSSED_ICON) {
			micIcon.setImage(UIUtil.MICROPHONE_ICON);
		} else {
			micIcon.setImage(UIUtil.MICROPHONE_CROSSED_ICON);
		}
	}

	@FXML
	private void toggleWebcam() {
		if (webcamIcon.getImage() == UIUtil.WEBCAM_CROSSED_ICON) {
			webcamIcon.setImage(UIUtil.WEBCAM_ICON);
		} else {
			webcamIcon.setImage(UIUtil.WEBCAM_CROSSED_ICON);
		}
	}
}
