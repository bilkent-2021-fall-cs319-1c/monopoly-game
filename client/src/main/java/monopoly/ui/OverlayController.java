package monopoly.ui;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.Setter;

public class OverlayController implements MonopolyUIController {
	@FXML
	private MigPane root;
	@FXML
	private Label title;
	@FXML
	private Text info;

	@Setter
	private ClientApplication app;

	@FXML
	private void closeOverlay() {
		app.closeOverlay();
	}

	@Override
	public void sizeChanged(double width, double height) {
		setFonts(width, height);
	}

	private void setFonts(double width, double height) {
		System.out.println(width + " " + height);

		UIUtil.fitFont(title, width * 0.45, height * 0.09);
		info.setFont(Font.font(title.getFont().getSize() * 0.5));
	}
}
