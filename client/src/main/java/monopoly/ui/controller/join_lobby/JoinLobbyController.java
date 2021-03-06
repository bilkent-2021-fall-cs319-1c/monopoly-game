package monopoly.ui.controller.join_lobby;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.Setter;
import monopoly.Error;
import monopoly.network.packet.important.packet_data.lobby.LobbyListPacketData;
import monopoly.network.packet.important.packet_data.lobby.LobbyPacketData;
import monopoly.ui.ClientApplication;
import monopoly.ui.UIUtil;
import monopoly.ui.controller.MonopolyUIController;

/**
 * Controls the join lobby UI
 *
 * @author Ziya Mukhtarov
 * @version Dec 13, 2020
 */
public class JoinLobbyController implements MonopolyUIController {
	/**
	 * The number of lobby rows that is displayed in one page
	 */
	private static final int ROWS_PER_PAGE = 10;

	@Setter
	private ClientApplication app;

	@FXML
	private MigPane mainPane;
	@FXML
	private ImageView backIcon;
	@FXML
	private MigPane rightPane;
	@FXML
	private MigPane leftPane;
	@FXML
	private Text mainTitle;
	@FXML
	private Text promptText;
	@FXML
	private Text roomTitle;
	@FXML
	private TextField roomName;
	@FXML
	private Text passwordTitle;
	@FXML
	private TextField passwordValue;
	@FXML
	private Button joinButton;
	@FXML
	private TableView<LobbyDisplayData> lobbyTable;
	@FXML
	private Pagination lobbyTablePagination;

	private IntegerProperty lobbyCount;
	private ObservableList<LobbyDisplayData> lobbies;

	public JoinLobbyController() {
		lobbyCount = new SimpleIntegerProperty(0);
		lobbies = FXCollections.observableArrayList();
	}

	@FXML
	public void initialize() {
		lobbyTable.setItems(lobbies);
		lobbyTablePagination.pageCountProperty()
				.bind(Bindings.divide(Bindings.add(lobbyCount, ROWS_PER_PAGE - 1), ROWS_PER_PAGE));
		lobbyTablePagination.setPageFactory(this::createPage);

		updateLobbyCount();

		lobbyTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && newValue.getIsPrivate()) {
				passwordValue.setDisable(false);
				passwordValue.setPromptText("Enter Password");
			} else {
				passwordValue.setDisable(true);
				passwordValue.setPromptText("No Private Lobby Selected");
				passwordValue.clear();
			}
		});

		mainPane.setBackground(new Background(
				new BackgroundImage(UIUtil.BACKGROUND_IMAGE5, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
						BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, true))));

		String fontAvenir = "Avenir Next";
		mainTitle.setFont(Font.font("Kabel"));
		promptText.setFont(Font.font(fontAvenir));
		roomTitle.setFont(Font.font(fontAvenir));
		roomName.setFont(Font.font(fontAvenir));
		passwordTitle.setFont(Font.font(fontAvenir));
		passwordValue.setFont(Font.font(fontAvenir));

	}

	/**
	 * Updates the number of available lobbies from the server.
	 */
	public void updateLobbyCount() {
		if (app != null)
			lobbyCount.set(app.getNetworkManager().getNumberOfLobbies());
	}

	/**
	 * Creates the lobby page for the given page index. The data for that page is
	 * taken from the server.
	 *
	 * @return An empty meaningless component. It is returned for correct JavaFX
	 *         paging API.
	 */
	private Node createPage(int pageIndex) {
		updateLobbyCount();

		int fromIndex = pageIndex * ROWS_PER_PAGE;
		int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, lobbyCount.get());
		lobbies.clear();

		LobbyListPacketData lobbyDatas = app.getNetworkManager().getLobbies(fromIndex, toIndex);
		if (lobbyDatas != null)
			lobbyDatas.getLobbies().forEach(lobby -> lobbies.add(new LobbyDisplayData(lobby)));

		return new Pane();
	}

	@FXML
	private void joinLobby() {
		LobbyDisplayData lobby = lobbyTable.getSelectionModel().getSelectedItem();
		if (lobby == null) {
			app.displayError(
					new Error("No Lobby Selected", "Please select a lobby from the table on the right to join."));
			return;
		}

		LobbyPacketData packetData = lobby.getPacketData();
		packetData.setPassword(passwordValue.getText());
		boolean success = app.getNetworkManager().joinLobby(lobby.getPacketData());
		if (success) {
			app.switchToView("fxml/Lobby.fxml");
		}
	}

	@FXML
	private void back() {
		app.switchToView("fxml/MainMenu.fxml");
	}

	@Override
	public void sizeChanged(double width, double height) {
		backIcon.setFitWidth(width * 0.03);
		backIcon.setFitHeight(height * 0.1);

		lobbyTable.setFixedCellSize((height * 0.9 - 50) / ROWS_PER_PAGE);

		lobbyTablePagination.setStyle("-fx-min-width_:" + width * 0.04 + ";-fx-min-height_:" + height * 0.04);

		setFontSizes(width, height);
	}

	/**
	 * Called when the bounds of this pane is changed. Responsive UIs should use
	 * this method to update their font sizes and scaling
	 *
	 * @param width  The total width of this container
	 * @param height The total height of this container
	 */
	private void setFontSizes(double width, double height) {
		UIUtil.fitFont(mainTitle, width * 0.27, height * 0.15);
		UIUtil.fitFont(promptText, width * 0.35, height * 0.14);

		UIUtil.fitFont(roomTitle, width * 0.15, height * 0.07);
		passwordTitle.setFont(roomTitle.getFont());
		UIUtil.fitFont(passwordValue, Double.MAX_VALUE, height * 0.04);
		roomName.setFont(passwordValue.getFont());

		UIUtil.fitFont(joinButton, width * 0.3, height * 0.05);

		Node paginationControlBox = lobbyTablePagination.lookup(".control-box");
		if (paginationControlBox != null)
			paginationControlBox.setStyle("-fx-font-size:" + height * 0.02);

		lobbyTable.setStyle("-fx-font-size: " + lobbyTable.getFixedCellSize() * 0.25);
	}
}
