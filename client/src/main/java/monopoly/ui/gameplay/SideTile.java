package monopoly.ui.gameplay;

import java.io.IOException;
import java.util.stream.Stream;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import monopoly.ui.UIUtil;

/**
 * Models a tile in Monopoly.
 *
 * @author Ziya Mukhtarov, Ege Kaan Gürkan
 * @version Dec 16, 2020
 */

public class SideTile extends MigPane {
	@FXML
	private MigPane topWrapper;
	@FXML
	private Text tileTitle;
	@FXML
	private Text tileValue;
	@FXML
	private MigPane pawns;

	private String tileColor;
	private String tileTitleString;
	private String tileValueString;
	private String tileType;

	private int tileTitleLineCount;

	public SideTile(@NamedArg("tileColor") String tileColor, @NamedArg("tileTitle") String tileTitle,
			@NamedArg("tileValue") String tileValue, @NamedArg("tileType") String tileType) {
		FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/SideTile.fxml"));
		loader.setController(this);
		loader.setRoot(this);

		this.tileColor = (tileColor != null ? tileColor : "red");
		this.tileTitleString = tileTitle;
		this.tileValueString = tileValue;
		this.tileType = tileType;

		tileTitleLineCount = tileTitleString.split("\n").length;

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
		if (!"".equals(tileColor))
			topWrapper.setBackground(new Background(new BackgroundFill(Color.valueOf(tileColor), null, null)));
		tileTitle.setText(tileTitleString);
		tileValue.setText(tileValueString);

		if ("RAILROAD".equals(tileType)) {
			topWrapper.setVisible(false);
			setComponentConstraints(pawns, "spany 2");
			setTileIcon(UIUtil.TRAIN);

		} else if ("UTILITY".equals(tileType)) {
			topWrapper.setVisible(false);
			setComponentConstraints(pawns, "spany 2");
			setTileIcon(UIUtil.QUESTION_MARK);

		} else if ("CHANCE_CHEST".equals(tileType)) {
			topWrapper.setVisible(false);
			tileValue.setVisible(false);
			setComponentConstraints(pawns, "spany 3");
			setTileIcon("CHANCE".equals(tileTitleString) ? UIUtil.QUESTION_MARK : UIUtil.CHEST);
		}
	}

	private void setTileIcon(Image icon) {
		pawns.setBackground(
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
}
