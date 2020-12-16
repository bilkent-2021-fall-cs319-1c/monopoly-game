package monopoly.gameplay.tiles;

import lombok.Getter;
import lombok.Setter;
import monopoly.gameplay.Actionable;
import monopoly.gameplay.GamePlayer;

/**
 * Tile parent class
 *
 * @author Alper Sari
 * @version Dec 15, 2020
 */
@Getter
@Setter
public class Tile implements Actionable{

    private String name;
    private String description;
    private int index;

    public Tile(String name, String description, int index)
    {
        this.name = name;
        this.description = description;
        this.index = index;
    }

    /**
     * Performs the tile action
     *
     * @param player player subject
     */
    @Override
    public void doAction(GamePlayer player) {
        //Do nothing in parent
    }
}
