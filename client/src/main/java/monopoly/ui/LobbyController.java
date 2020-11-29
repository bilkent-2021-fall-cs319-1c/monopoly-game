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
	private Text passwordText;
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
		lobbyText.setFont(UIUtil.calculateFittingFontSize(width * 0.38, height * 0.14, "Kabel", lobbyText.getText()));
		nameText.setFont(UIUtil.calculateFittingFontSize(width * 0.38, height * 0.09, nameText.getText()));
		passwordText.setFont(UIUtil.calculateFittingFontSize(width * 0.38, height * 0.09, passwordText.getText()));
		waitingText.setFont(UIUtil.calculateFittingFontSize(width * 0.38, height * 0.19, waitingText.getText()));
	}
}
