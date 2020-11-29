package monopoly.ui.join_lobby;

import java.util.ArrayList;
import java.util.Random;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import com.sun.javafx.collections.ObservableListWrapper;

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
import monopoly.ui.UIUtil;

public class JoinLobbyController {
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

	private ObservableList<LobbyDisplayData> lobbies;

	public JoinLobbyController() {
		// Load test data
		lobbies = new ObservableListWrapper<>(new ArrayList<>());
		Random rand = new Random();
		for (int i = 0; i < 34; i++) {
			int limit = rand.nextInt(5) + 2;
			lobbies.add(new LobbyDisplayData(new LobbyPacketData(i, "Room " + i, "", rand.nextBoolean(), "Player " + i,
					rand.nextInt(limit), limit)));
		}

		widthListener = (observable, oldValue, newValue) -> windowWidthChanged();
		heightListener = (observable, oldValue, newValue) -> windowHeightChanged();
	}

	@FXML
	public void initialize() {
		lobbyTablePagination.setPageCount(lobbies.size() / 10 + (lobbies.size() % 10 != 0 ? 1 : 0));
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
	}

	private void setFontSizes(double height, double width) {
		mainTitle.setFont(UIUtil.calculateFittingFontSize(width * 0.18, height, mainTitle.getText()));
		promptText.setFont(UIUtil.calculateFittingFontSize(width * 0.12, height, promptText.getText()));
		infoText.setFont(UIUtil.calculateFittingFontSize(width * 0.35, height, infoText.getText()));
		roomTitle.setFont(UIUtil.calculateFittingFontSize(width * 0.10, height, roomTitle.getText()));
		passwordTitle.setFont(UIUtil.calculateFittingFontSize(width * 0.075, height, passwordTitle.getText()));
		roomName.setFont(UIUtil.calculateFittingFontSize(height, width * 0.011, roomName.getText()));
		passwordValue.setFont(UIUtil.calculateFittingFontSize(height, width * 0.010, passwordValue.getText()));
		joinButton.setFont(UIUtil.calculateFittingFontSize(joinButton.getWidth() - 5, joinButton.getHeight() - 5,
				joinButton.getText()));
	}

	public Node createPage(int pageIndex) {
		int fromIndex = pageIndex * 10;
		int toIndex = Math.min(fromIndex + 10, lobbies.size());
		lobbyTable.setItems(FXCollections.observableArrayList(lobbies.subList(fromIndex, toIndex)));

		return new Text("");
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
}
