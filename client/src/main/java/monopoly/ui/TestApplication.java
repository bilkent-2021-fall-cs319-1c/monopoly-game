package monopoly.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class TestApplication extends Application {
	@Override
	public void start(Stage stage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("fxml/JoinLobby.fxml"));
		Scene scene = new Scene(root);

		stage.setTitle("Test JavaFX UI");
		stage.setScene(scene);

		// Just for testing
		stage.setWidth(1280);
		stage.setHeight(720);

		stage.setFullScreen(true);
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		stage.show();

	}
}
