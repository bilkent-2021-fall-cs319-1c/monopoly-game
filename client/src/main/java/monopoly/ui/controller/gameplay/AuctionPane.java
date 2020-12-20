package monopoly.ui.controller.gameplay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import monopoly.ui.ClientApplication;
import monopoly.ui.UIUtil;
import monopoly.ui.controller.gameplay.titledeed.DeedCard;

/**
 * Models a street deed card in Monopoly.
 *
 * @author Ege Kaan Gürkan
 * @version Dec 17, 2020
 */

public class AuctionPane extends MigPane {
	@FXML
	private MigPane playerPanesWrapper;
	@FXML
	private MigPane middleWrapper;
	@FXML
	private MigPane deedCardDisplay;
	@FXML
	private Text auctionText;
	@FXML
	private Text highestBidText;
	@FXML
	private Text bidHereText;
	@FXML
	private Spinner<Integer> bidInput;
	@FXML
	private Button bidButton;
	@FXML
	private Button passButton;

	private Pane deedCard;
	private ClientApplication app;
	private List<AuctionPlayerPane> playerPanes;
	private int highestBid;

	public AuctionPane(DeedCard deedCard, List<PlayerPane> playerPanes, ClientApplication app) {
		this.deedCard = (Pane) deedCard;
		this.app = app;

		this.playerPanes = new ArrayList<>();
		playerPanes.forEach(playerPane -> this.playerPanes.add(new AuctionPlayerPane(playerPane)));

		FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/AuctionPane.fxml"));
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
		deedCardDisplay.getChildren().add(deedCard);
		playerPanes.forEach(playerPanesWrapper::add);
		setDisableControl(true);
		setHighestBid(10);

		layoutBoundsProperty().addListener((observable, oldVal, newVal) -> adjustSize());
	}

	@FXML
	public void bid() {
		app.getNetworkManager().bidOnAuction(bidInput.getValue());
	}

	@FXML
	public void pass() {
		app.getNetworkManager().passOnAuction();
	}

	public void adjustSize() {
		double height = getHeight();
		double width = getWidth();

		UIUtil.fitFont(auctionText, width, height * 0.05);
		UIUtil.fitFont(highestBidText, width, height * 0.04);
		UIUtil.fitFont(bidHereText, width, height * 0.1);

		UIUtil.fitFont(bidButton, width * 0.03, height * 0.03);
		UIUtil.fitFont(passButton, width * 0.04, height * 0.04);
		UIUtil.fitFont(bidInput.getEditor(), Double.MAX_VALUE, height * 0.03);
	}

	/**
	 * @return the auction pane of the player with the given color, or null if it
	 *         could not be found
	 */
	public AuctionPlayerPane getPlayerPane(Color color) {
		Optional<AuctionPlayerPane> optional = playerPanes.stream()
				.filter(playerPane -> playerPane.getColor().equals(color)).findAny();
		if (!optional.isPresent())
			return null;
		return optional.get();
	}

	public void setDisableControl(boolean disable) {
		bidButton.setDisable(disable);
		passButton.setDisable(disable);
		bidInput.setDisable(disable);

		if (!disable) {
			int currentBalance = ((GameplayController) app.getMainController()).getGameData().getThisPlayersBalance();
			if (currentBalance <= highestBid)
				bidInput.setDisable(true);
			else
				bidInput.setValueFactory(
						new SpinnerValueFactory.IntegerSpinnerValueFactory(1, currentBalance - highestBid));
		}
	}

	public void setHighestBid(int bid) {
		highestBid = bid;
		highestBidText.setText("Highest Bid: " + bid + "M");
	}
}
