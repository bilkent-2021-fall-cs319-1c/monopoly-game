package monopoly.ui.controller.gameplay;

import java.io.IOException;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import monopoly.ui.ClientApplication;
import monopoly.ui.UIUtil;
import monopoly.ui.controller.gameplay.titledeed.DeedCard;

public class BuyOrAuctionPane extends MigPane {
	@FXML
	private MigPane buttonGroup;
	@FXML
	private MigPane deedCardDisplay;
	@FXML
	private Text paneTitle;
	@FXML
	private Text balanceText;
	@FXML
	private Text infoText;
	@FXML
	private Button buyButton;
	@FXML
	private Button auctionButton;

	private Pane deedCard;
	private ClientApplication app;

	public BuyOrAuctionPane(DeedCard deedCard, ClientApplication app) {
		this.deedCard = (Pane) deedCard;
		this.app = app;

		FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/BuyOrAuctionPane.fxml"));
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

		int currentBalance = ((GameplayController) app.getMainController()).getGameData().getThisPlayersBalance();
		int propertyCost = ((DeedCard) deedCard).getBuyCost();
		balanceText.setText("Your balance: " + currentBalance + "M. This property costs " + propertyCost + "M.");
		if (currentBalance < propertyCost) {
			buyButton.setDisable(true);
			balanceText.setText(balanceText.getText() + " You don't have enough money to buy this property");
		}

		layoutBoundsProperty().addListener((observable, oldVal, newVal) -> adjustSize());
	}

	public void adjustSize() {
		double height = getHeight();
		double width = getWidth();

		double deedWidth = Math.min(width * 0.9, height * 0.7 * 2 / 3);
		double deedHeight = Math.min(height * 0.7, width * 0.9 * 3 / 2);
		deedCard.setMaxWidth(deedWidth);
		deedCard.setMaxHeight(deedHeight);

		buyButton.setMaxWidth(deedWidth / 2);
		auctionButton.setMaxWidth(deedWidth / 2);

		setFontSizes(width, height);
	}

	private void setFontSizes(double width, double height) {
		UIUtil.fitFont(paneTitle, width * 0.65, height * 0.08);
		UIUtil.fitFont(balanceText, width * 0.9, height * 0.04);
		UIUtil.fitFont(infoText, width * 0.9, height * 0.03);

		UIUtil.fitFont(buyButton, width * 0.2, height * 0.03);
		UIUtil.fitFont(auctionButton, width * 0.2, height * 0.03);
	}

	public void displayOwnership(int propertyCount) {
		balanceText.setText("You have " + propertyCount + "more of the same coloured property.");
	}

	@FXML
	private void buy() {
		app.getNetworkManager().buyProperty();
		((GameplayController) app.getMainController()).closePopup();
	}

	@FXML
	private void auction() {
		app.getNetworkManager().auctionProperty();
		((GameplayController) app.getMainController()).closePopup();
	}
}
