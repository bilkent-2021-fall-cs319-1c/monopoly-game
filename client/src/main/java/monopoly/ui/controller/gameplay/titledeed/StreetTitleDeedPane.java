package monopoly.ui.controller.gameplay.titledeed;

import java.io.IOException;
import java.util.List;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import lombok.Getter;
import monopoly.network.packet.important.packet_data.gameplay.property.StreetTitleDeedPacketData;
import monopoly.ui.UIUtil;

/**
 * Models a street deed card in Monopoly.
 *
 * @author Ege Kaan Gürkan
 * @version Dec 17, 2020
 */
public class StreetTitleDeedPane extends MigPane implements DeedCard {
	@FXML
	private MigPane root;
	@FXML
	private MigPane topWrapper;
	@FXML
	private Text titleDeedText;
	@FXML
	private Text tileTitle;
	@FXML
	private Text rent;
	@FXML
	private Text colourSetRent;
	@FXML
	private Text colourSetRentValue;
	@FXML
	private Text oneHouseRent;
	@FXML
	private Text oneHouseRentValue;
	@FXML
	private Text twoHouseRent;
	@FXML
	private Text twoHouseRentValue;
	@FXML
	private Text threeHouseRent;
	@FXML
	private Text threeHouseRentValue;
	@FXML
	private Text fourHouseRent;
	@FXML
	private Text fourHouseRentValue;
	@FXML
	private Text hotelRent;
	@FXML
	private Text hotelRentValue;
	@FXML
	private Text plusFourHousesText;
	@FXML
	private Text housesCost;
	@FXML
	private Text housesCostValue;
	@FXML
	private Text hotelsCost;
	@FXML
	private Text hotelsCostValue;
	@FXML
	private Text rentValue;
	@FXML
	private ImageView oneHouse;
	@FXML
	private ImageView twoHouses;
	@FXML
	private ImageView threeHouses;
	@FXML
	private ImageView fourHouses;
	@FXML
	private ImageView hotel;

	@Getter
	private String name;
	private String tileColor;
	private String rentString;
	private String colourSetRentString;
	private String oneHouseRentString;
	private String twoHouseRentString;
	private String threeHouseRentString;
	private String fourHouseRentString;
	private String hotelRentString;
	private String houseCostString;
	private String hotelCostString;

	public StreetTitleDeedPane(StreetTitleDeedPacketData deedData) {
		tileColor = deedData.getColor();
		name = deedData.getTitle();
		houseCostString = "" + deedData.getHousePrice();
		hotelCostString = "" + deedData.getHotelPrice();

		List<Integer> rentCost = deedData.getRentCost();
		rentString = "" + rentCost.get(0);
		colourSetRentString = "" + rentCost.get(1);
		oneHouseRentString = "" + rentCost.get(2);
		twoHouseRentString = "" + rentCost.get(3);
		threeHouseRentString = "" + rentCost.get(4);
		fourHouseRentString = "" + rentCost.get(5);
		hotelRentString = "" + rentCost.get(6);

		FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/StreetTitleDeedPane.fxml"));
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
		topWrapper.setStyle("-fx-background-color: " + tileColor + "; -fx-border-color: black; -fx-border-width: 3px");

		tileTitle.setText(name);
		rentValue.setText(rentString);
		colourSetRentValue.setText(colourSetRentString);
		oneHouseRentValue.setText(oneHouseRentString);
		twoHouseRentValue.setText(twoHouseRentString);
		threeHouseRentValue.setText(threeHouseRentString);
		fourHouseRentValue.setText(fourHouseRentString);
		hotelRentValue.setText(hotelRentString);
		housesCostValue.setText(houseCostString);
		hotelsCostValue.setText(hotelCostString);


		layoutBoundsProperty().addListener((observable, oldVal, newVal) -> Platform.runLater(this::adjustSize));
	}

	private void adjustSize() {
		double width = getWidth();
		double height = getHeight();

		double rightColumnWidth = width * 0.3;
		double leftHeight = height * 0.04;
		double valuesHeight = height * 0.035;
		double rentWithWidth = width * 0.25;

		UIUtil.fitFont(titleDeedText, width * 0.9, topWrapper.getHeight() * 0.2);
		UIUtil.fitFont(tileTitle, width * 0.7, topWrapper.getHeight() * 0.6);

		UIUtil.fitFont(rent, width * 0.6, leftHeight);
		UIUtil.fitFont(rentValue, rightColumnWidth, valuesHeight);

		UIUtil.fitFont(colourSetRent, width * 0.6, leftHeight);
		UIUtil.fitFont(colourSetRentValue, rightColumnWidth, valuesHeight);

		UIUtil.fitFont(oneHouseRent, rentWithWidth, leftHeight);
		UIUtil.fitFont(oneHouseRentValue, rightColumnWidth, valuesHeight);

		UIUtil.fitFont(twoHouseRent, rentWithWidth, leftHeight);
		UIUtil.fitFont(twoHouseRentValue, rightColumnWidth, valuesHeight);

		UIUtil.fitFont(threeHouseRent, rentWithWidth, leftHeight);
		UIUtil.fitFont(threeHouseRentValue, rightColumnWidth, valuesHeight);

		UIUtil.fitFont(fourHouseRent, rentWithWidth, leftHeight);
		UIUtil.fitFont(fourHouseRentValue, rightColumnWidth, valuesHeight);

		UIUtil.fitFont(hotelRent, rentWithWidth, leftHeight);
		UIUtil.fitFont(hotelRentValue, rightColumnWidth, valuesHeight);

		UIUtil.fitFont(housesCost, width * 0.6, leftHeight);
		UIUtil.fitFont(housesCostValue, rightColumnWidth, valuesHeight);

		UIUtil.fitFont(hotelsCost, width * 0.6, leftHeight);
		UIUtil.fitFont(hotelsCostValue, rightColumnWidth, valuesHeight);
		UIUtil.fitFont(plusFourHousesText, rightColumnWidth, height * 0.025);

		double imageHeight = height * 0.04;
		oneHouse.setFitHeight(imageHeight);
		twoHouses.setFitHeight(imageHeight);
		threeHouses.setFitHeight(imageHeight);
		fourHouses.setFitHeight(imageHeight);
		hotel.setFitHeight(imageHeight);
	}
}
