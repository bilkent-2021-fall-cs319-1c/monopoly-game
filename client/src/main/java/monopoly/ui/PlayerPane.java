package monopoly.ui;

import java.io.IOException;
import java.util.stream.Stream;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PlayerPane extends MigPane {
    public static final String DEFAULT_SIDE = "left";

    @FXML
    private Label playerNameLabel;
    @FXML
    private Label moneyLabel;
    @FXML
    private Label tradeLabel;
    @FXML
    private ImageView playerImage;
    @FXML
    private Group playerNameLabelWrapper;

    private String side;

    public PlayerPane(@NamedArg("side") String side) throws IOException {
	this.side = side;

	FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/PlayerPane.fxml"));
	loader.setController(this);
	loader.setRoot(this);
	loader.load();

	Stream.of(widthProperty(), heightProperty()).forEach(p -> p.addListener((observable, oldVal, newVal) -> {
	    double width = getWidth();
	    double height = getHeight();

	    playerNameLabel
		    .setFont(new Font(calculateFittingFontSize(height, width * 0.09, playerNameLabel.getText())));

	    moneyLabel.setFont(
		    new Font(calculateFittingFontSize(width * 0.84 * 0.49, height * 0.14, moneyLabel.getText())));
	    tradeLabel.setFont(
		    new Font(calculateFittingFontSize(width * 0.84 * 0.49, height * 0.14, tradeLabel.getText())));

	    playerImage.setFitHeight(height * 0.83);
	    playerImage.setFitWidth(width * 0.86);

	    Image playerIconImg = new Image(GameplayController.class.getResourceAsStream(
		    "images/" + ("right".equals(side) ? "PlayerIconLookingLeft.png" : "PlayerIcon.png")));
	    playerImage.setImage(playerIconImg);
	}));
    }

    @FXML
    private void initialize() {
	if (side == null || "left".equals(side))
	    setCols("0[10%:10%:10%][grow]0");
	else {
	    setCols("0[grow][10%:10%:10%]0");

	    remove(playerNameLabelWrapper);
	    add(playerNameLabelWrapper, "spany 2, cell 1 0");
	    playerNameLabelWrapper.getChildren().get(0).setStyle("-fx-rotate: 90");
	}
    }

    private double calculateFittingFontSize(double width, double height, String text) {
	final int defaultFontSize = 20;
	Font font = Font.font(defaultFontSize);
	Text tempText = new Text(text);
	tempText.setFont(font);

	double textWidth = tempText.getLayoutBounds().getWidth();
	double textHeight = tempText.getLayoutBounds().getHeight();

	return Math.min(defaultFontSize * width / textWidth, defaultFontSize * height / textHeight);
    }
}
