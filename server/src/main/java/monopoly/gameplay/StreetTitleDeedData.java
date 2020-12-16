package monopoly.gameplay;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * Deed Data class for properties that can have buildings on them
 *
 * @author Alper Sari
 * @version Dec 16, 2020
 */

public class StreetTitleDeedData extends TitleDeedData{
    @Getter
    @Setter

    private int houseCost;
    private int hotelCost;

    public StreetTitleDeedData(int buyCost, int mortgageCost, HashMap<Integer,Integer> rentPrice, int houseCost, int hotelCost) {
        super(buyCost, mortgageCost, rentPrice);
        this.houseCost = houseCost;
        this.hotelCost = hotelCost;
    }
}
