package monopoly.ui.controller.gameplay;

import java.io.IOException;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Text;
import monopoly.ui.UIUtil;

public class AuctionPlayerPane extends MigPane {

	@FXML
	private MigPane yourTurnWrapper;
	@FXML
	private MigPane playerNameWrapper;
	@FXML
	private Text playerNameText;
	@FXML
	private Text yourTurnText;

	private String color;
	private String playerName;
	private boolean isTurn;

	public AuctionPlayerPane(@NamedArg("color") String color, @NamedArg("playerName") String playerName,
			@NamedArg("isTurn") boolean isTurn) {
		this.color = color;
		this.playerName = playerName;
		this.isTurn = isTurn;

		FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/AuctionPlayerPane.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void initialize() {
		playerNameText.setText(playerName);
		playerNameWrapper.setStyle("-fx-background-color: " + color + ";");
		if (!isTurn) {
			yourTurnWrapper.setVisible(false);
		}

		layoutBoundsProperty().addListener((observable, oldVal, newVal) -> adjustSize());
	}

	private void adjustSize() {
		double height = getHeight();
		double width = getWidth();

		UIUtil.fitFont(playerNameText, width * 0.5, height * 0.3);
		UIUtil.fitFont(yourTurnText, width * 0.5, height * 0.2);
	}

}
