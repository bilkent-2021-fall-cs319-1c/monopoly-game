package monopoly.gameplay.tiles;

import lombok.Getter;
import monopoly.gameplay.GamePlayer;

/**
 * GO tile
 *
 * @author Alper Sari
 * @version Dec 15, 2020
 */
@Getter
public class GoTile extends Tile{

    private final int MONEY_GAIN = 200;

    public GoTile(String name, String description, int index) {
        super(name, description, index);
    }

    @Override
    public void doAction(GamePlayer player) {
        player.setBalance(player.getBalance() + MONEY_GAIN); //Get 200
    }
}
