package monopoly.ui;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

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

	final ObservableList<LobbyDisplayData> lobbies = FXCollections.observableArrayList(
			new LobbyDisplayData("My Room 0", "123", 4, 6, false),
			new LobbyDisplayData("My Room 1", "123", 2, 6, false),
			new LobbyDisplayData("My Room 2", "123", 3, 6, true),
			new LobbyDisplayData("My Room 3", "123", 5, 6, false)
	);

	public JoinLobbyController() {
		widthListener = (observable, oldValue, newValue) -> windowWidthChanged();
		heightListener = (observable, oldValue, newValue) -> windowHeightChanged();
	}

	@FXML
	public void initialize() {
		lobbyTablePagination.setPageCount(lobbies.size() / 2);
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
		mainTitle.setFont(new Font(UIUtil.calculateFittingFontSize(width * 0.18, height, mainTitle.getText())));
		promptText.setFont(new Font(UIUtil.calculateFittingFontSize(width * 0.12, height, promptText.getText())));
		infoText.setFont(new Font(UIUtil.calculateFittingFontSize(width * 0.35, height, infoText.getText())));
		roomTitle.setFont(new Font(UIUtil.calculateFittingFontSize(width * 0.10, height, roomTitle.getText())));
		passwordTitle
				.setFont(new Font(UIUtil.calculateFittingFontSize(width * 0.075, height, passwordTitle.getText())));
		roomName.setFont(new Font(UIUtil.calculateFittingFontSize(height, width * 0.011, roomName.getText())));
		passwordValue
				.setFont(new Font(UIUtil.calculateFittingFontSize(height, width * 0.010, passwordValue.getText())));
		joinButton.setFont(new Font(UIUtil.calculateFittingFontSize(joinButton.getWidth() - 5,
				joinButton.getHeight() - 5, joinButton.getText())));
	}

	private void windowHeightChanged() {
		double height = rootPane.getScene().getHeight();
		double width = rootPane.getScene().getWidth();
		rootPane.setMaxHeight(height);

		joinButton.setPrefHeight(height * 0.04);
		setFontSizes(height, width);

	}

	public Node createPage(int pageIndex) {
		int fromIndex = pageIndex * 2;
		int toIndex = fromIndex + 2;
		lobbyTable.setItems(FXCollections.observableArrayList(lobbies.subList(fromIndex, toIndex)));

		return new BorderPane(lobbyTable);
	}

	private void windowWidthChanged() {
		double height = rootPane.getScene().getHeight();
		double width = rootPane.getScene().getWidth();
		rootPane.setMaxWidth(width);

		joinButton.setPrefWidth(width * 0.08);
		setFontSizes(height, width);

	}

}
