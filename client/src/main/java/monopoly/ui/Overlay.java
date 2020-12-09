package monopoly.ui;

import java.io.IOException;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.animation.FadeTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lombok.Setter;
import monopoly.Error;

public class Overlay extends MigPane implements MonopolyUIController {
	@FXML
	private Label title;
	@FXML
	private Text info;

	@Setter
	private ClientApplication app;

	private FadeTransition fadeTransition;
	private DoubleProperty fadeLevel;
	private Error error;

	public Overlay(Error error) {
		this.error = error;
		fadeLevel = new SimpleDoubleProperty(0);

		FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/Overlay.fxml"));
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
		fadeTransition = new FadeTransition(Duration.millis(500), this);

		fadeTransition.currentTimeProperty().addListener((observable, oldVal, newVal) -> fadeLevel
				.set(newVal.toMillis() / fadeTransition.getDuration().toMillis()));

		fadeTransition.setFromValue(0);
		fadeTransition.setToValue(1);

		parentProperty().addListener((observable, oldVal, newVal) -> {
			if (newVal != null) {
				fade(true);
			}
		});

		if (error != null) {
			title.setText(error.getTitle());
			info.setText(error.getInfo());
		}
	}

	@FXML
	private void closeOverlay() {
		fadeTransition.setOnFinished(e -> app.closeOverlay());
		fade(false);
	}

	private void fade(boolean fadeIn) {
		fadeTransition.setRate(fadeIn ? 1.0 : -1.0);
		fadeTransition.play();
	}

	/**
	 * @return Current fade level of this overlay.
	 */
	public ReadOnlyDoubleProperty fadeLevelProperty() {
		return fadeLevel;
	}

	@Override
	public void sizeChanged(double width, double height) {
		setFonts(width, height);
	}

	private void setFonts(double width, double height) {
		UIUtil.fitFont(title, width * 0.45, height * 0.09);
		info.setFont(Font.font(title.getFont().getSize() * 0.5));
	}
}
