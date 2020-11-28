package monopoly.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import monopoly.ui.MainScreenController;
import org.tbee.javafx.scene.layout.MigPane;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class MainUIController {

    @FXML
    Pane textPane;

    enum Screens {
        MAIN_SCREEN,
        LOBBY_SCREEN,
        GAME_SCREEN
    }

    Logger logger = Logger.getLogger(MainUIController.class.getName());

    @FXML
    StackPane stackPane;
    @FXML
    MigPane mainMigPane;
    @FXML
    Button createGameButton;
    @FXML
    Text heroMonopolyText;
    @FXML
    Button joinGameButton;
    @FXML
    Button helpButton;
    @FXML
    Button exitButton;

    private MainScreenController mainScreenController;
    //private LobbyScreenController lobbyScreenController;
    private GameplayController gameplayController;

    private FXMLLoader mainScreenLoader;
    private FXMLLoader lobbyScreenLoader;
    private FXMLLoader gameScreenLoader;

    private Node mainScreen;
    private Node lobbyScreen;
    private Node gameScreen;

    private Screens currentScreen;


    public void initialize() throws IOException {
        //mainScreenLoader = new FXMLLoader(getClass().getResource("fxml/MainScreen.fxml"));
        lobbyScreenLoader = new FXMLLoader(getClass().getResource("fxml/Gameplay.fxml"));
        gameScreenLoader = new FXMLLoader(getClass().getResource("fxml/Gameplay.fxml"));

        //Image backgroundImage = new Image(MainUIController.class.getResourceAsStream("images/Background.jpg"));
        //stackPane.setBackground(new Background(new BackgroundImage(backgroundImage, null, null, null, null)));
        heroMonopolyText.getStyleClass().add("heroMonopolyText");
        //initMainScreen();

        // Add classes to buttons
        currentScreen = Screens.MAIN_SCREEN;
    }

    public void initMainScreen() throws IOException {
        mainScreen = mainScreenLoader.load();
        mainScreenController = mainScreenLoader.getController();
        //mainScreenController.setMainController(this);
    }

    public void initGameScreen() throws IOException {
        gameScreen = gameScreenLoader.load();
        gameplayController = gameScreenLoader.getController();
    }

    public void switchToGameplayScreen() throws IOException {
        if (currentScreen != Screens.GAME_SCREEN) {
            if (gameScreen == null) {
                initGameScreen();
                logger.info("Initialized Game Screen");
            }

            currentScreen = Screens.GAME_SCREEN;
            stackPane.getChildren().remove(stackPane.getChildren().size() - 1);
            stackPane.getChildren().add(gameScreen);
            gameplayController.initialize();
            logger.info("Switched to Game Screen");
        }


    }

    public void exitApp() {
        logger.info("exit");
    }

}
