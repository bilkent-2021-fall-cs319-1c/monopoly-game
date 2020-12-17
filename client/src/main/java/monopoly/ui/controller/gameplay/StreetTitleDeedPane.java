package monopoly.ui.controller.gameplay;

import java.io.IOException;
import java.util.stream.Stream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import monopoly.ui.UIUtil;

/**
 * Models a street deed card in Monopoly.
 *
 * @author Ege Kaan Gürkan
 * @version Dec 17, 2020
 */

public class StreetTitleDeedPane extends MigPane {

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

    public StreetTitleDeedPane(@NamedArg("tileColor") String tileColor,
                               @NamedArg("tileTitle") String tileTitle,
                               @NamedArg("rent") String rent,
                               @NamedArg("colourSetRent") String colourSetRent,
                               @NamedArg("oneHouseRent") String oneHouseRent,
                               @NamedArg("twoHouseRent") String twoHouseRent,
                               @NamedArg("threeHouseRent") String threeHouseRent,
                               @NamedArg("fourHouseRent") String fourHouseRent,
                               @NamedArg("hotelRent") String hotelRent,
                               @NamedArg("houseCost") String houseCost,
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

        Stream.of(widthProperty(), heightProperty())
                .forEach(p -> p.addListener((observable, oldVal, newVal) -> adjustSize()));

    }

    @FXML
    public void initialize() {

        topWrapper.setStyle("-fx-background-color: " + tileColor + "; -fx-border-color: black; -fx-border-width: 0px 0px 3px 0px;");

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

    }

    private void adjustSize() {
        double width = getWidth();
        double height = getHeight();
        //System.out.println("title length: " + tileTitleString.length());
        /*if (tileTitleString.length() < 10) {
            tileTitle.setFont(Font.font(20));
            //UIUtil.fitFont(tileTitle, width, height * 0.1);
        } else {
            UIUtil.fitFont(tileTitle, width, height * 0.2);
        }*/
        UIUtil.fitFont(titleDeedText, width, height * 0.04);
        UIUtil.fitFont(tileTitle, width, height * 0.1);
        UIUtil.fitFont(rent, width, height * 0.04);
        UIUtil.fitFont(rentValue, width, height * 0.035);
        UIUtil.fitFont(colourSetRent, width, height * 0.04);
        UIUtil.fitFont(colourSetRentValue, width, height * 0.035);
        UIUtil.fitFont(oneHouseRent, width, height * 0.04);
        UIUtil.fitFont(oneHouseRentValue, width, height * 0.035);
        UIUtil.fitFont(twoHouseRent, width, height * 0.04);
        UIUtil.fitFont(twoHouseRentValue, width, height * 0.035);
        UIUtil.fitFont(threeHouseRent, width, height * 0.04);
        UIUtil.fitFont(threeHouseRentValue, width, height * 0.035);
        UIUtil.fitFont(fourHouseRent, width, height * 0.04);
        UIUtil.fitFont(fourHouseRentValue, width, height * 0.035);
        UIUtil.fitFont(hotelRent, width, height * 0.04);
        UIUtil.fitFont(hotelRentValue, width, height * 0.035);
        UIUtil.fitFont(housesCost, width, height * 0.04);
        UIUtil.fitFont(housesCostValue, width, height * 0.035);
        UIUtil.fitFont(hotelsCost, width, height * 0.04);
        UIUtil.fitFont(hotelsCostValue, width, height * 0.035);
        UIUtil.fitFont(plusFourHousesText, width, height * 0.025);

        oneHouse.setFitHeight(height * 0.05);
        twoHouses.setFitHeight(height * 0.05);
        threeHouses.setFitHeight(height * 0.05);
        fourHouses.setFitHeight(height * 0.05);
        hotel.setFitHeight(height * 0.05);

        //tileTitle.setFont(Font.font(5));
        //tileValue.setFont(Font.font(5));
        //UIUtil.fitFont(tileValue, width, height * 0.11);
    }

}

