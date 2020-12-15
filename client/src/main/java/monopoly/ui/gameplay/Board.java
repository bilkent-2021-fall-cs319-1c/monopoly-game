package monopoly.ui.gameplay;

import java.io.IOException;
import java.util.stream.Stream;

import javafx.scene.image.ImageView;
import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import monopoly.ui.UIUtil;

/**
 * Constructs the Monopoly board.
 *
 * @author Ziya Mukhtarov, Ege Kaan Gürkan
 * @version Dec 13, 2020
 */

public class Board extends MigPane {

    @FXML
    private MigPane topCenter;
    @FXML
    private MigPane leftMiddle;
    @FXML
    private MigPane bottomCenter;
    @FXML
    private MigPane rightMiddle;
    @FXML
    private ImageView freeParking;
    @FXML
    private ImageView go;
    @FXML
    private ImageView visitJail;
    @FXML
    private ImageView gotoJail;

    SideTile.TileType regularTile = SideTile.TileType.STREET;
    SideTile.TileType stationTile = SideTile.TileType.RAILROAD;
    SideTile.TileType chanceChestTile = SideTile.TileType.CHANCE_CHEST;
    SideTile.TileType utilityTile = SideTile.TileType.UTILITY;

    // Bottom tile attributes
    String[] bottomTileTitles = { "CONNECTICUT\nAVENUE", "VERMONT\nAVENUE", "CHANCE", "ORIENTAL\nAVENUE",
            "READING\nRAILROAD", "INCOME\nTAX", "BALTIC\nAVENUE", "COMMUNITY\nCHEST", "MEDITERRANEAN\nAVENUE" };
    SideTile.TileType[] bottomTileTypes = {regularTile, regularTile, chanceChestTile, regularTile, stationTile,
            utilityTile, regularTile, chanceChestTile, regularTile };
    String[] bottomTileColours = { "#b1e1fc", "#b1e1fc", "transparent", "#b1e1fc", "transparent", "transparent",
            "#8d5234", "transparent", "#8d5234" };
    String[] bottomTileValues = { "M120", "M100", "", "M100", "M200", "PAY M200", "M60", "", "M60" };

    // Top tile attributes
    String[] topTileTitles = { "MARVEN\nGARDENS", "WATER\nWORKS", "VENTNOR\nAVENUE", "ATLANTIC\nAVENUE",
            "B. & O.\nRAILROAD", "ILLINOIS\nAVENUE", "INDIANA\nAVENUE", "CHANCE", "KENTUCKY\nAVENUE" };
    SideTile.TileType[] topTileTypes = {regularTile, utilityTile, regularTile, regularTile, stationTile, regularTile,
            regularTile, chanceChestTile,regularTile };
    String[] topTileColours = { "yellow", "", "yellow", "yellow", "", "red", "red", "", "red" };
    String[] topTileValues = { "M280", "M150", "M260", "M260", "M200", "M240", "M220", "", "M220" };

    // Left tile attributes
    String[] leftTileTitles = { "NEW YORK\nAVENUE", "TENNESSEE\nAVENUE", "COMMUNITY\nCHEST", "ST. JAMES\nPLACE",
            "PENNNSYLVANIA\nROAD", "VIRGINIA\nAVENUE", "STATES\nAVENUE", "ELECTRIC\nCOMPANY", "ST. CHARLES\nPLACE"};
    SideTile.TileType[] leftTileTypes = { regularTile, regularTile, chanceChestTile, regularTile, stationTile, regularTile,
            regularTile, utilityTile, regularTile };
    String[] leftTileColours = { "orange", "orange", "", "orange", "", "#C81A80", "#C81A80", "", "#C81A80" };
    String[] leftTileValues = { "M200", "M180", "", "M180", "M200", "M160", "M140", "M150", "M140" };

    // Right tile attributes
    String[] rightTileTitles = { "BOARDWALK", "LUXURY\nTAX", "PARK\nPLACE", "CHANCE", "SHORT\nLINE", "PENNSYLVANIA\nAVENUE",
            "COMMUNITY\nCHEST", "NORTH\nCAROLINA\nAVENUE", "PACIFIC\nAVENUE" };
    SideTile.TileType[] rightTileTypes = { regularTile, utilityTile, regularTile, chanceChestTile, stationTile, regularTile,
            chanceChestTile, regularTile, regularTile};
    String[] rightTileColours = { "#0076C1", "", "#0076C1", "", "", "darkgreen", "", "darkgreen", "darkgreen" };
    String[] rightTileValues = { "M400", "PAY M100", "M350", "", "M200", "M320", "", "M300", "M300" };

    /**
     * Constructs a Board object
     */
    public Board() {
        FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/Board.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stream.of(widthProperty(), heightProperty())
                .forEach(p -> p.addListener((observable, oldVal, newVal) -> adjustSize()));
    }

    @FXML
    private void initialize() {
        for (int i = 0; i < bottomTileTitles.length; i++) {
//			SideTile node = new SideTile(bottomTileColours[i], bottomTileTitles[i], bottomTileValues[i]);
            topCenter.add(new SideTile(topTileColours[i], topTileTitles[i], topTileValues[i], topTileTypes[i]), "grow");
            bottomCenter.add(new SideTile(bottomTileColours[i], bottomTileTitles[i], bottomTileValues[i], bottomTileTypes[i]), "grow");
            rightMiddle.add(new SideTile(rightTileColours[i], rightTileTitles[i], rightTileValues[i], rightTileTypes[i]), "grow");
            leftMiddle.add(new SideTile(leftTileColours[i], leftTileTitles[i], leftTileValues[i], leftTileTypes[i]), "grow");
        }


    }

    /**
     * Makes certain objects like labels and images responsive.
     */

    private void adjustSize() {
        double width = getWidth();
        double height = getHeight();

        System.out.println("Board size: " + width + " x " + height);

        leftMiddle.setTranslateX(-width * 0.87 / 2);
        leftMiddle.setTranslateY(height * (0.13 / 2 + 0.08));
        rightMiddle.setTranslateX(width * 0.87 / 2);
        rightMiddle.setTranslateY(-height * (0.13 / 2 + 0.08));

        freeParking.setFitHeight(height * 0.13);
        freeParking.setFitWidth(width * 0.13);
        go.setFitHeight(height * 0.13);
        go.setFitWidth(width * 0.13);
        visitJail.setFitHeight(height * 0.13);
        visitJail.setFitWidth(width * 0.13);
        gotoJail.setFitHeight(height * 0.13);
        gotoJail.setFitWidth(width * 0.13);

    }
}
