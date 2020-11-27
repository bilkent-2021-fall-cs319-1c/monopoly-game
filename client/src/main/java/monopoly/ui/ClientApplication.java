package monopoly.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import lombok.Getter;

/**
 * Singleton JavaFX Application class for Monopoly.
 * 
 * @author Ziya Mukhtarov
 * @version Nov 28, 2020
 */
@Getter
public class ClientApplication extends Application {
	private ClientApplication instance;
	private Scene scene;

	@Override
	public void init() {
		instance = this;
		scene = null;
	}

	@Override
	public void start(Stage stage) throws IOException {
		switchToView("fxml/Gameplay.fxml");

		stage.setTitle("Monopoly");
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.setFullScreen(true);
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		stage.show();
	}

	/**
	 * Switches the active scene's root node to the given one
	 * 
	 * @param resourcePath The path to the resource (mainly, FXML file) to switch to
	 * @throws IOException If the resource cannot be found
	 */
	public void switchToView(String resourcePath) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(resourcePath));
		if (scene == null) {
			scene = new Scene(root);
		} else {
			scene.setRoot(root);
		}
	}
}
