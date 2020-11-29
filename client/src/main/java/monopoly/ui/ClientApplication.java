package monopoly.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Singleton JavaFX Application class for Monopoly.
 * 
 * @author Ziya Mukhtarov
 * @version Nov 28, 2020
 */
@Getter
public class ClientApplication extends Application {
	@Getter
	@Setter(AccessLevel.PRIVATE)
	private static ClientApplication instance;
	private Scene scene;
	private Object controller;

	@Override
	public void init() {
		// Loading UIUtil class into memory here to call its static initialize block
		// that loads fonts
		UIUtil.calculateFittingFontSize(0, 0, "");

		setInstance(this);
		scene = null;
		controller = null;
	}

	@Override
	public void start(Stage stage) throws IOException {
		switchToView("fxml/Lobby.fxml");

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
		FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
		Parent root = loader.load();
		controller = loader.getController();

		if (scene == null) {
			scene = new Scene(root);
		} else {
			scene.setRoot(root);
		}
	}
}
