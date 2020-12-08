package monopoly.ui;

import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
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
	private StackPane rootPane;
	private MonopolyUIController controller;
	private NetworkManager networkManager;

	private ChangeListener<Number> widthListener;
	private ChangeListener<Number> heightListener;

	public ClientApplication() throws IOException {
		UIUtil.loadFonts();

		scene = null;
		controller = null;

		widthListener = (observable, newVal, oldVal) -> {
			if (controller != null)
				controller.widthChanged(rootPane.getWidth(), rootPane.getHeight());
		};
		heightListener = (observable, newVal, oldVal) -> {
			if (controller != null)
				controller.heightChanged(rootPane.getWidth(), rootPane.getHeight());
		};

		networkManager = new NetworkManager(this);
	}

	@Override
	public void start(Stage stage) throws IOException {
		rootPane = new StackPane();
		scene = new Scene(rootPane);

		rootPane.maxHeightProperty().bind(stage.heightProperty());
		rootPane.maxWidthProperty().bind(stage.widthProperty());
		rootPane.widthProperty().addListener(widthListener);
		rootPane.heightProperty().addListener(heightListener);

		switchToView("fxml/MainMenu.fxml");

		stage.setTitle("Monopoly");
		stage.setScene(scene);
		stage.centerOnScreen();

		stage.setWidth(1280);
		stage.setHeight(720);
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
		controller = (MonopolyUIController) loader.getController();
		controller.setApp(this);

		List<Node> children = rootPane.getChildren();
		children.clear();
		children.add(root);

		// Fire fake resize action for correct initial sizing
		widthListener.changed(null, null, null);
		heightListener.changed(null, null, null);
	}
}
