package monopoly.gameplay;

import lombok.Getter;

/**
 * GO tile
 *
 * @author Alper Sari
 * @version Dec 15, 2020
 */

public class GoTile extends Tile{
    @Getter
    private final int MONEY_GAIN = 200;

    public GoTile(String name, String description) {
        super(name, description);
    }

    @Override
    public void doAction(GamePlayer player) {
        player.setBalance(player.getBalance() + MONEY_GAIN); //Get 200
    }
}
