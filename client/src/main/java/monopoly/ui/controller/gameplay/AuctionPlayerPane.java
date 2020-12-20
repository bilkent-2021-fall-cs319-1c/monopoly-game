package monopoly.ui.controller.gameplay;

import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.text.Text;
import monopoly.ui.UIUtil;
import org.tbee.javafx.scene.layout.fxml.MigPane;

import java.io.IOException;
import java.util.stream.Stream;

public class AuctionPlayerPane extends MigPane {

    @FXML
    private MigPane yourTurnWrapper;
    @FXML
    private MigPane playerNameWrapper;
    @FXML
    private Text playerNameText;
    @FXML
    private Text yourTurnText;


    private String color;
    private String playerName;
    private boolean isTurn;

    public AuctionPlayerPane(@NamedArg("color") String color, @NamedArg("playerName") String playerName,
                             @NamedArg("isTurn") boolean isTurn) {

        FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/AuctionPlayerPane.fxml"));
        loader.setController(this);
        loader.setRoot(this);

        this.color = color;
        this.playerName = playerName;
        this.isTurn = isTurn;

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stream.of(widthProperty(), heightProperty())
                .forEach(p -> p.addListener((observable, oldVal, newVal) -> adjustSize()));

    }

    @FXML
    public void initialize() {

        playerNameText.setText(playerName);
        playerNameWrapper.setStyle("-fx-background-color: " + color + ";");
        if (!isTurn) {
            yourTurnWrapper.setVisible(false);
        }

    }

    private void adjustSize() {
        double height = getHeight();
        double width = getWidth();

        UIUtil.fitFont(playerNameText, width * 0.5, height * 0.3);
        UIUtil.fitFont(yourTurnText, width * 0.5, height * 0.2);
    }

}
