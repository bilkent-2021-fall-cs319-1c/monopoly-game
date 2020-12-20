package monopoly.ui.controller.gameplay;

import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import monopoly.ui.UIUtil;
import org.tbee.javafx.scene.layout.fxml.MigPane;

import java.io.IOException;
import java.util.stream.Stream;

public class AuctionPane extends MigPane {

    @FXML
    private MigPane middleWrapper;
    @FXML
    private MigPane addDeedCard;
    @FXML
    private Text auctionText;
    @FXML
    private Text highestBidText;
    @FXML
    private Text bidHereText;
    @FXML
    private TextField bidTextField;
    @FXML
    private Button bidButton;
    @FXML
    private Button passButton;

    private DeedCard deedCard;

    public AuctionPane(@NamedArg("deedCard") DeedCard deedCard) {

        FXMLLoader loader = new FXMLLoader(UIUtil.class.getResource("fxml/AuctionPane.fxml"));
        loader.setController(this);
        loader.setRoot(this);

        this.deedCard = deedCard;

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
        addDeedCard.getChildren().add((Node) deedCard);
    }

    public void adjustSize() {
        double height = getHeight();
        double width = getWidth();

        UIUtil.fitFont(auctionText, width, height * 0.05);
        UIUtil.fitFont(highestBidText, width, height * 0.04);
        UIUtil.fitFont(bidHereText, width, height * 0.04);

        UIUtil.fitFont(bidButton, width * 0.03, height * 0.03);
        UIUtil.fitFont(passButton, width * 0.04, height * 0.04);
        UIUtil.fitFont(bidTextField, Double.MAX_VALUE, height * 0.03);

    }

    public void updateHighestBid(int newBid) {
        highestBidText.setText("Highest Bid: " + newBid);
    }

}
