package monopoly.ui.controller.gameplay.board;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import lombok.Getter;
import monopoly.network.packet.important.packet_data.gameplay.property.TilePacketData;
import monopoly.ui.UIUtil;
import monopoly.ui.controller.gameplay.GameplayDataManager;

/**
 * Constructs the Monopoly board.
 *
 * @author Ziya Mukhtarov, Ege Kaan Gürkan
 * @version Dec 19, 2020
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

	@Getter
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
		layoutBoundsProperty().addListener((observable, oldVal, newVal) -> Platform.runLater(this::adjustSize));
	}

	public void buildBoard(GameplayDataManager gameData) {
		buildTiles(gameData.getBoardData().getTiles());
		addTiles();

		// Create Player Tokens
		gameData.getPlayerPanes().forEach(player -> {
			Token token = new Token(player.getColor());
			tokens.add(token);
			player.setToken(token);
		});

		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			token.setBoard(this);
			go.add(token);
			go.getTileTokens()[i] = token;
			token.setCurrentTile(go);
		}
	}

	private void buildTiles(List<TilePacketData> tileDataList) {
		for (int i = 0; i < tileDataList.size(); i++) {
			TilePacketData tileData = tileDataList.get(i);

			if (i == 0) {
				tiles.add(go);
			} else if (i == 10) {
				tiles.add(jail);
			} else if (i == 20) {
				tiles.add(freeParking);
			} else if (i == 30) {
				tiles.add(gotoJail);
			} else {
				tiles.add(new SideTile(tileData));
			}
		}
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
}
