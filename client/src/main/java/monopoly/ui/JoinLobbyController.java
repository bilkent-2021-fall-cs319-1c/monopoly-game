package monopoly.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class JoinLobbyController {
	@FXML
	private StackPane rootPane;
	@FXML
	private MigPane mainPane;
	@FXML
	private BorderPane rightPane;
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
	private TableView<Lobby> lobbyTable;
	@FXML
	public TableColumn<Lobby, Image> ownerImageCol;
	@FXML
	public TableColumn<Lobby, String> lobbyNameCol;
	@FXML
	public TableColumn<Lobby, String> playerNumCol;
	@FXML
	private Pagination lobbyTablePagination;


	private ChangeListener<Number> widthListener;
	private ChangeListener<Number> heightListener;

	final ObservableList<Lobby> lobbies = FXCollections.observableArrayList(
			new Lobby("My Room 0", "123", UIUtil.DEFAULT_PLAYER_IMAGE, 4, false),
			new Lobby("My Room 1", "123", UIUtil.DEFAULT_PLAYER_IMAGE, 2, false),
			new Lobby("My Room 2", "123", UIUtil.DEFAULT_PLAYER_IMAGE, 3, true),
			new Lobby("My Room 3", "123", UIUtil.DEFAULT_PLAYER_IMAGE, 5, false)
			);


	public JoinLobbyController() {
		widthListener = (observable, oldValue, newValue) -> windowWidthChanged();
		heightListener = (observable, oldValue, newValue) -> windowHeightChanged();
	}

	@FXML
	public void initialize() {

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
		mainTitle.setFont(
				new Font(UIUtil.calculateFittingFontSize( width * 0.18, height, mainTitle.getText())));
		promptText.setFont(
				new Font(UIUtil.calculateFittingFontSize( width * 0.12, height, promptText.getText())));
		infoText.setFont(
				new Font(UIUtil.calculateFittingFontSize( width * 0.35, height, infoText.getText())));
		roomTitle.setFont(
				new Font(UIUtil.calculateFittingFontSize( width * 0.10, height, roomTitle.getText())));
		passwordTitle.setFont(
				new Font(UIUtil.calculateFittingFontSize( width * 0.075, height, passwordTitle.getText())));
		roomName.setFont(
				new Font(UIUtil.calculateFittingFontSize(height, width * 0.011, roomName.getText())));
		passwordValue.setFont(
				new Font(UIUtil.calculateFittingFontSize(height, width * 0.010, passwordValue.getText())));
		joinButton.setFont(new Font(UIUtil.calculateFittingFontSize(joinButton.getWidth() - 5, joinButton.getHeight() - 5, joinButton.getText())));
	}
	private void windowHeightChanged() {
		double height = rootPane.getScene().getHeight();
		double width = rootPane.getScene().getWidth();
		rootPane.setMaxHeight(height);


		joinButton.setPrefHeight(height * 0.04);
		joinButton.setMinHeight(height * 0.04);
		//joinButton.setMaxHeight(height * 0.04);
		setFontSizes(height, width);

	}
    
	public Node createPage(int pageIndex) {
		int fromIndex = pageIndex * 2;
		int toIndex = Math.min(fromIndex + 2, lobbies.size());
		lobbyTable.setItems(FXCollections.observableArrayList(lobbies.subList(fromIndex, toIndex)));

		return new BorderPane(lobbyTable);
	}

	private void windowWidthChanged() {
		double height = rootPane.getScene().getHeight();
		double width = rootPane.getScene().getWidth();
		rootPane.setMaxWidth(width);


		joinButton.setPrefWidth(width * 0.08);
		joinButton.setMinWidth(width * 0.08);
		//joinButton.setMaxWidth(width * 0.08);
		setFontSizes(height, width);

	}


}
