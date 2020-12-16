package monopoly.gameplay;

import lombok.Getter;
import lombok.Setter;

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

    public Tile(String name, String description)
    {
        this.name = name;
        this.description = description;
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
