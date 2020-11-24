package monopoly.ui;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class GameplayController {
    @FXML
    private StackPane stackPane;
    @FXML
    private MigPane rootMigPane;
    @FXML
    private ImageView board;
    @FXML
    private ImageView rotateCWIcon;
    @FXML
    private ImageView rotateCCWIcon;
    @FXML
    private ImageView chatIcon;
    @FXML
    private MigPane chatPane;

    private TranslateTransition openChatPane;
    private TranslateTransition closeChatPane;

    private RotateTransition boardRotateTransition;
    private ScaleTransition boardScaleTransition;
    private ParallelTransition boardRoateAndScaleTransition;

    private volatile boolean chatOpen;
    private volatile boolean boardRotating;
    private int currentBoardAngle;

    private ChangeListener<Number> widthListener;
    private ChangeListener<Number> heightListener;

    public GameplayController() {
	widthListener = (observable, oldValue, newValue) -> windowWidthChanged();
	heightListener = (observable, oldValue, newValue) -> windowHeightChanged();

	currentBoardAngle = 0;
	chatOpen = false;
	boardRotating = false;

	openChatPane = new TranslateTransition(Duration.millis(500));
	openChatPane.setToX(0);
	closeChatPane = new TranslateTransition(Duration.millis(500));

	openChatPane.setOnFinished(e -> chatOpen = true);
	closeChatPane.setOnFinished(e -> chatOpen = false);

	boardRotateTransition = new RotateTransition(Duration.millis(1000));
	boardScaleTransition = new ScaleTransition(Duration.millis(500));
	boardScaleTransition.setCycleCount(2);
	boardScaleTransition.setAutoReverse(true);
	boardScaleTransition.setToX(0.65);
	boardScaleTransition.setToY(0.65);
	boardRoateAndScaleTransition = new ParallelTransition(boardRotateTransition, boardScaleTransition);
	boardRoateAndScaleTransition.setOnFinished(e -> boardRotating = false);
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

	Image backgroundImage = new Image(GameplayController.class.getResourceAsStream("images/Background.jpg"));
	rootMigPane.setBackground(new Background(new BackgroundImage(backgroundImage, null, null, null, null)));

	Image boardImage = new Image(GameplayController.class.getResourceAsStream("images/Board.png"));
	board.setImage(boardImage);

	Image chatIconImg = new Image(GameplayController.class.getResourceAsStream("images/ChatIcon.png"));
	chatIcon.setImage(chatIconImg);

	Image rotateCWImg = new Image(GameplayController.class.getResourceAsStream("images/CurvedArrowCW.png"));
	rotateCWIcon.setImage(rotateCWImg);
	rotateCWIcon.setPickOnBounds(true);

	Image rotateCCWImg = new Image(GameplayController.class.getResourceAsStream("images/CurvedArrowCCW.png"));
	rotateCCWIcon.setImage(rotateCCWImg);
	rotateCCWIcon.setPickOnBounds(true);

	openChatPane.setNode(chatPane);
	closeChatPane.setNode(chatPane);

	boardRoateAndScaleTransition.setNode(board);
    }

    private void windowHeightChanged() {
	double windowHeight = stackPane.getScene().getHeight();
	double windowWidth = stackPane.getScene().getWidth();
	stackPane.setMaxHeight(windowHeight);

	double boardSize = Math.min(windowWidth * 0.5, windowHeight * 0.9);
	board.setFitWidth(boardSize);
	board.setFitHeight(boardSize);

	double iconHeight = windowHeight * 0.05;
	chatIcon.setFitHeight(iconHeight);
	rotateCWIcon.setFitHeight(iconHeight);
	rotateCCWIcon.setFitHeight(iconHeight);
    }

    private void windowWidthChanged() {
	double windowHeight = stackPane.getScene().getHeight();
	double windowWidth = stackPane.getScene().getWidth();
	stackPane.setMaxWidth(windowWidth);

	double boardSize = Math.min(windowWidth * 0.5, windowHeight * 0.9);
	board.setFitWidth(boardSize);
	board.setFitHeight(boardSize);

	double iconWidth = windowWidth * 0.025;
	chatIcon.setFitWidth(iconWidth);
	rotateCWIcon.setFitWidth(iconWidth);
	rotateCCWIcon.setFitWidth(iconWidth);

	double chatPaneWidth = windowWidth * 0.2;
	chatPane.setMinWidth(chatPaneWidth);
	chatPane.setPrefWidth(chatPaneWidth);
	chatPane.setMaxWidth(chatPaneWidth);

	chatPane.setTranslateX(chatPane.getPrefWidth());
	closeChatPane.setToX(chatPane.getPrefWidth());
    }

    @FXML
    private void toggleChatPane() {
	if (chatOpen) {
	    closeChatPane.play();
	} else {
	    openChatPane.play();
	}
    }

    @FXML
    private void rotateBoardCW() {
	if (boardRotating)
	    return;

	boardRotating = true;
	currentBoardAngle = currentBoardAngle + 90;
	boardRotateTransition.setToAngle(currentBoardAngle);
	boardRoateAndScaleTransition.play();
    }

    @FXML
    private void rotateBoardCCW() {
	if (boardRotating)
	    return;

	boardRotating = true;
	currentBoardAngle = currentBoardAngle - 90;
	boardRotateTransition.setToAngle(currentBoardAngle);
	boardRoateAndScaleTransition.play();
    }
}
