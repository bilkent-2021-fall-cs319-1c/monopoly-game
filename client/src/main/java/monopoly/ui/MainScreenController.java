package monopoly.ui;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainScreenController {

    @FXML
    private StackPane stackPane;
    @FXML
    private Button createGameButton;
    @FXML
    private Button joinGameButton;

    private ChangeListener<Number> widthListener;
    private ChangeListener<Number> heightListener;

    Logger logger = Logger.getLogger(MainScreenController.class.getName());

    public MainScreenController() {
        widthListener = (observable, oldValue, newValue) -> windowWidthChanged();
        heightListener = (observable, oldValue, newValue) -> windowHeightChanged();
    }

    private void windowHeightChanged() {
        double windowHeight = stackPane.getScene().getHeight();
        double windowWidth = stackPane.getScene().getWidth();
        stackPane.setMaxHeight(windowHeight);
    }

    private void windowWidthChanged() {
        double windowHeight = stackPane.getScene().getHeight();
        double windowWidth = stackPane.getScene().getWidth();
        stackPane.setMaxWidth(windowWidth);
    }

    @FXML
    public void initialize() {
        stackPane.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.widthProperty().removeListener(widthListener);
                oldValue.heightProperty().removeListener(heightListener);
            }
            if (newValue != null) {
                newValue.widthProperty().addListener(widthListener);
                newValue.heightProperty().addListener(heightListener);
            }
        });

        Image backgroundImage = new Image(MainScreenController.class.getResourceAsStream("images/Background.jpg"));
        stackPane.setBackground(new Background(new BackgroundImage(backgroundImage, null, null, null, null)));
    }


    public void switchToCreateGameScreen(ActionEvent actionEvent) {
        logger.log(Level.INFO,"Create Game Button Pressed!");
        FXMLLoader createGameScreen = new FXMLLoader(getClass().getResource("fxml/Gameplay.fxml"));
        Stage stage = (Stage) createGameButton.getScene().getWindow();
        Scene scene = null;
        try {
            scene = new Scene(createGameScreen.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.setFullScreen(true);
    }

    public void switchToJoinGameScreen(ActionEvent actionEvent) {
        logger.log(Level.INFO,"Join Game Button Pressed");
        FXMLLoader joinGameScreen = new FXMLLoader(getClass().getResource("fxml/Gameplay.fxml"));
        Stage stage = (Stage) joinGameButton.getScene().getWindow();
        Scene scene = null;
        try {
            scene = new Scene(joinGameScreen.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.setFullScreen(true);
    }
}
