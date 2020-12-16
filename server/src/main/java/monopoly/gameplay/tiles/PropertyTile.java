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
public class PropertyTile extends Tile{
    @Getter
    @Setter

    private Property property;

    public PropertyTile(String name, String description, Property property) {
        super(name, description);
        this.property = property;
    }

    @Override
    public void doAction(GamePlayer player) {

        if (!player.equals(property.getOwner()))
            player.setBalance(player.getBalance() - property.getRentCost());
    }
}
