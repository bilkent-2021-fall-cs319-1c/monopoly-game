package monopoly.ui.controller.gameplay.titledeed;

import java.io.IOException;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import lombok.Getter;
import monopoly.network.packet.important.packet_data.gameplay.property.TitleDeedPacketData;
import monopoly.ui.UIUtil;

/**
 * Models the deed card of utilities (Water Works and Electric Company)
 *
 * @author Ege Kaan Gürkan
 * @version 17/12/2020
 */
public class UtilitiesTileDeedPane extends MigPane implements DeedCard {
	@FXML
	private ImageView image;
	@FXML
	private Text utilityTitle;
	@FXML
	private Text firstPar;
	@FXML
	private Text secondPar;

	@Getter
	private String name;

	@Getter
	private int buyCost;

	public UtilitiesTileDeedPane(TitleDeedPacketData deedData) {
		this.name = deedData.getTitle();
		buyCost = deedData.getBuyPrice();

		FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/UtilitiesTileDeedPane.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void initialize() {
		setIconAndName();
		firstPar.setText("If one utility is owned,\nrent is 4 times amount shown on dice.");
		secondPar.setText("If both utilities are owned,\nrent is 10 time amount shown on dice.");

		layoutBoundsProperty().addListener((observable, oldVal, newVal) -> adjustSize());
	}

	private void adjustSize() {
		double width = getWidth();
		double height = getHeight();

		UIUtil.fitFont(utilityTitle, width, height * 0.6);
		UIUtil.fitFont(firstPar, width, height * 0.9);
		UIUtil.fitFont(secondPar, width, height * 0.9);
		image.setFitWidth(width * 0.6);
	}

	private void setIconAndName() {
		if ("ELECTRIC\nCOMPANY".equals(name)) {
			utilityTitle.setText(name);
			image.setImage(UIUtil.LIGHTBULB);
		} else {
			utilityTitle.setText(name);
			image.setImage(UIUtil.FAUCET);
		}
	}
}
