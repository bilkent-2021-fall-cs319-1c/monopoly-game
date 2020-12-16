package monopoly.gameplay;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Deed Data for railroads
 *
 * @author Alper Sari
 * @version Dec 16, 2020
 */

public class RailroadTitleDeedData extends TitleDeedData{
    public RailroadTitleDeedData(int buyCost, int mortgageCost, ArrayList<Integer> rentPrice) {
        super(buyCost, mortgageCost, rentPrice);
    }
}
