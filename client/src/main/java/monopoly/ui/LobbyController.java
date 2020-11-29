package monopoly.ui;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class LobbyController {
	@FXML
	private MigPane root;
	@FXML
	private Text lobbyText;
	@FXML
	private Text nameText;
	@FXML
	private Text nameField;
	@FXML
	private Text passwordText;
	@FXML
	private Text passwordField;
	@FXML
	private Text waitingText;

	private ChangeListener<Number> widthListener;
	private ChangeListener<Number> heightListener;

	public LobbyController() {
		widthListener = (observable, oldValue, newValue) -> windowWidthChanged();
		heightListener = (observable, oldValue, newValue) -> windowHeightChanged();
	}

	@FXML
	public void initialize() {
		root.sceneProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue != null) {
				oldValue.widthProperty().removeListener(widthListener);
				oldValue.heightProperty().removeListener(heightListener);
			}
			if (newValue != null) {
				newValue.widthProperty().addListener(widthListener);
				newValue.heightProperty().addListener(heightListener);

				windowHeightChanged();
				windowWidthChanged();
			}
		});
	}

	private void windowHeightChanged() {
		double windowHeight = root.getScene().getHeight();
		double windowWidth = root.getScene().getWidth();

		setFontSizes(windowWidth, windowHeight);
	}

	private void windowWidthChanged() {
		double windowHeight = root.getScene().getHeight();
		double windowWidth = root.getScene().getWidth();

		setFontSizes(windowWidth, windowHeight);
	}

	private void setFontSizes(double width, double height) {
		lobbyText.setFont(UIUtil.calculateFittingFontSize(width * 0.30, height * 0.10, "Kabel", lobbyText.getText()));
		nameText.setFont(UIUtil.calculateFittingFontSize(width * 0.20, height * 0.09, "Avenir Next", nameText.getText()));
		nameField.setFont(UIUtil.calculateFittingFontSize(width * 0.15, height * 0.09, "Avenir Next", nameText.getText()));
		passwordText.setFont(UIUtil.calculateFittingFontSize(width * 0.25, height * 0.09, "Avenir Next", passwordText.getText()));
		passwordField.setFont(UIUtil.calculateFittingFontSize(width * 0.15, height * 0.09, "Avenir Next", nameText.getText()));
		waitingText.setFont(UIUtil.calculateFittingFontSize(width * 0.25, height * 0.09, "Avenir Next", waitingText.getText()));
	}
}
