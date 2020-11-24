package monopoly.ui;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.util.Duration;

public class GameplayController {
    @FXML
    public MigPane rootMigPane;
    @FXML
    private ImageView board;
    @FXML
    private ImageView rotateCWIcon;
    @FXML
    private ImageView rotateCCWIcon;
    @FXML
    private ImageView chatIcon;
    @FXML
    public MigPane chatPane;

    private TranslateTransition openChatPane;
    private TranslateTransition closeChatPane;

    private RotateTransition boardRotateTransition;
    private ScaleTransition boardScaleTransition;
    private ParallelTransition boardRoateAndScaleTransition;

    private volatile boolean chatOpen;
    private volatile boolean boardRotating;
    private int currentBoardAngle;

    public GameplayController() {
	currentBoardAngle = 0;
	chatOpen = false;
	boardRotating = false;

	openChatPane = new TranslateTransition(Duration.seconds(1));
	openChatPane.setToX(0);
	closeChatPane = new TranslateTransition(Duration.seconds(1));

	openChatPane.setOnFinished(e -> chatOpen = true);
	closeChatPane.setOnFinished(e -> chatOpen = false);

	boardRotateTransition = new RotateTransition(Duration.millis(1000));
	boardScaleTransition = new ScaleTransition(Duration.millis(500));
	boardScaleTransition.setCycleCount(2);
	boardScaleTransition.setAutoReverse(true);
	boardScaleTransition.setToX(1 / Math.sqrt(2));
	boardScaleTransition.setToY(1 / Math.sqrt(2));
	boardRoateAndScaleTransition = new ParallelTransition(boardRotateTransition, boardScaleTransition);
	boardRoateAndScaleTransition.setOnFinished(e -> boardRotating = false);
    }

    @FXML
    public void initialize() {
	Image backgroundImage = new Image(GameplayController.class.getResourceAsStream("images/Background.jpg"));
	rootMigPane.setBackground(new Background(new BackgroundImage(backgroundImage, null, null, null, null)));
	rootMigPane.heightProperty().addListener((observable, oldValue, newValue) -> windowSizeChanged());
	rootMigPane.widthProperty().addListener((observable, oldValue, newValue) -> windowSizeChanged());

	Image boardImage = new Image(GameplayController.class.getResourceAsStream("images/Board.png"));
	board.setImage(boardImage);

	Image chatIconImg = new Image(GameplayController.class.getResourceAsStream("images/ChatIcon.png"));
	chatIcon.setImage(chatIconImg);

	Image rotateCWImg = new Image(GameplayController.class.getResourceAsStream("images/CurvedArrowCW.png"));
	rotateCWIcon.setImage(rotateCWImg);

	Image rotateCCWImg = new Image(GameplayController.class.getResourceAsStream("images/CurvedArrowCCW.png"));
	rotateCCWIcon.setImage(rotateCCWImg);

	windowSizeChanged();
	openChatPane.setNode(chatPane);
	closeChatPane.setNode(chatPane);
	
	boardRoateAndScaleTransition.setNode(board);
    }

    private void windowSizeChanged() {
	double windowWidth = rootMigPane.getWidth();
	double windowHeight = rootMigPane.getHeight();

	double boardSize = Math.min(windowWidth * 0.5, windowHeight);
	board.setFitWidth(boardSize);
	board.setFitHeight(boardSize);

	double chatIconWidth = windowWidth * 0.02;
	double chatIconHeight = windowHeight * 0.04;
	chatIcon.setFitHeight(chatIconHeight);
	chatIcon.setFitWidth(chatIconWidth);

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
