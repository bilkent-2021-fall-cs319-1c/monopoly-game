package monopoly.ui;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
public class CreateLobbyController {
	@FXML
	private StackPane stackPane;
	@FXML
    public MigPane migPanel;
	@FXML
    public Text mainTitle;
	@FXML
    public Text limitTitle;
	@FXML
	public Spinner<Integer> limitValue;
	@FXML
    public Text roomTitle;
	@FXML
	public TextField roomName;
	@FXML
    public Text passwordTitle;
	@FXML
	public TextField passwordValue;
	@FXML
	public CheckBox checkPriv;
	@FXML
	public Button createButton;
	
	private ChangeListener<Number> widthListener;
	private ChangeListener<Number> heightListener;
	public CreateLobbyController() {
		
		widthListener = (observable, oldValue, newValue) -> windowWidthChanged();
		heightListener = (observable, oldValue, newValue) -> windowHeightChanged();
	}
	@FXML
	public void initialize() {
		stackPane.sceneProperty().addListener((observable, oldValue, newValue) -> {
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
				new Font(UIUtil.calculateFittingFontSize(height, width * 0.055, mainTitle.getText())));
		limitTitle.setFont(
				new Font(UIUtil.calculateFittingFontSize(height, width * 0.03, limitTitle.getText())));
		roomTitle.setFont(
				new Font(UIUtil.calculateFittingFontSize(height, width * 0.03, roomTitle.getText())));
		passwordTitle.setFont(
				new Font(UIUtil.calculateFittingFontSize(height, width * 0.03, passwordTitle.getText())));
		roomName.setFont(
				new Font(UIUtil.calculateFittingFontSize(height, width * 0.014, roomName.getText())));
		passwordValue.setFont(
				new Font(UIUtil.calculateFittingFontSize(height, width * 0.014, passwordValue.getText())));
		
		checkPriv.setFont(
				new Font(UIUtil.calculateFittingFontSize(height, width * 0.02, checkPriv.getText())));
//		System.out.println(createButton.getWidth() + " " + createButton.getHeight());
		createButton.setFont(new Font(UIUtil.calculateFittingFontSize(createButton.getWidth() - 20, createButton.getHeight() - 20, createButton.getText())));
	}
	private void windowHeightChanged() {
		double height = stackPane.getScene().getHeight();
		double width = stackPane.getScene().getWidth();
		stackPane.setMaxHeight(height);
		

		createButton.setPrefHeight(height * 0.04);
		createButton.setMinHeight(height * 0.04);
		createButton.setMaxHeight(height * 0.04);
		setFontSizes(height, width);
		
	}
	private void windowWidthChanged() {
		double height = stackPane.getScene().getHeight();
		double width = stackPane.getScene().getWidth();
		stackPane.setMaxWidth(width);
		

		createButton.setPrefWidth(width * 0.08);
		createButton.setMinWidth(width * 0.08);
		createButton.setMaxWidth(width * 0.08);
		setFontSizes(height, width);
	
	}
}
