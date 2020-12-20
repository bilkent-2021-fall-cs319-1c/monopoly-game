package monopoly.ui.controller.in_lobby;

import java.io.IOException;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import lombok.Getter;
import monopoly.ui.UIUtil;

/**
 * Displays a player in a lobby screen.
 * 
 * @author Ziya Mukhtarov
 * @version Nov 30, 2020
 */
public class UserLobbyPane extends MigPane {
	@FXML
	private ImageView playerImage;
	@FXML
	private Text playerName;
	@FXML
	private MigPane buttonGroup;
	@FXML
	private Button removeButton;

	private String userType;
	@Getter
	private String name;

	/**
	 * @param userType "admin" for display as an admin, anything else for default
	 *                 view
	 * @param name     username to display
	 */
	public UserLobbyPane(@NamedArg("userType") String userType, @NamedArg("name") String name) {
		this.userType = userType;
		this.name = name;

		FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/UserLobbyPane.fxml"));
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
		playerName.setText(name);

		if ("admin".equals(userType)) {
			playerName.setText(playerName.getText() + " (Admin)");
			buttonGroup.setVisible(false);
		} else {
			buttonGroup.setVisible(true);
		}

		layoutBoundsProperty().addListener((observable, oldVal, newVal) -> Platform.runLater(this::adjustSize));
	}

	public void changeReady(boolean ready) {
		Platform.runLater(() -> setStyle("-fx-background-color:" + (ready ? "lightgreen" : "indianred")));
	}

	/**
	 * Adjusts the size of inner components based on the outer bounds
	 */
	private void adjustSize() {
		double width = getWidth();
		double height = getHeight();

		UIUtil.fitFont(playerName, width * 0.3, height * 0.3);
	}
}
