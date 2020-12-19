package monopoly.ui.controller.gameplay;

import java.io.IOException;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
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

	private String tileColor;
	private String tileTitleString;
	private String rentString;
	private String colourSetRentString;
	private String oneHouseRentString;
	private String twoHouseRentString;
	private String threeHouseRentString;
	private String fourHouseRentString;
	private String hotelRentString;
	private String houseCostString;
	private String hotelCostString;

	public StreetTitleDeedPane(@NamedArg("tileColor") String tileColor, @NamedArg("tileTitle") String tileTitle,
			@NamedArg("rent") String rent, @NamedArg("colourSetRent") String colourSetRent,
			@NamedArg("oneHouseRent") String oneHouseRent, @NamedArg("twoHouseRent") String twoHouseRent,
			@NamedArg("threeHouseRent") String threeHouseRent, @NamedArg("fourHouseRent") String fourHouseRent,
			@NamedArg("hotelRent") String hotelRent, @NamedArg("houseCost") String houseCost,
			@NamedArg("hotelCost") String hotelCost) {

		FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/StreetTitleDeedPane.fxml"));
		loader.setController(this);
		loader.setRoot(this);

		this.tileColor = tileColor;
		tileTitleString = tileTitle;
		rentString = rent;
		colourSetRentString = colourSetRent;
		oneHouseRentString = oneHouseRent;
		twoHouseRentString = twoHouseRent;
		threeHouseRentString = threeHouseRent;
		fourHouseRentString = fourHouseRent;
		hotelRentString = hotelRent;
		houseCostString = houseCost;
		hotelCostString = hotelCost;

		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void initialize() {

		topWrapper.setStyle("-fx-background-color: " + tileColor + "; -fx-border-color: black; -fx-border-width: 3px");

		tileTitle.setText(tileTitleString);
		rentValue.setText(rentString);
		colourSetRentValue.setText(colourSetRentString);
		oneHouseRentValue.setText(oneHouseRentString);
		twoHouseRentValue.setText(twoHouseRentString);
		threeHouseRentValue.setText(threeHouseRentString);
		fourHouseRentValue.setText(fourHouseRentString);
		hotelRentValue.setText(hotelRentString);
		housesCostValue.setText(houseCostString);
		hotelsCostValue.setText(hotelCostString);

		setStyle(" -fx-background-color: white; -fx-border-color: black; -fx-border-width: 3px");

		layoutBoundsProperty().addListener((observable, oldVal, newVal) -> Platform.runLater(this::adjustSize));
	}

	private void adjustSize() {
		double width = getWidth();
		double height = getHeight();

		double rightColumnWidth = width * 0.3;
		double leftHeight = height * 0.04;
		double valuesHeight = height * 0.035;

		UIUtil.fitFont(titleDeedText, width * 0.9, topWrapper.getHeight() * 0.2);
		UIUtil.fitFont(tileTitle, width * 0.7, topWrapper.getHeight() * 0.6);

		UIUtil.fitFont(rent, width * 0.6, leftHeight);
		UIUtil.fitFont(rentValue, rightColumnWidth, valuesHeight);

		UIUtil.fitFont(colourSetRent, width * 0.6, leftHeight);
		UIUtil.fitFont(colourSetRentValue, rightColumnWidth, valuesHeight);

		UIUtil.fitFont(oneHouseRent, width * 0.2, leftHeight);
		UIUtil.fitFont(oneHouseRentValue, rightColumnWidth, valuesHeight);

		UIUtil.fitFont(twoHouseRent, width * 0.2, leftHeight);
		UIUtil.fitFont(twoHouseRentValue, rightColumnWidth, valuesHeight);

		UIUtil.fitFont(threeHouseRent, width * 0.2, leftHeight);
		UIUtil.fitFont(threeHouseRentValue, rightColumnWidth, valuesHeight);

		UIUtil.fitFont(fourHouseRent, width * 0.2, leftHeight);
		UIUtil.fitFont(fourHouseRentValue, rightColumnWidth, valuesHeight);

		UIUtil.fitFont(hotelRent, width * 0.2, leftHeight);
		UIUtil.fitFont(hotelRentValue, rightColumnWidth, valuesHeight);

		UIUtil.fitFont(housesCost, width * 0.6, leftHeight);
		UIUtil.fitFont(housesCostValue, rightColumnWidth, valuesHeight);

		UIUtil.fitFont(hotelsCost, width * 0.6, leftHeight);
		UIUtil.fitFont(hotelsCostValue, rightColumnWidth, valuesHeight);
		UIUtil.fitFont(plusFourHousesText, rightColumnWidth, height * 0.025);

		double imageHeight = height * 0.05;
		oneHouse.setFitHeight(imageHeight);
		twoHouses.setFitHeight(imageHeight);
		threeHouses.setFitHeight(imageHeight);
		fourHouses.setFitHeight(imageHeight);
		hotel.setFitHeight(imageHeight);
	}

	public String getName() {
		return tileTitleString;
	}

}
