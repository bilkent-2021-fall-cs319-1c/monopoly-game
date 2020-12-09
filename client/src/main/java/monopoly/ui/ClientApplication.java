package monopoly.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
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
	private List<MonopolyUIController> controllers;
	private NetworkManager networkManager;

	private ChangeListener<Number> sizeListener;

	public ClientApplication() throws IOException {
		UIUtil.loadFonts();

		scene = null;
		controllers = Collections.synchronizedList(new ArrayList<MonopolyUIController>());

		sizeListener = (observable, newVal, oldVal) -> {
			synchronized (controllers) {
				for (MonopolyUIController controller : controllers) {
					if (controller != null) {
						controller.sizeChanged(rootPane.getWidth(), rootPane.getHeight());
					}
				}
			}
		};

		networkManager = new NetworkManager(this);
	}

	@Override
	public void start(Stage stage) throws IOException {
		rootPane = new StackPane();
		scene = new Scene(rootPane);

		rootPane.maxHeightProperty().bind(stage.heightProperty());
		rootPane.maxWidthProperty().bind(stage.widthProperty());
		rootPane.widthProperty().addListener(sizeListener);
		rootPane.heightProperty().addListener(sizeListener);

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

		MonopolyUIController controller = (MonopolyUIController) loader.getController();
		controller.setApp(this);
		controllers.clear();
		controllers.add(controller);

		List<Node> children = rootPane.getChildren();
		children.clear();
		children.add(root);

		fakeResize();
	}

	public void displayError() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Overlay.fxml"));
			Parent errorPane = loader.load();

			MonopolyUIController controller = (MonopolyUIController) loader.getController();
			controller.setApp(this);
			controllers.add(controller);

			Platform.runLater(() -> {
				rootPane.getChildren().add(errorPane);
				fakeResize();
				
				rootPane.getChildren().get(0).setEffect(new GaussianBlur(50));
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeOverlay() {
		List<Node> children = rootPane.getChildren();
		if (children.size() > 1) {
			children.remove(children.size() - 1);
			controllers.remove(controllers.size() - 1);

			rootPane.getChildren().get(0).setEffect(null);
		}
	}

	/**
	 * Fires fake resize event for correct initial sizing
	 */
	private void fakeResize() {
		sizeListener.changed(null, null, null);
	}

	/**
	 * @return The controller of the base node - the node that is displayed under
	 *         all other overlays, or null if there are no nodes
	 */
	public MonopolyUIController getMainController() {
		if (controllers.isEmpty())
			return null;
		return controllers.get(0);
	}
}
