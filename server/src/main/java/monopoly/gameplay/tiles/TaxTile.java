package monopoly.gameplay.tiles;

import lombok.Getter;
import lombok.Setter;
import monopoly.gameplay.GamePlayer;

/**
 * Tile which takes taxes from the player
 *
 * @author Alper Sari
 * @version Dec 15, 2020
 */
@Getter
@Setter
public class TaxTile extends Tile{

    private int amount;

    public TaxTile(String name, String description,int index, int amount) {
        super(name, description, index);
        this.amount = amount;
    }

    @Override
    public void doAction(GamePlayer player) {
        player.setBalance(player.getBalance() - amount);
    }
}
