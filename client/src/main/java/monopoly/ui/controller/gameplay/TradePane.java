package monopoly.ui.controller.gameplay;

import java.io.IOException;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import monopoly.ui.UIUtil;
import monopoly.ui.controller.gameplay.titledeed.DeedCard;

/**
 *
 * Models a trading pane.
 *
 * @author Ege Kaan Gürkan
 * @version 19/12/2020
 *
 */

public class TradePane extends MigPane {

	@FXML
	private Text tradeTitle;
	@FXML
	private Text playerOneNameText;
	@FXML
	private Text playerTwoNameText;
	@FXML
	private Text playerTwoMoneyText;
	@FXML
	private Text playerOneMoneyText;
	@FXML
	private ComboBox<String> playerOneComboBox;
	@FXML
	private MigPane playerOneAssetsWrapper;

	private final String playerOneName;
	private final String playerTwoName;
	private final Node[] playerOneAssets;
	private final Node[] playerTwoAssets;
	private final Node[] playerOneTradeAssets;
	private final Node[] playerTwoTradeAssets;

	public TradePane(@NamedArg("playerOneName") String playerOneName, @NamedArg("playerTwoName") String playerTwoName,
			@NamedArg("playerOneAssets") Node[] playerOneAssets, @NamedArg("playerTwoAssets") Node[] playerTwoAssets,
			@NamedArg("playerOneTradeAssets") Node[] playerOneTradeAssets,
			@NamedArg("playerTwoTradeAssets") Node[] playerTwoTradeAssets) {

		FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/TradePane.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		this.playerOneName = playerOneName;
		this.playerTwoName = playerTwoName;
		this.playerOneAssets = playerOneAssets;
		this.playerTwoAssets = playerTwoAssets;
		this.playerOneTradeAssets = playerOneTradeAssets;
		this.playerTwoTradeAssets = playerTwoTradeAssets;

		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	private void initialize() {

		tradeTitle.setText("Trade with: " + playerTwoName);

		for (Node n : playerOneAssets) {
			playerOneComboBox.getItems().add(((DeedCard) n).getName());
		}
		for (Node n : playerOneTradeAssets) {
			playerOneAssetsWrapper.getChildren().add(n);
		}

		layoutBoundsProperty().addListener((observable, oldVal, newVal) -> Platform.runLater(this::adjustSize));
	}

	private void adjustSize() {
		double height = getHeight();
		double width = getWidth();

		UIUtil.fitFont(tradeTitle, width, height * 0.07);

		UIUtil.fitFont(playerOneNameText, width, height * 0.03);
		UIUtil.fitFont(playerTwoNameText, width, height * 0.03);

		UIUtil.fitFont(playerOneMoneyText, width, height * 0.03);
		UIUtil.fitFont(playerTwoMoneyText, width, height * 0.03);
	}

}
