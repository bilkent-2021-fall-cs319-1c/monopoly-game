package monopoly.gameplay;

import lombok.Getter;
import lombok.Setter;

/**
 * Tile which takes taxes from the player
 *
 * @author Alper Sari
 * @version Dec 15, 2020
 */

public class TaxTile extends Tile{
    @Getter
    @Setter

    private int amount;

    public TaxTile(String name, String description, int amount) {
        super(name, description);
        this.amount = amount;
    }

    @Override
    public void doAction(GamePlayer player) {
        player.setBalance(player.getBalance() - amount);
    }
}
