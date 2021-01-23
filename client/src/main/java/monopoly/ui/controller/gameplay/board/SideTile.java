package monopoly.ui.controller.gameplay.board;

import java.io.IOException;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import lombok.Getter;
import monopoly.network.packet.important.packet_data.gameplay.property.StreetTitleDeedPacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.TilePacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.TileType;
import monopoly.network.packet.important.packet_data.gameplay.property.TitleDeedPacketData;
import monopoly.ui.UIUtil;
import monopoly.ui.controller.gameplay.PlayerPane;
import monopoly.ui.controller.gameplay.titledeed.DeedCard;
import monopoly.ui.controller.gameplay.titledeed.RailroadTitleDeedPane;
import monopoly.ui.controller.gameplay.titledeed.StreetTitleDeedPane;
import monopoly.ui.controller.gameplay.titledeed.UtilityTitleDeedPane;

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
	private MigPane tileValueWrapper;
	@FXML
	private MigPane tokens;

	private TitleDeedPacketData titleDeedData;
	private Pane tileInfoContainer;

	private String tileColor;
	private String tileTitleString;
	private String tileValueString;
	private TileType tileType;
	@Getter
	private int tileIndex;

	private int tileTitleLineCount;

	@Getter
	private Token[] tileTokens;

	public SideTile(String tileColor, String tileTitle, String tileValue, TileType tileType, int tileIndex,
			Pane tileInfoContainer) {
		this.tileColor = (tileColor != null ? tileColor : "red");
		this.tileTitleString = tileTitle;
		this.tileValueString = tileValue;
		this.tileType = tileType;
		this.tileIndex = tileIndex;
		this.tileInfoContainer = tileInfoContainer;

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

	public SideTile(TilePacketData tileData, Pane tileInfoContainer) {
		this("", tileData.getTitle(), tileData.getDescription(), tileData.getType(), tileData.getIndex(),
				tileInfoContainer);
		titleDeedData = tileData.getTitleDeed();

		if (titleDeedData instanceof StreetTitleDeedPacketData) {
			StreetTitleDeedPacketData streetTitleDeed = (StreetTitleDeedPacketData) titleDeedData;
			tileColor = streetTitleDeed.getColor();
			topWrapper.setBackground(new Background(new BackgroundFill(Color.valueOf(tileColor), null, null)));
		}
	}

	@FXML
	public void initialize() {
		tileTitle.setText(tileTitleString);
		tileValue.setText(tileValueString);

		switch (tileType) {
		case RAILROAD:
			topWrapper.setVisible(false);
			setComponentConstraints(tokens, "spany 2");
			setTileIcon(UIUtil.TRAIN);
			break;
		case UTILITY:
			topWrapper.setVisible(false);
			setComponentConstraints(tokens, "spany 2");
			setTileIcon("ELECTRIC\nCOMPANY".equals(tileTitleString) ? UIUtil.LIGHTBULB : UIUtil.FAUCET);
			break;
		case TAX:
			topWrapper.setVisible(false);
			setComponentConstraints(tokens, "spany 2");
			break;
		case CHANCE:
		case COMMUNITY_CHEST:
			topWrapper.setVisible(false);
			tileValueWrapper.setVisible(false);
			setComponentConstraints(tokens, "spany 3");
			setTileIcon(tileType == TileType.CHANCE ? UIUtil.QUESTION_MARK : UIUtil.CHEST);
			break;
		default:
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

		if (tileValueWrapper.isVisible())
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

	public void setOwner(PlayerPane playerPaneOfPlayer) {
		Platform.runLater(() -> tileValueWrapper
				.setBackground(new Background(new BackgroundFill(playerPaneOfPlayer.getColor(), null, null))));
	}

	private volatile boolean mouseOnTile;
	private volatile boolean mouseOnPopup;

	@FXML
	private void mouseEntered() {
		mouseOnTile = true;
		mouseOnPopup = false;
		showInfoPopup();
	}

	@FXML
	private void mouseExited() {
		mouseOnTile = false;
		checkAndClosePopup();
	}

	Pane infoPopup;

	private void showInfoPopup() {
		if (infoPopup != null) {
			return;
		}
		infoPopup = createInfoPopup();
		if (infoPopup == null) {
			return;
		}

		Board board = (Board) getParent().getParent();
		double popupWidth = board.getWidth() * 0.2;
		double popupHeight = board.getHeight() * 0.4;
		infoPopup.setPrefWidth(popupWidth);
		infoPopup.setPrefHeight(popupHeight);
		infoPopup.setMaxWidth(popupWidth);
		infoPopup.setMaxHeight(popupHeight);
		infoPopup.setMinWidth(popupWidth);
		infoPopup.setMinHeight(popupHeight);
		infoPopup.setOnMouseEntered(e -> mouseOnPopup = true);
		infoPopup.setOnMouseExited(e -> {
			mouseOnPopup = false;
			checkAndClosePopup();
		});
		tileInfoContainer.getChildren().add(infoPopup);

		Rotate rotate = new Rotate(0);
		// PopupRotation = TileRotation + BoardRotation
		rotate.angleProperty().bind(Bindings.add(getParent().rotateProperty(), board.rotateProperty()));
		infoPopup.getTransforms().add(rotate);

		// point is top center point of the tile translated by the popupHeight
		Point2D point = localToScene(new Point2D((getWidth() - popupWidth) / 2, -popupHeight));
		infoPopup.relocate(point.getX(), point.getY());
	}

	private void checkAndClosePopup() {
		new Thread(() -> {
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			if (!mouseOnPopup && !mouseOnTile)
				Platform.runLater(() -> {
					tileInfoContainer.getChildren().remove(infoPopup);
					infoPopup = null;
				});
		}).start();
	}

	private Pane createInfoPopup() {
		DeedCard deedCard;
		switch (tileType) {
		case STREET:
			StreetTitleDeedPacketData streetTitleDeedData = (StreetTitleDeedPacketData) titleDeedData;
			deedCard = new StreetTitleDeedPane(streetTitleDeedData);
			break;
		case RAILROAD:
			deedCard = new RailroadTitleDeedPane(titleDeedData);
			break;
		case UTILITY:
			deedCard = new UtilityTitleDeedPane(titleDeedData);
			break;
		default:
			deedCard = null;
		}
		return (Pane) deedCard;
	}
}
