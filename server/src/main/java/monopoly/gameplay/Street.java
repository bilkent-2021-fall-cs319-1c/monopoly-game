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
@Getter
@Setter
public class Street extends Property{

    private int houseCount;
    private int hotelCount;

    public Street(PropertyTile tile, GamePlayer owner, StreetTitleDeedData titleDeed, ColorSet colorSet) {
        super(tile, owner, titleDeed, colorSet);
        houseCount = 0;
        hotelCount = 0;
    }

    @Override
    public int getRentCost() {
        if(hotelCount == 0 && houseCount < 5)
        {
            return getTitleDeed().getRentTier(houseCount);
        }
        else if(hotelCount != 0)
            return getTitleDeed().getRentTier(5); //5th tier is for hotels
        else
            return -1; //Owned buildings do not conform to monopoly rules
    }

    public boolean buildHouse()
    {
        if(houseCount < 5)
        {
            houseCount++;
            getOwner().setBalance(getOwner().getBalance() - ((StreetTitleDeedData) getTitleDeed()).getHouseCost());
            return true;
        }
        else
            return false;
    }

    public boolean buildHotel()
    {
        if (houseCount == 4 && hotelCount == 0)
        {
            hotelCount++;
            getOwner().setBalance(getOwner().getBalance() - ((StreetTitleDeedData)getTitleDeed()).getHotelCost());
            return true;
        }
        else
            return false;
    }
}
