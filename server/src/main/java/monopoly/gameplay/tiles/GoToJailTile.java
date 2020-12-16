package monopoly.gameplay.tiles;

import monopoly.gameplay.GamePlayer;

/**
 * Tile that sends player to jail
 *
 * @author Alper Sari
 * @version Dec 15, 2020
 */

public class GoToJailTile extends Tile{
    public GoToJailTile(String name, String description, int index) {
        super(name, description, index);
    }

    @Override
    public void doAction(GamePlayer player) {
        player.goToJail();
    }
}
