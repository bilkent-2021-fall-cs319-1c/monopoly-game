package monopoly.ui.controller.gameplay;

import java.io.IOException;
import java.util.stream.Stream;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import lombok.Setter;
import monopoly.ui.ClientApplication;
import monopoly.ui.UIUtil;
import monopoly.network.packet.important.packet_data.gameplay.PlayerPacketData;

public class TitleDeedActionPane extends MigPane {
	@Setter
	private ClientApplication app;

	@FXML
	private MigPane buttonGroup;
	@FXML
	private MigPane addDeedCard;
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

	private DeedCard deedCard;

	public TitleDeedActionPane(@NamedArg("deedCard") DeedCard deedCard) {

		FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/TitleDeedActionPane.fxml"));
		loader.setController(this);
		loader.setRoot(this);

		this.deedCard = deedCard;

		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Stream.of(widthProperty(), heightProperty())
				.forEach(p -> p.addListener((observable, oldVal, newVal) -> adjustSize()));
	}

	public void initialize() {
		addDeedCard.getChildren().add((Node) deedCard);
	}

	public void adjustSize() {
		double height = getHeight();
		double width = getWidth();

		double deedWidth = Math.min(width, height * 0.75 * 2 / 3);
		double deedHeight = Math.min(height * 0.75, width * 3 / 2);
		addDeedCard.setMaxWidth(deedWidth);
		addDeedCard.setMaxHeight(deedHeight);
		
		buyButton.setMaxWidth(deedWidth / 2);
		auctionButton.setMaxWidth(deedWidth / 2);

		setFontSizes(width, height);
	}

	private void setFontSizes(double width, double height) {
		UIUtil.fitFont(paneTitle, width * 0.65, height * 0.08);
		UIUtil.fitFont(balanceText, width * 0.4, height * 0.06);
		UIUtil.fitFont(infoText, width * 0.2, height * 0.03);

		UIUtil.fitFont(buyButton, width * 0.2, height * 0.03);
		UIUtil.fitFont(auctionButton, width * 0.2, height * 0.03);
	}

	public void displayBalance() {
	}

	public void displayOwnership(int propertyCount) {
		balanceText.setText("You have " + propertyCount + "more of the same coloured property.");
	}

}
