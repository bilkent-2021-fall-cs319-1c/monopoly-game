package monopoly.ui.controller;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import lombok.Setter;
import monopoly.Error;
import monopoly.ui.ClientApplication;
import monopoly.ui.UIUtil;

/**
 * Controls the create lobby UI
 * 
 * @author Ziya Mukhtarov
 * @version Dec 13, 2020
 */
public class CreateLobbyController implements MonopolyUIController {
	@Setter
	private ClientApplication app;

	@FXML
	private MigPane root;
	@FXML
	private ImageView backIcon;
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
	private void validateAndCreateLobby() {
		String lobbyName = roomName.getText();
		boolean isPublic = !checkPriv.isSelected();
		String password = passwordValue.getText();
		int playerLimit = limitValue.getValue();

		if (!validate(playerLimit)) {
			app.displayError(new Error("Invalid Lobby Data",
					"The data you entered is invalid for a lobby. Please fix the issues and try again."));
			return;
		}

		boolean success = app.getNetworkManager().createLobby(lobbyName, isPublic, password, playerLimit);
		if (success) {
			app.switchToView("fxml/Lobby.fxml");
		}
	}

	/**
	 * Validates the input fields and check whether they are valid values for a
	 * lobby
	 * 
	 * @return true if the fields are valid, false otherwise
	 */
	private boolean validate(int playerLimit) {
		return playerLimit >= 2 && playerLimit <= 6;
	}

	@FXML
	private void back() {
		app.switchToView("fxml/MainMenu.fxml");
	}

	@Override
	public void sizeChanged(double width, double height) {
		backIcon.setFitHeight(height * 0.1);
		backIcon.setFitWidth(width * 0.03);

		setFontSizes(width, height);
	}

	/**
	 * Called when the bounds of this pane is changed. Responsive UIs should use
	 * this method to update their font sizes and scaling
	 * 
	 * @param width  The total width of this container
	 * @param height The total height of this container
	 */
	private void setFontSizes(double width, double height) {
		UIUtil.fitFont(mainTitle, width * 0.75, height * 0.15);

		UIUtil.fitFont(limitTitle, width * 0.35, height * 0.05);
		UIUtil.fitFont(roomTitle, width * 0.35, height * 0.05);
		UIUtil.fitFont(passwordTitle, width * 0.35, height * 0.05);
		UIUtil.fitFont(checkPriv, width * 0.15, height * 0.05);

		UIUtil.fitFont(createButton, width * 0.35, height * 0.05);
		UIUtil.fitFont(limitValue.getEditor(), Double.MAX_VALUE, height * 0.03);
		UIUtil.fitFont(roomName, Double.MAX_VALUE, height * 0.03);
		UIUtil.fitFont(passwordValue, Double.MAX_VALUE, height * 0.03);
	}
}
