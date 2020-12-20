package monopoly.ui.controller.gameplay.board.dice;

import java.io.IOException;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import lombok.Getter;
import lombok.Setter;
import monopoly.network.packet.important.packet_data.gameplay.DicePacketData;
import monopoly.ui.ClientApplication;
import monopoly.ui.UIUtil;

public class Dice extends MigPane {
	@FXML
	private Die die1;
	@FXML
	private Die die2;

	@Setter
	private ClientApplication app;

	@Getter
	private boolean rolling;

	public Dice() {
		FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/Dice.fxml"));
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

	@FXML
	private void roll() {
		if (rolling)
			return;

		rolling = true;
		app.getNetworkManager().askRollDice();
	}

	public void rollAndShowResult(DicePacketData result) {
		die1.roll(result.getFirstDieValue());
		die2.roll(result.getSecondDieValue());

		while (die1.isRolling() || die2.isRolling()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
		rolling = false;
	}
}
