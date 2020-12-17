package monopoly.ui.controller.gameplay;

import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import monopoly.ui.UIUtil;
import org.tbee.javafx.scene.layout.fxml.MigPane;


import java.io.IOException;
import java.util.stream.Stream;

public class UtilitiesTileDeedPane extends MigPane {

    @FXML
    private ImageView image;
    @FXML
    private Text utilityTitle;
    @FXML
    private Text firstPar;
    @FXML
    private Text secondPar;

    private UtilityType utility;

    public UtilitiesTileDeedPane(@NamedArg("utility") UtilityType utility) {

        FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/UtilitiesTileDeedPane.fxml"));
        loader.setController(this);
        loader.setRoot(this);

        this.utility = utility;

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

        setIcon(utility);

    }

    private void adjustSize() {
        double width = getWidth();
        double height = getHeight();

        UIUtil.fitFont(utilityTitle, width, height * 0.07);
        UIUtil.fitFont(firstPar, width, height * 0.7);
        UIUtil.fitFont(secondPar, width, height * 0.7);
        image.setFitWidth(width * 0.6);
    }

    private void setIcon(UtilityType utility) {
        if (utility == UtilityType.ELECTRIC_COMPANY) {
            utilityTitle.setText("ELECTRIC COMPANY");
            image.setImage(UIUtil.LIGHTBULB);
        } else {
            utilityTitle.setText("WATER WORKS");
            image.setImage(UIUtil.FAUCET);
        }
    }

    public enum UtilityType {WATER_WORKS, ELECTRIC_COMPANY}

}
