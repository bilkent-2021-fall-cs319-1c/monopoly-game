package monopoly.ui;

import java.io.IOException;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class CreateLobbyController {
	@FXML
	private StackPane stackPane;
	@FXML
	public MigPane migPanel;
	@FXML
	public Text mainTitle;
	@FXML
	public Text limitTitle;
	@FXML
	public Spinner<Integer> limitValue;
	@FXML
	public Text roomTitle;
	@FXML
	public TextField roomName;
	@FXML
	public Text passwordTitle;
	@FXML
	public TextField passwordValue;
	@FXML
	public CheckBox checkPriv;
	@FXML
	public Button createButton;

	private ChangeListener<Number> widthListener;
	private ChangeListener<Number> heightListener;

	public CreateLobbyController() {
		widthListener = (observable, oldValue, newValue) -> windowWidthChanged();
		heightListener = (observable, oldValue, newValue) -> windowHeightChanged();
	}

	@FXML
	public void initialize() {
		stackPane.sceneProperty().addListener((observable, oldValue, newValue) -> {
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

	@FXML
	private void privateChange() {
		boolean isPrivate = checkPriv.isSelected();
		if (isPrivate) {
			passwordValue.setDisable(false);
		} else {
			passwordValue.setDisable(true);
			passwordValue.clear();
		}
	}

	@FXML
	private void validateAndCreateLobby() throws IOException {
		if (!validate()) {
			// TODO Display error
			return;
		}

		String lobbyName = roomName.getText();
		boolean isPublic = !checkPriv.isSelected();
		String password = passwordValue.getText();
		int playerLimit = limitValue.getValue();

		boolean success = ClientApplication.getInstance().getNetworkManager().createLobby(lobbyName, isPublic, password,
				playerLimit);
		System.out.println(success);
		if (success) {
			ClientApplication.getInstance().switchToView("fxml/Lobby.fxml");
		}
	}

	private boolean validate() {
		// TODO
		return true;
	}

	private void windowHeightChanged() {
		double height = stackPane.getScene().getHeight();
		double width = stackPane.getScene().getWidth();
		stackPane.setMaxHeight(height);

		createButton.setPrefHeight(height * 0.04);
		setFontSizes(height, width);
	}

	private void windowWidthChanged() {
		double height = stackPane.getScene().getHeight();
		double width = stackPane.getScene().getWidth();
		stackPane.setMaxWidth(width);

		createButton.setPrefWidth(width * 0.08);
		setFontSizes(height, width);
	}

	private void setFontSizes(double height, double width) {
		mainTitle.setFont(UIUtil.calculateFittingFontSize(height, width * 0.055, mainTitle.getText()));
		limitTitle.setFont(UIUtil.calculateFittingFontSize(height, width * 0.03, limitTitle.getText()));
		roomTitle.setFont(UIUtil.calculateFittingFontSize(height, width * 0.03, roomTitle.getText()));
		passwordTitle.setFont(UIUtil.calculateFittingFontSize(height, width * 0.03, passwordTitle.getText()));
		roomName.setFont(UIUtil.calculateFittingFontSize(height, width * 0.014, roomName.getText()));
		passwordValue.setFont(UIUtil.calculateFittingFontSize(height, width * 0.014, passwordValue.getText()));

		checkPriv.setFont(UIUtil.calculateFittingFontSize(height, width * 0.02, checkPriv.getText()));
		createButton.setFont(UIUtil.calculateFittingFontSize(createButton.getPrefWidth() - 20,
				createButton.getPrefHeight() - 20, createButton.getText()));
	}
}
