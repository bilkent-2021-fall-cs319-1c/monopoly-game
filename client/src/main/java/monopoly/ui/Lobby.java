package monopoly.ui;

import javafx.beans.property.*;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class Lobby {

    private ArrayList<String> players;
    private ArrayList<Image> playerImages;

    private transient StringProperty lobbyNameProp;
    private transient StringProperty passwordProp;
    private transient IntegerProperty sizeProp;
    private transient IntegerProperty playerCountProp;
    private transient BooleanProperty isPrivateProp;
    private transient SimpleObjectProperty<Image> ownerImageProp;


    public Lobby(String lobbyName, String password, Image ownerAvatar, int size,
                 boolean isPrivate) {

        this.lobbyNameProp = new SimpleStringProperty(lobbyName);
        this.passwordProp = new SimpleStringProperty(password);
        this.sizeProp = new SimpleIntegerProperty(size);
        this.isPrivateProp = new SimpleBooleanProperty(isPrivate);
        this.playerCountProp = new SimpleIntegerProperty(0);
        this.ownerImageProp = new SimpleObjectProperty<>(ownerAvatar);

    }

    public boolean addPlayer(String name, Image image) {
        if (playerCountProp.get() >= sizeProp.get()) {
            return false;
        } else {
            // TODO: Player class
            players.add(name);
            playerImages.add(image);
            playerCountProp.add(1);
            return true;
        }
    }
}


