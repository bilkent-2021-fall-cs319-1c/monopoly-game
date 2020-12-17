package monopoly.ui.gameplay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import monopoly.ui.UIUtil;

/**
 * Constructs the Monopoly board.
 *
 * @author Ziya Mukhtarov, Ege Kaan Gürkan
 * @version Dec 13, 2020
 */
public class Board extends MigPane {
	@FXML
	private MigPane topSideTiles;
	@FXML
	private MigPane leftSideTiles;
	@FXML
	private MigPane bottomSideTiles;
	@FXML
	private MigPane rightSideTiles;
	@FXML
	private CornerTile freeParking;
	@FXML
	private CornerTile go;
	@FXML
	private CornerTile jail;
	@FXML
	private CornerTile gotoJail;

	private List<Tile> tiles;
	private List<Token> tokens;

	/**
	 * Constructs a Board object
	 */
	public Board() {
		tiles = new ArrayList<>();
		tokens = new ArrayList<>();

		FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/Board.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void initialize() {
		loadTestData();
		addTiles();

		layoutBoundsProperty().addListener((observable, oldVal, newVal) -> Platform.runLater(this::adjustSize));
	}

	private void addTiles() {
		for (int i = tiles.size() - 1; i >= 0; i--) {
			if (i > 0 && i < 10)
				bottomSideTiles.add((Node) tiles.get(i));
			else if (i > 10 && i < 20)
				leftSideTiles.add((Node) tiles.get(i));
			else if (i > 20 && i < 30)
				topSideTiles.add((Node) tiles.get(i));
			else if (i > 30)
				rightSideTiles.add((Node) tiles.get(i));
		}
	}

	public Tile getNextTile(Tile tile) {
		int index = (tiles.indexOf(tile) + 1) % tiles.size();
		return tiles.get(index);
	}

	/**
	 * Makes certain objects like labels and images responsive.
	 */
	private void adjustSize() {
		double width = getWidth();
		double height = getHeight();

		leftSideTiles.setTranslateX(-width * 0.87 / 2);
		leftSideTiles.setTranslateY(height * (0.13 / 2 + 0.08));
		rightSideTiles.setTranslateX(width * 0.87 / 2);
		rightSideTiles.setTranslateY(-height * (0.13 / 2 + 0.08));

		new Thread(() -> tokens.forEach(token -> Platform.runLater(() -> {
			token.fixPosition();
			token.setRadius(width * 0.74 * 0.11 * 0.1);
		}))).start();
	}

	// JUST FOR TESTING. EVERYTHING BELOW WILL BE DELETED
	private void loadTestData() {
		tiles.add(go);
		for (int i = 0; i < bottomTileTitles.length; i++) {
			tiles.add(new SideTile(bottomTileColours[i], bottomTileTitles[i], bottomTileValues[i], bottomTileTypes[i]));
		}
		tiles.add(jail);
		for (int i = 0; i < leftTileTitles.length; i++) {
			tiles.add(new SideTile(leftTileColours[i], leftTileTitles[i], leftTileValues[i], leftTileTypes[i]));
		}
		tiles.add(freeParking);
		for (int i = 0; i < topTileTitles.length; i++) {
			tiles.add(new SideTile(topTileColours[i], topTileTitles[i], topTileValues[i], topTileTypes[i]));
		}
		tiles.add(gotoJail);
		for (int i = 0; i < rightTileTitles.length; i++) {
			tiles.add(new SideTile(rightTileColours[i], rightTileTitles[i], rightTileValues[i], rightTileTypes[i]));
		}

		tokens.add(new Token(Color.RED));
		tokens.add(new Token(Color.GREEN));
		tokens.add(new Token(Color.BLUE));
		tokens.add(new Token(Color.ORANGE));
		tokens.add(new Token(Color.YELLOW));
		tokens.add(new Token(Color.GRAY));

		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			token.setBoard(this);
			go.add(token);
			go.getTileTokens()[i] = token;
			token.setCurrentTile(go);

			token.setOnMouseClicked(e -> token.moveToNext());
		}
	}

	String regularTile = "STREET";
	String stationTile = "RAILROAD";
	String chanceChestTile = "CHANCE_CHEST";
	String utilityTile = "UTILITY";

	// Bottom tile attributes
	String[] bottomTileTitles = { "CONNECTICUT\nAVENUE", "VERMONT\nAVENUE", "CHANCE", "ORIENTAL\nAVENUE",
			"READING\nRAILROAD", "INCOME\nTAX", "BALTIC\nAVENUE", "COMMUNITY\nCHEST", "MEDITER-\nRANEAN\nAVENUE" };
	String[] bottomTileTypes = { regularTile, regularTile, chanceChestTile, regularTile, stationTile, utilityTile,
			regularTile, chanceChestTile, regularTile };
	String[] bottomTileColours = { "#b1e1fc", "#b1e1fc", "transparent", "#b1e1fc", "transparent", "transparent",
			"#8d5234", "transparent", "#8d5234" };
	String[] bottomTileValues = { "M120", "M100", "m", "M100", "M200", "PAY M200", "M60", "", "M60" };

	// Top tile attributes
	String[] topTileTitles = { "MARVEN\nGARDENS", "WATER\nWORKS", "VENTNOR\nAVENUE", "ATLANTIC\nAVENUE",
			"B. & O.\nRAILROAD", "ILLINOIS\nAVENUE", "INDIANA\nAVENUE", "CHANCE", "KENTUCKY\nAVENUE" };
	String[] topTileTypes = { regularTile, utilityTile, regularTile, regularTile, stationTile, regularTile, regularTile,
			chanceChestTile, regularTile };
	String[] topTileColours = { "yellow", "", "yellow", "yellow", "", "red", "red", "", "red" };
	String[] topTileValues = { "M280", "M150", "M260", "M260", "M200", "M240", "M220", "", "M220" };

	// Left tile attributes
	String[] leftTileTitles = { "NEW YORK\nAVENUE", "TENNESSEE\nAVENUE", "COMMUNITY\nCHEST", "ST. JAMES\nPLACE",
			"PENNNSYLVANIA\nROAD", "VIRGINIA\nAVENUE", "STATES\nAVENUE", "ELECTRIC\nCOMPANY", "ST. CHARLES\nPLACE" };
	String[] leftTileTypes = { regularTile, regularTile, chanceChestTile, regularTile, stationTile, regularTile,
			regularTile, utilityTile, regularTile };
	String[] leftTileColours = { "orange", "orange", "", "orange", "", "#C81A80", "#C81A80", "", "#C81A80" };
	String[] leftTileValues = { "M200", "M180", "", "M180", "M200", "M160", "M140", "M150", "M140" };

	// Right tile attributes
	String[] rightTileTitles = { "BOARDWALK", "LUXURY\nTAX", "PARK\nPLACE", "CHANCE", "SHORT\nLINE",
			"PENNSYLVANIA\nAVENUE", "COMMUNITY\nCHEST", "NORTH\nCAROLINA\nAVENUE", "PACIFIC\nAVENUE" };
	String[] rightTileTypes = { regularTile, utilityTile, regularTile, chanceChestTile, stationTile, regularTile,
			chanceChestTile, regularTile, regularTile };
	String[] rightTileColours = { "#0076C1", "", "#0076C1", "", "", "darkgreen", "", "darkgreen", "darkgreen" };
	String[] rightTileValues = { "M400", "PAY M100", "M350", "", "M200", "M320", "", "M300", "M300" };
}
