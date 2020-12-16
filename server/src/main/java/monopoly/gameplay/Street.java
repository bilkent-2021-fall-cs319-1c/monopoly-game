package monopoly.gameplay;

import lombok.Getter;
import lombok.Setter;
import monopoly.gameplay.tiles.PropertyTile;

import java.util.ArrayList;

/**
 * A property which can have have an amount of hotels and houses on it
 *
 * @author Alper Sari
 * @version Dec 16, 2020
 */

public class Street extends Property{
    @Getter
    @Setter

    private int houseCount;
    private int hotelCount;

    public Street(PropertyTile tile, GamePlayer owner, TitleDeedData titleDeed, ColorSet colorSet) {
        super(tile, owner, titleDeed, colorSet);
        houseCount = 0;
        hotelCount = 0;
    }

    @Override
    public int getRentCost() {
        if(hotelCount == 0 && houseCount < 5)
        {
            return titleDeed.getRentTier(houseCount);
        }
        else if(hotelCount != 0)
            return titleDeed.getRentTier(5); //5th tier is for hotels
        else
            return -1; //Owned buildings do not conform to monopoly rules
    }
}
