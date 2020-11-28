package monopoly.ui;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.tbee.javafx.scene.layout.fxml.MigPane;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainScreenController {

    @FXML
    private MigPane mainMigPane;
    @FXML
    private Text heroMonopolyText;
    @FXML
    private Button createGameButton;
    @FXML
    private Button joinGameButton;
    @FXML
    private Button helpButton;
    @FXML
    private Button exitButton;

    private ChangeListener<Number> widthListener;
    private ChangeListener<Number> heightListener;


    Logger logger = Logger.getLogger(MainScreenController.class.getName());

    public MainScreenController() {
        widthListener = (observable, oldValue, newValue) -> windowWidthChanged();
        heightListener = (observable, oldValue, newValue) -> windowHeightChanged();
    }

    private void windowHeightChanged() {
        double windowHeight = mainMigPane.getScene().getHeight();
        double windowWidth = mainMigPane.getScene().getWidth();
        //Font heroTextFont = new Font(UIUtil.calculateFittingFontSize(windowHeight, windowWidth * 0.05, heroMonopolyText.getText()));
        //Font.font("Avenir Next");
        //heroMonopolyText.setFont(heroTextFont);
    }

    private void windowWidthChanged() {
        double windowHeight = mainMigPane.getScene().getHeight();
        double windowWidth = mainMigPane.getScene().getWidth();
        mainMigPane.setMaxWidth(windowWidth);
    }

    @FXML
    public void initialize() {
        mainMigPane.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.widthProperty().removeListener(widthListener);
                oldValue.heightProperty().removeListener(heightListener);
            }
            if (newValue != null) {
                newValue.widthProperty().addListener(widthListener);
                newValue.heightProperty().addListener(heightListener);
            }

            // Add necessary CSS classes
            heroMonopolyText.getStyleClass().add("heroMonopolyText");
            createGameButton.getStyleClass().add("buttonRegular");
            joinGameButton.getStyleClass().add("buttonRegular");
            helpButton.getStyleClass().add("buttonRegular");
            exitButton.getStyleClass().add("buttonDanger");

        });

        //Image backgroundImage = new Image(MainScreenController.class.getResourceAsStream("images/Background.png"));
        //mainMigPane.setBackground(new Background(new BackgroundImage(backgroundImage, null, null, BackgroundPosition.CENTER, null)));
    }


    // These functions will be filled in once the main controller is up and running
    @FXML
    public void switchToCreateGameScreen () throws IOException {
        logger.info("Crate Game Button Clicked!");
    }

    @FXML
    public void switchToLobbyScreen() throws IOException {
        logger.info("Join Game Button Clicked!");
    }

    @FXML
    public void switchToHelpScreen () throws IOException {
        logger.info("Help Button Clicked!");
    }

    @FXML
    public void exitApp() throws IOException {
        logger.info("Exit Button Clicked!");
    }

}
