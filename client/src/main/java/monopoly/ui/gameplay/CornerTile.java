package monopoly.ui.gameplay;

import java.io.IOException;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import monopoly.ui.UIUtil;

/**
 * Controls a corner tile in Monopoly.
 *
 * @author Ziya Mukhtarov
 * @version Dec 16, 2020
 */
public class CornerTile extends MigPane {
	private String type;

	public CornerTile(@NamedArg("type") String type) {
		this.type = type;

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
}
