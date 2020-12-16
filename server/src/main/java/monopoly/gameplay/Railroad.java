package monopoly.gameplay;

import monopoly.gameplay.tiles.PropertyTile;

/**
 * Railroad property, can't take any buildings
 *
 * @author Alper Sari
 * @version Dec 16, 2020
 */

public class Railroad extends Property{
    public Railroad(PropertyTile tile, GamePlayer owner, TitleDeedData titleDeed, ColorSet colorSet) {
        super(tile, owner, titleDeed, colorSet);
    }

    @Override
    public int getRentCost() {
        int ownedRailroads = 0;

        for (int i = 0; i < getOwner().properties.size(); i++)
        {
            if (getOwner().properties.get(i) instanceof Railroad)
                ownedRailroads++;
        }

        if (ownedRailroads < 5)
            return titleDeed.getRentTier(ownedRailroads);
        else
            return -1; //Can't own more than 4 railroads
    }
}
