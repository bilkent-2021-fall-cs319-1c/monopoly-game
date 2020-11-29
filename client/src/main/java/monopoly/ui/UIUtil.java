package monopoly.ui;

import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class UIUtil {
	public static final Image GAMEPLAY_BACKGROUND_IMAGE = loadImage("images/Background.jpg");
	public static final Image DEFAULT_PLAYER_IMAGE = loadImage("images/PlayerIcon.png");
	public static final Image DEFAULT_PLAYER_IMAGE_LOOKING_LEFT = loadImage("images/PlayerIconLookingLeft.png");
	public static final Image LOCK_ICON = loadImage("images/Lock.png");

	public static final Image BOARD = loadImage("images/Board.png");
	public static final Image CURVED_ARROW_CW = loadImage("images/CurvedArrowCW.png");
	public static final Image CURVED_ARROW_CCW = loadImage("images/CurvedArrowCCW.png");

	public static final Image CHAT_ICON = loadImage("images/ChatIcon.png");
	public static final Image WEBCAM_ICON = loadImage("images/Camera.png");
	public static final Image WEBCAM_CROSSED_ICON = loadImage("images/CameraCross.png");
	public static final Image MICROPHONE_ICON = loadImage("images/Microphone.png");
	public static final Image MICROPHONE_CROSSED_ICON = loadImage("images/MicrophoneCross.png");

	public static final String MAIN_CSS = loadStylesheet("css/common.css");
	public static final String MAIN_SCREEN_CSS = loadStylesheet("css/mainScreen.css");

	private UIUtil() {
	}

	public static double calculateFittingFontSize(double width, double height, String text) {
		final int defaultFontSize = 20;
		Font font = Font.font(defaultFontSize);
		Text tempText = new Text(text);
		tempText.setFont(font);

		double textWidth = tempText.getLayoutBounds().getWidth();
		double textHeight = tempText.getLayoutBounds().getHeight();

		return Math.min(defaultFontSize * width / textWidth, defaultFontSize * height / textHeight);
	}

	private static Image loadImage(String path) {
		return new Image(GameplayController.class.getResourceAsStream(path));
	}
	private static String loadStylesheet(String path) {
		return GameplayController.class.getResource(path).toExternalForm();
	}
}
