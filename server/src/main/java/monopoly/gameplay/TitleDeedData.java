package monopoly.gameplay;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * Deed Data parent class which has a hashmap for different tiers of ownership that change rent value
 * 5 rent tiers for properties (4 houses 1 hotel) 4 rent tiers for railroads and 2 rent tiers for utilities
 * @author Alper Sari
 * @version Dec 16, 2020
 */

public class TitleDeedData {
    @Getter
    @Setter
    private Property property;
    private int buyCost;
    private int mortgageCost;
    private HashMap<Integer, Integer> rentPrice;

    public TitleDeedData(int buyCost, int mortgageCost, HashMap<Integer,Integer> rentPrice)
    {
        this.buyCost = buyCost;
        this.mortgageCost = mortgageCost;
        //Property field is set in property instancing

        //Must have a hashmap constructed from JSON file during tile instancing
        //Maybe have a list of deed data objects?
        this.rentPrice = rentPrice;
    }

    public int getRentTier(int tier)
    {
        return rentPrice.get(tier);
    }
}
