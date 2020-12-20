package monopoly.ui.controller.gameplay.board;

import java.io.IOException;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import lombok.Getter;
import monopoly.ui.UIUtil;

/**
 * Controls a corner tile in Monopoly.
 *
 * @author Ziya Mukhtarov
 * @version Dec 16, 2020
 */
public class CornerTile extends MigPane implements Tile {
	private String type;
	@Getter
	private int tileIndex;

	@Getter
	private Token[] tileTokens;

	public CornerTile(@NamedArg("type") String type, @NamedArg("tileIndex") int tileIndex) {
		this.type = type;
		this.tileIndex = tileIndex;
		tileTokens = new Token[6];

		FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/CornerTile.fxml"));
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
		if ("JAIL".equals(type))
			setTileIcon(UIUtil.JAIL);
		else if ("GO_TO_JAIL".equals(type))
			setTileIcon(UIUtil.GO_TO_JAIL);
		else if ("PARKING".equals(type))
			setTileIcon(UIUtil.FREE_PARKING);
		else
			setTileIcon(UIUtil.GO);
	}

	private void setTileIcon(Image icon) {
		setBackground(new Background(new BackgroundImage(icon, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER, new BackgroundSize(1, 1, true, true, false, false))));
	}

	@Override
	public Point2D getTokenLocation(int tokenNum) {
		double width = getWidth();
		double height = getHeight();
		double tokenRadius = width * 0.1;

		double verticalGap = (height - 4 * tokenRadius) / 3;
		double horisontalGap = (width - 6 * tokenRadius) / 4;

		Point2D tokensMinCoordinateInBoard = localToParent(Point2D.ZERO);

		int colNum = tokenNum % 3;
		double relativeX = horisontalGap + colNum * (2 * tokenRadius + horisontalGap) + tokenRadius;
		int rowNum = tokenNum / 3;
		double relativeY = verticalGap + rowNum * (2 * tokenRadius + verticalGap) + tokenRadius;

		double x = tokensMinCoordinateInBoard.getX() + relativeX;
		double y = tokensMinCoordinateInBoard.getY() + relativeY;
		return new Point2D(x, y);
	}
}
