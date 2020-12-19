package monopoly.ui.controller.gameplay;

import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import monopoly.ui.UIUtil;
import org.tbee.javafx.scene.layout.fxml.*;

import javax.swing.text.html.ImageView;
import java.io.IOException;

public class RailroadTitleDeedPane extends MigPane implements DeedCard {

    @FXML
    private MigPane topWrapper;
    @FXML
    private Text tileName;
    @FXML
    private Text rentText;
    @FXML
    private Text rentValueText;
    @FXML
    private Text twoRailroadsText;
    @FXML
    private Text twoRailroadsValue;
    @FXML
    private Text threeRailroadsText;
    @FXML
    private Text threeRailroadsValue;
    @FXML
    private Text fourRailroadsText;
    @FXML
    private Text fourRailroadsValue;
    @FXML
    private Text mortgageValue;

    private String tileNameString;
    private String rentString;
    private String oneRailroadRent;
    private String twoRailroadRent;
    private String threeRailroadRent;
    private String fourRailroadRent;
    private String mortgageString;

    public RailroadTitleDeedPane(@NamedArg("tileName") String tileName, @NamedArg("rent") String rent,
                                 @NamedArg("twoRailroadRent") String twoRailroadRent,
                                 @NamedArg("threeRailroadRent") String threeRailroadRent,
                                 @NamedArg("fourRailroadRent") String fourRailroadRent,
                                 @NamedArg("mortgage") String mortgage) {

        FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/RailroadTitleDeedPane.fxml"));
        loader.setController(this);
        loader.setRoot(this);

        tileNameString = tileName;
        rentString = rent;
        this.oneRailroadRent = oneRailroadRent;
        this.twoRailroadRent = twoRailroadRent;
        this.threeRailroadRent = threeRailroadRent;
        this.fourRailroadRent = fourRailroadRent;
        mortgageString = mortgage;

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        tileName.setText(tileNameString);
        topWrapper.setBackground(new Background(new BackgroundImage(UIUtil.BOARD, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(0.6,0.8, true, true, false, false))));

        rentValueText.setText(rentString);
        twoRailroadsValue.setText(twoRailroadRent);
        threeRailroadsValue.setText(threeRailroadRent);
        fourRailroadsValue.setText(fourRailroadRent);
        mortgageValue.setText("Mortgage: " + mortgageString);

        layoutBoundsProperty().addListener((observable, oldVal, newVal) -> Platform.runLater(this::adjustSize));

    }

    private void adjustSize() {

        double width = getWidth();
        double height = getHeight();

        UIUtil.fitFont(tileName, width * 0.25, height * 0.1);

        UIUtil.fitFont(rentText, width * 0.12, height * 0.2);
        UIUtil.fitFont(rentValueText, width * 0.1, height * 0.05);

        UIUtil.fitFont(twoRailroadsText, width * 0.6, height * 0.04);
        UIUtil.fitFont(twoRailroadsValue,width * 0.1, height * 0.05);

        UIUtil.fitFont(threeRailroadsText, width * 0.6, height * 0.04);
        UIUtil.fitFont(threeRailroadsValue, width * 0.1, height * 0.05);

        UIUtil.fitFont(fourRailroadsText, width * 0.6, height * 0.04);
        UIUtil.fitFont(fourRailroadsValue, width * 0.1, height * 0.05);

        UIUtil.fitFont(mortgageValue, width * 0.5, height * 0.04);

    }

    @Override
    public String getName() {
        return tileNameString;
    }
}
