package monopoly.ui;

import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.tbee.javafx.scene.layout.fxml.MigPane;

import java.io.IOException;
import java.util.stream.Stream;

public class PlayerLobbyPane extends MigPane {

    enum UserType {
        ADMIN,
        REGULAR
    }

    @FXML
    private ImageView playerImage;
    @FXML
    private Text playerName;
    @FXML
    private MigPane buttonGroup;
    @FXML
    private Button makeAdminButton;
    @FXML
    private Button removeButton;

    private String userType;
    private Image userImage;
    private String name;

    public PlayerLobbyPane(@NamedArg("userType") String userType, @NamedArg("name") String name, @NamedArg("userImage") Image userImage) throws IOException {

        this.userType = userType;
        this.name = name;
        this.userImage = userImage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/PlayerLobbyPane.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();

        Stream.of(widthProperty(), heightProperty())
                .forEach(p -> p.addListener((observable, oldVal, newVal) -> adjustSize()));

    }

    public void adjustSize() {
        double width = getWidth();
        double height = getHeight();

        playerName.setFont(UIUtil.calculateFittingFontSize(width * 0.3, height * 0.3, playerName.getText()));

        playerImage.setFitHeight(height * 0.84);
        playerImage.setFitWidth(width * 0.84);

    }

    @FXML
    private void initialize() {

        if ("regular".equals(userType)) {
            setCols("[::%19]1%[::80%]");
            buttonGroup.setVisible(false);
        } else if ("admin".equals(userType)) {
            playerName.setText(playerName.getText() + " (Admin)");
        }

    }

}
