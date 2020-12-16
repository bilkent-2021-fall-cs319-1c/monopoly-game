package monopoly.gameplay;

import java.util.HashMap;

/**
 * Deed Data for Utility properties
 *
 * @author Alper Sari
 * @version Dec 16, 2020
 */

public class UtilityTitleDeedData extends TitleDeedData{
    public UtilityTitleDeedData(int buyCost, int mortgageCost, HashMap<Integer,Integer> rentPrice) {
        super(buyCost, mortgageCost, rentPrice);
    }

}
