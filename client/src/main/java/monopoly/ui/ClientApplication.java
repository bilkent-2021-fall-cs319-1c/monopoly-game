package monopoly.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.Getter;
import monopoly.Error;
import monopoly.ErrorListener;
import monopoly.network.NetworkManager;
import monopoly.network.packet.important.PacketType;
import monopoly.ui.controller.MonopolyUIController;
import monopoly.ui.controller.Overlay;

/**
 * JavaFX Application class for Monopoly.
 *
 * @author Ziya Mukhtarov
 * @version Nov 28, 2020
 */
public class ClientApplication extends Application implements ErrorListener {
	@Getter
	private Scene scene;
	private StackPane rootPane;
	@Getter
	private List<MonopolyUIController> controllers;
	@Getter
	private NetworkManager networkManager;

	private ChangeListener<Bounds> sizeListener;

	private GaussianBlur baseNodeBlur;

	/**
	 * Creates a new client application and connects it to the server
	 *
	 * @throws IOException if an I/O error occurs when connecting to the server
	 */
	public ClientApplication() throws IOException {
		UIUtil.loadFonts();

		scene = null;
		baseNodeBlur = new GaussianBlur();
		controllers = Collections.synchronizedList(new ArrayList<MonopolyUIController>());

		sizeListener = (observable, newVal, oldVal) -> {
			synchronized (controllers) {
				for (MonopolyUIController controller : controllers) {
					if (controller != null) {
						Platform.runLater(() -> controller.sizeChanged(rootPane.getWidth(), rootPane.getHeight()));
					}
				}
			}
		};

		networkManager = new NetworkManager(this);
		networkManager.addErrorListener(this);
	}

	@Override
	public void start(Stage stage) throws IOException {
		rootPane = new StackPane();
		scene = new Scene(rootPane);

		rootPane.maxHeightProperty().bind(stage.heightProperty());
		rootPane.maxWidthProperty().bind(stage.widthProperty());

		switchToView("fxml/MainMenu.fxml");

		stage.setTitle("Monopoly");
		stage.setScene(scene);
		stage.centerOnScreen();

		stage.setWidth(1600);
		stage.setHeight(900);
		stage.setMaximized(true);

		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		stage.show();

		rootPane.layoutBoundsProperty().addListener(sizeListener);
	}

	/**
	 * Switches the active scene's root node to the given one
	 *
	 * @param resourcePath The path to the resource (mainly, FXML file) to switch to
	 * @throws IOException If the resource cannot be found
	 */
	public void switchToView(String resourcePath) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
		Parent root;
		loader.setLoadListener(new MonopolyFXMLLoadListener() {
			@Override
			public void endElement(Object value) {
				MonopolyUIController controller = (MonopolyUIController) loader.getController();
				controller.setApp(ClientApplication.this);
			}
		});
		try {
			root = loader.load();
		} catch (IOException | IllegalStateException e) {
			e.printStackTrace();
			displayError(new Error(PacketType.ERR_UNKNOWN));
			return;
		}

		MonopolyUIController controller = (MonopolyUIController) loader.getController();
		controllers.clear();
		controllers.add(controller);

		List<Node> children = rootPane.getChildren();
		children.clear();
		children.add(root);

		baseNodeBlur.setRadius(0);
		root.setEffect(baseNodeBlur);

		fakeResize();
	}

	/**
	 * Displays an error overlay showing the given error details. Blurs the base
	 * node that is displayed under the overlay.
	 *
	 * @param error The error to display
	 */
	public void displayError(Error error) {
		Overlay errorOverlay = new Overlay(error);
		errorOverlay.setApp(this);
		controllers.add(errorOverlay);

		Platform.runLater(() -> {
			baseNodeBlur.radiusProperty().bind(Bindings.multiply(50.0, errorOverlay.fadeLevelProperty()));

			rootPane.getChildren().add(errorOverlay);
			fakeResize();
		});
	}

	/**
	 * Closes the most recent opened overlay. Removes the blur effect from the base
	 * node that was displayed under the overlay.
	 */
	public void closeOverlay() {
		List<Node> children = rootPane.getChildren();
		if (children.size() > 1) {
			controllers.remove(controllers.size() - 1);
			children.remove(children.size() - 1);
			baseNodeBlur.radiusProperty().unbind();
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

	@Override
	public void errorOccured(Error error) {
		displayError(error);
	}
}
