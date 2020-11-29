package monopoly.ui.join_lobby;

import java.io.IOException;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
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
import monopoly.network.packet.important.packet_data.LobbyPacketData;
import monopoly.ui.ClientApplication;
import monopoly.ui.UIUtil;

public class JoinLobbyController {
	private static final int ROWS_PER_PAGE = 10;

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

	private ChangeListener<Number> widthListener;
	private ChangeListener<Number> heightListener;

	private IntegerProperty lobbyCount;
	private ObservableList<LobbyDisplayData> lobbies;

	public JoinLobbyController() {
		lobbyCount = new SimpleIntegerProperty(0);
		lobbies = FXCollections.observableArrayList();

		widthListener = (observable, oldValue, newValue) -> windowWidthChanged();
		heightListener = (observable, oldValue, newValue) -> windowHeightChanged();
	}

	@FXML
	public void initialize() {
		lobbyTable.setItems(lobbies);
		lobbyTablePagination.pageCountProperty()
				.bind(Bindings.divide(Bindings.add(lobbyCount, ROWS_PER_PAGE - 1), ROWS_PER_PAGE));
		lobbyTablePagination.setPageFactory(this::createPage);

		rootPane.sceneProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue != null) {
				oldValue.widthProperty().removeListener(widthListener);
				oldValue.heightProperty().removeListener(heightListener);
			}
			if (newValue != null) {
				newValue.widthProperty().addListener(widthListener);
				newValue.heightProperty().addListener(heightListener);

				windowHeightChanged();
				windowWidthChanged();
			}
		});

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
		lobbyCount.set(ClientApplication.getInstance().getNetworkManager().getNumberOfLobbies());
	}

	private Node createPage(int pageIndex) {
		updateLobbyCount();

		int fromIndex = pageIndex * ROWS_PER_PAGE;
		int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, lobbyCount.get());
		lobbies.clear();
		ClientApplication.getInstance().getNetworkManager().getLobbies(fromIndex, toIndex)
				.forEach(lobby -> lobbies.add(new LobbyDisplayData(lobby)));

		return new Text("");
	}

	@FXML
	private void joinLobby() throws IOException {
		LobbyDisplayData lobby = lobbyTable.getSelectionModel().getSelectedItem();
		if (lobby == null) {
			// TODO Show error
			return;
		}

		LobbyPacketData packetData = lobby.getPacketData();
		packetData.setPassword(passwordValue.getText());
		boolean success = ClientApplication.getInstance().getNetworkManager().joinLobby(lobby.getPacketData());
		if (success) {
			ClientApplication.getInstance().switchToView("fxml/Lobby.fxml");
		}
	}

	private void windowWidthChanged() {
		double height = rootPane.getScene().getHeight();
		double width = rootPane.getScene().getWidth();
		rootPane.setMaxWidth(width);

		joinButton.setPrefWidth(width * 0.08);
		setFontSizes(height, width);
	}

	private void windowHeightChanged() {
		double height = rootPane.getScene().getHeight();
		double width = rootPane.getScene().getWidth();
		rootPane.setMaxHeight(height);

		joinButton.setPrefHeight(height * 0.04);
		setFontSizes(height, width);

		lobbyTable.setFixedCellSize(height * 0.86 / 10);
	}

	private void setFontSizes(double height, double width) {
		mainTitle.setFont(UIUtil.calculateFittingFont(width * 0.18, height, mainTitle.getText()));
		promptText.setFont(UIUtil.calculateFittingFont(width * 0.12, height, promptText.getText()));
		infoText.setFont(UIUtil.calculateFittingFont(width * 0.35, height, infoText.getText()));
		roomTitle.setFont(UIUtil.calculateFittingFont(width * 0.10, height, roomTitle.getText()));
		passwordTitle.setFont(UIUtil.calculateFittingFont(width * 0.075, height, passwordTitle.getText()));
		roomName.setFont(UIUtil.calculateFittingFont(height, width * 0.011, roomName.getText()));
		passwordValue.setFont(UIUtil.calculateFittingFont(height, width * 0.010, passwordValue.getText()));
		joinButton.setFont(UIUtil.calculateFittingFont(joinButton.getWidth() - 5, joinButton.getHeight() - 5,
				joinButton.getText()));
	}
}
