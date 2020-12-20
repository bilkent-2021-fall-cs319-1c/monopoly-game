package monopoly.ui.controller.gameplay;

import java.io.IOException;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Getter;
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

	@Getter
	private Color color;
	private String playerName;
	@Getter
	private PlayerPane playerPane;

	public AuctionPlayerPane(PlayerPane playerPane) {
		this.playerPane = playerPane;
		this.color = playerPane.getColor();
		this.playerName = playerPane.getUsername();

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
		playerNameWrapper.setBackground(new Background(new BackgroundFill(color, null, null)));

		layoutBoundsProperty().addListener((observable, oldVal, newVal) -> adjustSize());
	}

	private void adjustSize() {
		double height = getHeight();
		double width = getWidth();

		UIUtil.fitFont(playerNameText, width * 0.5, height * 0.3);
		UIUtil.fitFont(yourTurnText, width * 0.5, height * 0.2);
	}

	public void passed() {
		playerNameWrapper.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
	}

	public void setTurn(boolean inTurn) {
		yourTurnWrapper.setVisible(inTurn);
	}
}
