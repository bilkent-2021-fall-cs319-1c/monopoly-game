package monopoly.ui;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class JoinLobbyController {
	@FXML
	private StackPane rootPane;
	@FXML
	private MigPane mainPane;
	@FXML
	private MigPane rightPane;
	@FXML
	private MigPane leftPane;
	@FXML
	private Text mainTitle;
	@FXML
	private Text promptText;
	@FXML
	private Text infoText;
	@FXML
	private Text roomTitle;
	@FXML
	private TextField roomName;
	@FXML
	private Text passwordTitle;
	@FXML
	private TextField passwordValue;
	@FXML
	private Button joinButton;

	private ChangeListener<Number> widthListener;
	private ChangeListener<Number> heightListener;

	public JoinLobbyController() {
		widthListener = (observable, oldValue, newValue) -> windowWidthChanged();
		heightListener = (observable, oldValue, newValue) -> windowHeightChanged();
	}

	@FXML
	public void initialize() {
		rootPane.sceneProperty().addListener((observable, oldValue, newValue) -> {
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

	private void setFontSizes(double height, double width) {
		mainTitle.setFont(new Font(UIUtil.calculateFittingFontSize(width * 0.18, height, mainTitle.getText())));
		promptText.setFont(new Font(UIUtil.calculateFittingFontSize(width * 0.12, height, promptText.getText())));
		infoText.setFont(new Font(UIUtil.calculateFittingFontSize(width * 0.35, height, infoText.getText())));
		roomTitle.setFont(new Font(UIUtil.calculateFittingFontSize(width * 0.10, height, roomTitle.getText())));
		passwordTitle
				.setFont(new Font(UIUtil.calculateFittingFontSize(width * 0.075, height, passwordTitle.getText())));
		roomName.setFont(new Font(UIUtil.calculateFittingFontSize(height, width * 0.011, roomName.getText())));
		passwordValue
				.setFont(new Font(UIUtil.calculateFittingFontSize(height, width * 0.010, passwordValue.getText())));
		joinButton.setFont(new Font(UIUtil.calculateFittingFontSize(joinButton.getWidth() - 5,
				joinButton.getHeight() - 5, joinButton.getText())));
	}

	private void windowHeightChanged() {
		double height = rootPane.getScene().getHeight();
		double width = rootPane.getScene().getWidth();
		rootPane.setMaxHeight(height);

		joinButton.setPrefHeight(height * 0.04);
		setFontSizes(height, width);

	}

	private void windowWidthChanged() {
		double height = rootPane.getScene().getHeight();
		double width = rootPane.getScene().getWidth();
		rootPane.setMaxWidth(width);

		joinButton.setPrefWidth(width * 0.08);
		setFontSizes(height, width);

	}

}
