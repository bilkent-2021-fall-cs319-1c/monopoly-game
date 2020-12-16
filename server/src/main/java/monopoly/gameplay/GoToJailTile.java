package monopoly.gameplay;

/**
 * Tile that sends player to jail
 *
 * @author Alper Sari
 * @version Dec 15, 2020
 */

public class GoToJailTile extends Tile{
    public GoToJailTile(String name, String description) {
        super(name, description);
    }

    @Override
    public void doAction(GamePlayer player) {
        player.goToJail();
    }
}
