package monopoly.ui.join_lobby;

import java.io.IOException;

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
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import lombok.Setter;
import monopoly.Error;
import monopoly.network.packet.important.packet_data.LobbyPacketData;
import monopoly.ui.ClientApplication;
import monopoly.ui.MonopolyUIController;
import monopoly.ui.UIUtil;

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
	private StackPane rootPane;
	@FXML
	private MigPane mainPane;
	@FXML
	private MigPane rightPane;
	@FXML
	private MigPane leftPane;
	@FXML
	private Text mainTitle;
	@FXML
	private Text promptText;
	@FXML
	private Text infoText;
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

		joinButton.getStyleClass().add("buttonRegular");
		joinButton.getStyleClass().add("joinButton");
		updateLobbyCount();

		lobbyTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && newValue.getIsPrivate()) {
				passwordValue.setDisable(false);
			} else {
				passwordValue.setDisable(true);
				passwordValue.clear();
			}
		});
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
	 * @param pageIndex
	 * @return An empty meaningless node. It is returned for correct JavaFX paging
	 *         API.
	 */
	private Node createPage(int pageIndex) {
		updateLobbyCount();

		int fromIndex = pageIndex * ROWS_PER_PAGE;
		int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, lobbyCount.get());
		lobbies.clear();
		app.getNetworkManager().getLobbies(fromIndex, toIndex)
				.forEach(lobby -> lobbies.add(new LobbyDisplayData(lobby)));

		return new Text("");
	}

	@FXML
	private void joinLobby() throws IOException {
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

	@Override
	public void sizeChanged(double width, double height) {
		joinButton.setPrefWidth(width * 0.08);
		joinButton.setPrefHeight(height * 0.04);

		setFontSizes(height, width);
		lobbyTable.setFixedCellSize(height * 0.86 / 10);
	}

	/**
	 * Called when the bounds of this pane is changed. Responsive UIs should use
	 * this method to update their font sizes and scaling
	 * 
	 * @param width  The total width of this container
	 * @param height The total height of this container
	 */
	private void setFontSizes(double height, double width) {
		UIUtil.fitFont(mainTitle, width * 0.18, height);
		UIUtil.fitFont(promptText, width * 0.12, height);
		UIUtil.fitFont(infoText, width * 0.35, height);
		UIUtil.fitFont(roomTitle, width * 0.10, height);
		UIUtil.fitFont(passwordTitle, width * 0.075, height);
//		UIUtil.fitFont(roomName, width * 0.10, height);
//		UIUtil.fitFont(passwordValue, width * 0.10, height);
//		UIUtil.fitFont(joinButton, width * 0.10, height);
	}

}
