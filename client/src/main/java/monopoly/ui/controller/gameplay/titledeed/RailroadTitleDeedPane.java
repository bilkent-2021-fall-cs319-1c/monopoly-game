package monopoly.ui.controller.gameplay.titledeed;

import java.io.IOException;
import java.util.List;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.text.Text;
import lombok.Getter;
import monopoly.network.packet.important.packet_data.gameplay.property.TitleDeedPacketData;
import monopoly.ui.UIUtil;

public class RailroadTitleDeedPane extends MigPane implements DeedCard {
	@FXML
	private MigPane topWrapper;
	@FXML
	private Text tileName;
	@FXML
	private Text rentText;
	@FXML
	private Text rentValueText;
	@FXML
	private Text twoRailroadsText;
	@FXML
	private Text twoRailroadsValue;
	@FXML
	private Text threeRailroadsText;
	@FXML
	private Text threeRailroadsValue;
	@FXML
	private Text fourRailroadsText;
	@FXML
	private Text fourRailroadsValue;
	@FXML
	private Text mortgageValue;

	@Getter
	private String name;
	private String rentString;
	private String twoRailroadRent;
	private String threeRailroadRent;
	private String fourRailroadRent;
	private String mortgageString;

	public RailroadTitleDeedPane(TitleDeedPacketData deedData) {
		name = deedData.getTitle();
		mortgageString = "" + deedData.getMortgagePrice();

		List<Integer> rentCost = deedData.getRentCost();
		rentString = "" + rentCost.get(0);
		twoRailroadRent = "" + rentCost.get(1);
		threeRailroadRent = "" + rentCost.get(2);
		fourRailroadRent = "" + rentCost.get(3);

		FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/RailroadTitleDeedPane.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void initialize() {
		tileName.setText(name);
		topWrapper.setBackground(
				new Background(new BackgroundImage(UIUtil.BOARD, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
						BackgroundPosition.CENTER, new BackgroundSize(0.6, 0.8, true, true, false, false))));

		rentValueText.setText(rentString);
		twoRailroadsValue.setText(twoRailroadRent);
		threeRailroadsValue.setText(threeRailroadRent);
		fourRailroadsValue.setText(fourRailroadRent);
		mortgageValue.setText("Mortgage: " + mortgageString);

		layoutBoundsProperty().addListener((observable, oldVal, newVal) -> Platform.runLater(this::adjustSize));
	}

	private void adjustSize() {
		double width = getWidth();
		double height = getHeight();

		UIUtil.fitFont(tileName, width * 0.25, height * 0.1);

		UIUtil.fitFont(rentText, width * 0.12, height * 0.2);
		UIUtil.fitFont(rentValueText, width * 0.1, height * 0.05);

		UIUtil.fitFont(twoRailroadsText, width * 0.6, height * 0.04);
		UIUtil.fitFont(twoRailroadsValue, width * 0.1, height * 0.05);

		UIUtil.fitFont(threeRailroadsText, width * 0.6, height * 0.04);
		UIUtil.fitFont(threeRailroadsValue, width * 0.1, height * 0.05);

		UIUtil.fitFont(fourRailroadsText, width * 0.6, height * 0.04);
		UIUtil.fitFont(fourRailroadsValue, width * 0.1, height * 0.05);

		UIUtil.fitFont(mortgageValue, width * 0.5, height * 0.04);
	}
}
