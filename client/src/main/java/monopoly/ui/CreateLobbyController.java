package monopoly.ui;

import java.io.IOException;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import lombok.Setter;
import monopoly.Error;

public class CreateLobbyController implements MonopolyUIController {
	@Setter
	private ClientApplication app;

	@FXML
	private MigPane root;
	@FXML
	private Text mainTitle;
	@FXML
	private Text limitTitle;
	@FXML
	private Spinner<Integer> limitValue;
	@FXML
	private Text roomTitle;
	@FXML
	private TextField roomName;
	@FXML
	private Text passwordTitle;
	@FXML
	private TextField passwordValue;
	@FXML
	private CheckBox checkPriv;
	@FXML
	private Button createButton;

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
			app.displayError(new Error("Invalid Lobby Data",
					"The data you entered is invalid for a lobby. Please fix the issues and try again."));
			return;
		}

		String lobbyName = roomName.getText();
		boolean isPublic = !checkPriv.isSelected();
		String password = passwordValue.getText();
		int playerLimit = limitValue.getValue();

		boolean success = app.getNetworkManager().createLobby(lobbyName, isPublic, password, playerLimit);
		if (success) {
			app.switchToView("fxml/Lobby.fxml");
		}
	}

	private boolean validate() {
		// TODO
		return true;
	}

	@Override
	public void sizeChanged(double width, double height) {
		createButton.setPrefHeight(height * 0.04);
		createButton.setPrefWidth(width * 0.08);
		setFontSizes(width, height);
	}

	private void setFontSizes(double width, double height) {
		UIUtil.fitFont(mainTitle, width, height * 0.14);

		UIUtil.fitFont(limitTitle, width * 0.45, height * 0.05);
		UIUtil.fitFont(roomTitle, width * 0.45, height * 0.05);
		UIUtil.fitFont(passwordTitle, width * 0.45, height * 0.05);
		UIUtil.fitFont(checkPriv, width * 0.45, height * 0.05);

		UIUtil.fitFont(createButton, width * 0.45, height * 0.05);
		UIUtil.fitFont(roomName, Double.MAX_VALUE, height * 0.03);
		UIUtil.fitFont(passwordValue, Double.MAX_VALUE, height * 0.03);
	}
}
