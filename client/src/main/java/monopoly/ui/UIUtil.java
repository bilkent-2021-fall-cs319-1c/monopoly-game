package monopoly.ui;

import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class UIUtil {
	public static final Image BACKGROUND_IMAGE1 = loadImage("images/Background1.jpg");
	public static final Image BACKGROUND_IMAGE2 = loadImage("images/Background2.jpg");
	public static final Image BACKGROUND_IMAGE3 = loadImage("images/Background3.jpg");

	public static final Image DEFAULT_PLAYER_IMAGE = loadImage("images/PlayerIcon.png");
	public static final Image DEFAULT_PLAYER_IMAGE_LOOKING_LEFT = loadImage("images/PlayerIconLookingLeft.png");

	public static final Image BOARD = loadImage("images/Board.png");
	public static final Image CURVED_ARROW_CW = loadImage("images/CurvedArrowCW.png");
	public static final Image CURVED_ARROW_CCW = loadImage("images/CurvedArrowCCW.png");

	public static final Image CHAT_ICON = loadImage("images/ChatIcon.png");
	public static final Image WEBCAM_ICON = loadImage("images/Camera.png");
	public static final Image WEBCAM_CROSSED_ICON = loadImage("images/CameraCross.png");
	public static final Image MICROPHONE_ICON = loadImage("images/Microphone.png");
	public static final Image MICROPHONE_CROSSED_ICON = loadImage("images/MicrophoneCross.png");

	public static final String COMMON_CSS = loadStylesheet("css/common.css");
	public static final String MAIN_SCREEN_CSS = loadStylesheet("css/mainScreen.css");
	public static final String PLAYER_LOBBY_PANEL_SCREEN_CSS = loadStylesheet("css/playerLobbyPanel.css");

	static {
		loadFont("fonts/kabel/Kabel-Bold.ttf");
	}

	private UIUtil() {
	}

	/**
	 * Calculates the font that fits in the given area
	 *
	 * @param width      The width of the area
	 * @param height     The height of the area
	 * @param fontFamily The name of the font family to use for the calculation
	 * @param text       The text to fit
	 * @return The font that fits in the given rectangular area
	 */
	public static Font calculateFittingFontSize(double width, double height, String fontFamily, String text) {
		final int defaultFontSize = 20;
		Font font = Font.font(fontFamily, defaultFontSize);
		Text tempText = new Text(text);
		tempText.setFont(font);

		double textWidth = tempText.getLayoutBounds().getWidth();
		double textHeight = tempText.getLayoutBounds().getHeight();

		double fontSize = Math.min(defaultFontSize * width / textWidth, defaultFontSize * height / textHeight);
		return new Font(fontFamily, fontSize);
	}

	/**
	 * Calculates the default font size that fits in the given area
	 *
	 * @param width  The width of the area
	 * @param height The height of the area
	 * @param text   The text to fit
	 * @return The font with the maximum font size that fits in the given
	 *         rectangular area using the default font
	 */
	public static Font calculateFittingFontSize(double width, double height, String text) {
		String defaultFontFamily = Font.getDefault().getFamily();
		return calculateFittingFontSize(width, height, defaultFontFamily, text);
	}

	private static Image loadImage(String path) {
		return new Image(GameplayController.class.getResourceAsStream(path));
	}

	private static String loadStylesheet(String path) {
		return GameplayController.class.getResource(path).toExternalForm();
	}

	private static void loadFont(String path) {
		Font.loadFont(GameplayController.class.getResourceAsStream(path), 20);
	}
}
