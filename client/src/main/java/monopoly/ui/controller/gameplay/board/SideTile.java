package monopoly.ui.controller.gameplay.board;

import java.io.IOException;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Getter;
import monopoly.network.packet.important.packet_data.gameplay.property.StreetTitleDeedPacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.TilePacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.TileType;
import monopoly.network.packet.important.packet_data.gameplay.property.TitleDeedPacketData;
import monopoly.ui.UIUtil;

/**
 * Models a tile in Monopoly.
 *
 * @author Ziya Mukhtarov, Ege Kaan Gürkan
 * @version Dec 16, 2020
 */
public class SideTile extends MigPane implements Tile {
	@FXML
	private MigPane topWrapper;
	@FXML
	private Text tileTitle;
	@FXML
	private Text tileValue;
	@FXML
	private MigPane tokens;

	private String tileColor;
	private String tileTitleString;
	private String tileValueString;
	private TileType tileType;

	private int tileTitleLineCount;

	@Getter
	private Token[] tileTokens;

	public SideTile(@NamedArg("tileColor") String tileColor, @NamedArg("tileTitle") String tileTitle,
			@NamedArg("tileValue") String tileValue, @NamedArg("tileType") TileType tileType) {
		this.tileColor = (tileColor != null ? tileColor : "red");
		this.tileTitleString = tileTitle;
		this.tileValueString = tileValue;
		this.tileType = tileType;

		tileTitleLineCount = tileTitleString.split("\n").length;

		tileTokens = new Token[6];

		FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/SideTile.fxml"));
		loader.setController(this);
		loader.setRoot(this);

		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public SideTile(TilePacketData tileData) {
		this("", tileData.getTitle(), tileData.getDescription(), tileData.getType());

		TitleDeedPacketData titleDeed = tileData.getTitleDeed();
		if (titleDeed instanceof StreetTitleDeedPacketData) {
			tileColor = ((StreetTitleDeedPacketData) titleDeed).getColor();
			topWrapper.setBackground(new Background(new BackgroundFill(Color.valueOf(tileColor), null, null)));
		}
	}

	@FXML
	public void initialize() {
		tileTitle.setText(tileTitleString);
		tileValue.setText(tileValueString);

		if (tileType == TileType.RAILROAD) {
			topWrapper.setVisible(false);
			setComponentConstraints(tokens, "spany 2");
			setTileIcon(UIUtil.TRAIN);

		} else if (tileType == TileType.UTILITY) {
			topWrapper.setVisible(false);
			setComponentConstraints(tokens, "spany 2");
			setTileIcon(UIUtil.QUESTION_MARK);

		} else if (tileType == TileType.TAX) {
			topWrapper.setVisible(false);
			setComponentConstraints(tokens, "spany 2");

		} else if (tileType == TileType.CHANCE || tileType == TileType.COMMUNITY_CHEST) {
			topWrapper.setVisible(false);
			tileValue.setVisible(false);
			setComponentConstraints(tokens, "spany 3");
			setTileIcon(tileType == TileType.CHANCE ? UIUtil.QUESTION_MARK : UIUtil.CHEST);
		}

		layoutBoundsProperty().addListener((observable, oldVal, newVal) -> Platform.runLater(this::adjustSize));
	}

	private void setTileIcon(Image icon) {
		tokens.setBackground(
				new Background(new BackgroundImage(icon, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
						BackgroundPosition.CENTER, new BackgroundSize(0.7, 0.8, true, true, false, false))));
	}

	private void adjustSize() {
		double width = getWidth();
		double height = getHeight();

		if (tileTitleLineCount == 1)
			UIUtil.fitFont(tileTitle, width * 0.8, height * 0.24);
		else if (tileTitleLineCount == 2)
			UIUtil.fitFont(tileTitle, width - 5, height * 0.24);
		else
			UIUtil.fitFont(tileTitle, width - 5, height * 0.36);

		if (tileValue.isVisible())
			UIUtil.fitFont(tileValue, width, height * 0.15);
	}

	@Override
	public Point2D getTokenLocation(int tokenNum) {
		double height = tokens.getHeight();
		double width = tokens.getWidth();
		double tokenRadius = width * 0.1;

		double verticalGap = (height - 4 * tokenRadius) / 3;
		double horisontalGap = (width - 6 * tokenRadius) / 4;

		Point2D tokensMinCoordinateInBoard = getParent()
				.localToParent(localToParent(tokens.localToParent(Point2D.ZERO)));
		Point2D tokensMaxCoordinateInBoard = getParent()
				.localToParent(localToParent(tokens.localToParent(new Point2D(width, height))));

		int colNum = tokenNum % 3;
		double relativeX = horisontalGap + colNum * (2 * tokenRadius + horisontalGap) + tokenRadius;
		int rowNum = tokenNum / 3;
		double relativeY = verticalGap + rowNum * (2 * tokenRadius + verticalGap) + tokenRadius;

		int rotation = findRotation(tokensMinCoordinateInBoard, tokensMaxCoordinateInBoard);

		if (rotation == 90 || rotation == 270) {
			double temp = relativeX;
			relativeX = relativeY;
			relativeY = temp;
		}

		double x;
		double y;
		if (rotation == 180 || rotation == 270)
			x = tokensMinCoordinateInBoard.getX() - relativeX;
		else
			x = tokensMinCoordinateInBoard.getX() + relativeX;
		if (rotation == 180 || rotation == 90)
			y = tokensMinCoordinateInBoard.getY() - relativeY;
		else
			y = tokensMinCoordinateInBoard.getY() + relativeY;

		return new Point2D(x, y);
	}

	private int findRotation(Point2D minBound, Point2D maxBound) {
		if (minBound.getX() <= maxBound.getX() && minBound.getY() <= maxBound.getY())
			return 0;
		if (minBound.getX() <= maxBound.getX() && minBound.getY() > maxBound.getY())
			return 90;
		if (minBound.getX() > maxBound.getX() && minBound.getY() > maxBound.getY())
			return 180;
		return 270;
	}
}
