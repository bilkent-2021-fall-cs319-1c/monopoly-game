package monopoly.gameplay.tiles;

import lombok.Getter;
import lombok.Setter;
import monopoly.gameplay.GamePlayer;
import monopoly.gameplay.Property;

/**
 * Tile with property
 *
 * @author Alper Sari
 * @version Dec 15, 2020
 */
@Getter
@Setter
public class PropertyTile extends Tile{

    private Property property;

    public PropertyTile(String name, String description, int index, Property property) {
        super(name, description, index);
        this.property = property;
    }

    @Override
    public void doAction(GamePlayer player) {

        if (!player.equals(property.getOwner()))
            player.setBalance(player.getBalance() - property.getRentCost());
    }
}
