package monopoly.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import lombok.Getter;
import monopoly.NetworkManager;

/**
 * Singleton JavaFX Application class for Monopoly.
 *
 * @author Ziya Mukhtarov
 * @version Nov 28, 2020
 */
@Getter
public class ClientApplication extends Application {
	private Scene scene;
	private Object controller;
	private NetworkManager networkManager;

	public ClientApplication() throws IOException {
		// Loading UIUtil class into memory here to call its static initialize block
		// that loads fonts
		UIUtil.calculateFittingFont(0, 0, "");
		scene = null;
		controller = null;

		networkManager = new NetworkManager(this);
	}

	@Override
	public void start(Stage stage) throws IOException {
		switchToView("fxml/MainMenu.fxml");

		stage.setTitle("Monopoly");
		stage.setScene(scene);
		stage.centerOnScreen();
//		stage.setFullScreen(true);
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
		((MonopolyUIController) controller).setApp(this);

		if (scene == null) {
			scene = new Scene(root);
		} else {
			scene.setRoot(root);
		}
	}
}
