package monopoly.gameplay;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import monopoly.network.packet.important.packet_data.gameplay.property.TitleDeedPacketData;

/**
 * Deed Data parent class which has a hashmap for different tiers of ownership that change rent value
 * 5 rent tiers for properties (4 houses 1 hotel) 4 rent tiers for railroads and 2 rent tiers for utilities
 * @author Alper Sari
 * @version Dec 16, 2020
 */
@Getter
@Setter
public class TitleDeedData {

	private String title;
    private Property property;
    private int buyCost;
    private int mortgageCost;
    private List<Integer> rentPrice;

    TitleDeedData(String title, int buyCost, int mortgageCost, List<Integer> rentPrice)
    {
    	this.title = title;
        this.buyCost = buyCost;
        this.mortgageCost = mortgageCost;
        //Property field is set in property instancing

        this.rentPrice = rentPrice;
    }

    public int getRentTierPrice(int tier)
    {
        return rentPrice.get(tier);
    }
    
    public TitleDeedPacketData getAsTitleDeedPacket() {
    	return new TitleDeedPacketData( title, buyCost, mortgageCost, rentPrice);
    }
}
