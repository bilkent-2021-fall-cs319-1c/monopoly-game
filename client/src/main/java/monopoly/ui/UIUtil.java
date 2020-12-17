package monopoly.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Contains utility methods for working with UI. Loads resources such as images
 * and CSS, and holds them in the static constant fields.
 *
 * @author Ziya Mukhtarov
 * @version Dec 8, 2020
 */
public class UIUtil {
	public static final Image BACKGROUND_IMAGE1 = loadImage("images/Background1.jpg");
	public static final Image BACKGROUND_IMAGE2 = loadImage("images/Background2.jpg");
	public static final Image BACKGROUND_IMAGE3 = loadImage("images/Background3.jpg");
	public static final Image BACKGROUND_IMAGE4 = loadImage("images/Background4.png");

	public static final Image DEFAULT_PLAYER_IMAGE = loadImage("images/PlayerIcon.png");
	public static final Image DEFAULT_PLAYER_IMAGE_LOOKING_LEFT = loadImage("images/PlayerIconLookingLeft.png");
	public static final Image LOCK_ICON = loadImage("images/Lock.png");

	public static final Image BOARD = loadImage("images/Board.png");
	public static final Image CURVED_ARROW_CW = loadImage("images/CurvedArrowCW.png");
	public static final Image CURVED_ARROW_CCW = loadImage("images/CurvedArrowCCW.png");
	public static final Image BACK_ICON = loadImage("images/BackIcon.png");
	
	public static final Image CHAT_ICON = loadImage("images/ChatIcon.png");
	public static final Image WEBCAM_ICON = loadImage("images/Camera.png");
	public static final Image WEBCAM_CROSSED_ICON = loadImage("images/CameraCross.png");
	public static final Image MICROPHONE_ICON = loadImage("images/Microphone.png");
	public static final Image MICROPHONE_CROSSED_ICON = loadImage("images/MicrophoneCross.png");

	public static final Image FREE_PARKING = loadImage("images/freeParking.png");
	public static final Image GO_TO_JAIL = loadImage("images/goToJail.png");
	public static final Image GO = loadImage("images/go.png");
	public static final Image JAIL = loadImage("images/justVisiting.png");
	public static final Image TRAIN = loadImage("images/train.png");
	public static final Image CHEST = loadImage("images/chest.png");
	public static final Image QUESTION_MARK = loadImage("images/questionMark.png");

	public static final String COMMON_CSS = loadStylesheet("css/common.css");
	public static final String MAIN_SCREEN_CSS = loadStylesheet("css/mainScreen.css");
	public static final String PLAYER_LOBBY_PANEL_SCREEN_CSS = loadStylesheet("css/playerLobbyPane.css");
	public static final String CREATE_LOBBY_CSS = loadStylesheet("css/createLobby.css");
	public static final String PLAYER_PANE_CSS = loadStylesheet("css/playerPane.css");
	public static final String JOIN_LOBBY_CSS = loadStylesheet("css/joinLobby.css");

	/**
	 * Utility classes should not have instances
	 */
	private UIUtil() {
	}

	/**
	 * Loads external fonts into the memory
	 */
	public static void loadFonts() {
		loadFont("fonts/kabel/Kabel-Bold.ttf");
		loadFont("fonts/recoleta/RecoletaAlt-Bold.ttf");
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
	private static Font calculateFittingFont(double width, double height, String fontFamily, String text) {
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
	 * Changes the font of the given component so that it fits in the given area
	 *
	 * @param labeled The component to fit
	 * @param width   The width of the area
	 * @param height  The height of the area
	 */
	public static void fitFont(Labeled labeled, double width, double height) {
		labeled.setFont(calculateFittingFont(width, height, labeled.getFont().getFamily(), labeled.getText()));
	}

	/**
	 * Changes the font of the given component so that it fits in the given area
	 *
	 * @param textInput The component to fit
	 * @param width     The width of the area
	 * @param height    The height of the area
	 */
	public static void fitFont(TextInputControl textInput, double width, double height) {
		textInput.setFont(calculateFittingFont(width, height, textInput.getFont().getFamily(), textInput.getText()));
	}

	/**
	 * Changes the font of the given component so that it fits in the given area
	 *
	 * @param text   The component to fit
	 * @param width  The width of the area
	 * @param height The height of the area
	 */
	public static void fitFont(Text text, double width, double height) {
		text.setFont(calculateFittingFont(width, height, text.getFont().getFamily(), text.getText()));
	}

	/**
	 * Changes the font of the given button so that it fits in the given area.
	 * Changes the font using CSS.
	 *
	 * @param button The button to fit
	 * @param width  The width of the area
	 * @param height The height of the area
	 */
	public static void fitFont(Button button, double width, double height) {
		double buttonFontSize = calculateFittingFont(width, height, button.getFont().getFamily(), button.getText())
				.getSize();
		button.setStyle("-fx-font-size: " + buttonFontSize);
	}

	private static Image loadImage(String path) {
		return new Image(UIUtil.class.getResourceAsStream(path));
	}

	private static String loadStylesheet(String path) {
		return UIUtil.class.getResource(path).toExternalForm();
	}

	private static void loadFont(String path) {
		Font.loadFont(UIUtil.class.getResourceAsStream(path), 20);
	}
}
