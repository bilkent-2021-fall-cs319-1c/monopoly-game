package monopoly.ui.controller.gameplay.board.dice;

import java.io.IOException;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import monopoly.ui.UIUtil;

public class Dice extends MigPane {
	@FXML
	private Die die1;
	@FXML
	private Die die2;

	public Dice() {
		FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/Dice.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		setOnMouseClicked(e -> {
			die1.roll(4);
			die2.roll(4);
		});
	}

	@FXML
	private void initialize() {
		layoutBoundsProperty().addListener((observable, oldVal, newVal) -> Platform.runLater(this::adjustSize));
	}

	/**
	 * Adjusts the size of inner components based on the outer bounds
	 */
	private void adjustSize() {
		double height = getHeight();
		double width = getWidth();

		double dieSize = Math.min(height, width * 0.4);

		die1.setFitHeight(dieSize);
		die2.setFitHeight(dieSize);
	}
}
