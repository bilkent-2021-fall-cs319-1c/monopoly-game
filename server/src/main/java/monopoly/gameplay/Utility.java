package monopoly.gameplay;

import monopoly.gameplay.tiles.PropertyTile;

/**
 * Utility property class
 *
 * @author Alper Sari
 * @version Dec 16, 2020
 */

public class Utility extends Property{
    public Utility(PropertyTile tile, TitleDeedData titleDeed, ColorSet colorSet) {
        super(tile, titleDeed, colorSet);
    }

    @Override
    public int getRentCost() {
        int ownedUtilities = 0;

        for (int i = 0; i < getOwner().getProperties().size(); i++)
        {
            if (getOwner().getProperties().get(i) instanceof Railroad)
                ownedUtilities++;
        }

        return getTitleDeed().getRentTierPrice(ownedUtilities);
    }
}
