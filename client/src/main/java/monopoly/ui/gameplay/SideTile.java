package monopoly.ui.gameplay;

import java.io.IOException;
import java.util.stream.Stream;

import javafx.scene.image.Image;
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
 * Models a tile in Monopoly.
 *
 * @author Ziya Mukhtarov, Ege Kaan Gürkan
 * @version Dec 13, 2020
 */

public class SideTile extends MigPane {

    @FXML
    private MigPane root;
    @FXML
    private MigPane topWrapper;
    @FXML
    private Text tileTitle;
    @FXML
    private Label tileValue;
    @FXML
    private MigPane pawns;
    private String tileColor;
    private String tileTitleString;
    private String tileValueString;
    private TileType tileType;
    public SideTile(@NamedArg("tileColor") String tileColor, @NamedArg("tileTitle") String tileTitle,
                    @NamedArg("tileValue") String tileValue, @NamedArg("tileType") TileType tileType) {
        FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/SideTile.fxml"));
        loader.setController(this);
        loader.setRoot(this);

        this.tileColor = tileColor;
        this.tileTitleString = tileTitle;
        this.tileValueString = tileValue;
        this.tileType = tileType;

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
        //tileTitle.setFont(Font.font(tileTitle.getFont().getFamily(), FontWeight.BOLD, 20));
        tileValue.setText(tileValueString);

        if (tileType == TileType.RAILROAD) {
            remove(topWrapper);
            setRows("7%[20%]7%[fill]push[5%, center]0");
            pawns.setVisible(true);
            //pawns.setStyle("-fx-background-color: #b50404");
            pawns.setBackground(new Background(new BackgroundImage(UIUtil.TRAIN, BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));

        } else if (tileType == TileType.UTILITY) {
            remove(topWrapper);
            pawns.setVisible(true);
            pawns.setStyle("-fx-background-color: deepskyblue");
            setRows("7%[20%]7%[fill, grow]push[5%, center]0");

        } else if (tileType == TileType.CHANCE_CHEST) {
            remove(topWrapper);
            remove(tileValue);
            setRows("7%[20%]7%[fill, grow]0");
            pawns.setVisible(true);
            //pawns.setStyle("-fx-background-color: darkolivegreen");
            pawns.setBackground(new Background(new BackgroundImage("CHANCE".equals(tileTitleString) ? UIUtil.QUESTION_MARK : UIUtil.CHEST, BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                    new BackgroundSize(0.7, 0.8, true,
                            true, false, false))));
        }

        if ("KENTUCKY\nAVENUE".equals(tileTitleString) || "PACIFIC\nAVENUE".equals(tileTitleString) || "ST. CHARLES\nPLACE".equals(tileTitleString)) {
            root.setStyle("-fx-border-width: 3px 3px 3px 3px; -fx-border-color: black");
        }
    }

    private void adjustSize() {
        double width = getWidth();
        double height = getHeight();
        System.out.println("title length: " + tileTitleString.length());
        /*if (tileTitleString.length() < 10) {
            tileTitle.setFont(Font.font(20));
            //UIUtil.fitFont(tileTitle, width, height * 0.1);
        } else {
            UIUtil.fitFont(tileTitle, width, height * 0.2);
        }*/
        //UIUtil.fitFont(tileTitle, width, height * 0.2);
        tileTitle.setFont(Font.font(5));
        tileValue.setFont(Font.font(5));
        //UIUtil.fitFont(tileValue, width, height * 0.11);
    }

    enum TileType {STREET, RAILROAD, UTILITY, CHANCE_CHEST}
}
