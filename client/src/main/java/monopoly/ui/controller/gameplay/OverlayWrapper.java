package monopoly.ui.controller.gameplay;

import java.io.IOException;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.util.Duration;
import monopoly.ui.UIUtil;

public class OverlayWrapper extends MigPane {
	private Node overlay;
	private FadeTransition fadeTransition;

	public OverlayWrapper(Node overlay) {
		this.overlay = overlay;

		FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/OverlayWrapper.fxml"));
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
		overlay.getStyleClass().add("bordered");
		getChildren().add(overlay);

		fadeTransition = new FadeTransition(Duration.millis(500), this);
		fadeTransition.setFromValue(0);
		fadeTransition.setToValue(1);

		parentProperty().addListener((observable, oldVal, newVal) -> {
			if (newVal != null) {
				fade(true);
			}
		});
	}

	/**
	 * Fades in or out this overlay
	 * 
	 * @param fadeIn whether to fade in or out
	 */
	public void fade(boolean fadeIn) {
		fadeTransition.setRate(fadeIn ? 1.0 : -1.0);
		fadeTransition.play();
	}

	public void setOnFadeFinished(EventHandler<ActionEvent> handler) {
		fadeTransition.setOnFinished(handler);
	}
}
