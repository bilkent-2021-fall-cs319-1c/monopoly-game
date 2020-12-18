package monopoly.ui.in_lobby;

import java.io.IOException;
import java.util.stream.Stream;

import org.tbee.javafx.scene.layout.fxml.MigPane;

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
public class PlayerLobbyPane extends MigPane {
	@FXML
	private ImageView playerImage;
	@FXML
	private Text playerName;
	@FXML
	private MigPane buttonGroup;
	@FXML
	private Button makeAdminButton;
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
	public PlayerLobbyPane(@NamedArg("userType") String userType, @NamedArg("name") String name) throws IOException {
		this.userType = userType;
		this.name = name;

		FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/PlayerLobbyPane.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		loader.load();

		Stream.of(widthProperty(), heightProperty())
				.forEach(p -> p.addListener((observable, oldVal, newVal) -> adjustSize()));
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
	}

	public void adjustSize() {
		double width = getWidth();
		double height = getHeight();

		playerName.setFont(UIUtil.calculateFittingFont(width * 0.3, height * 0.3, playerName.getText()));

		makeAdminButton.setPrefHeight(height * 0.2);
		makeAdminButton.setPrefWidth(width * 0.2);
	}
}