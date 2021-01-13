package monopoly.gameplay.properties;

import java.util.List;

import lombok.Getter;
import monopoly.network.packet.important.packet_data.gameplay.property.StreetTitleDeedPacketData;

/**
 * Deed Data class for properties that can have buildings on them
 *
 * @author Alper Sari
 * @version Dec 16, 2020
 */
@Getter
public class StreetTitleDeedData extends TitleDeedData {
	private int houseCost;
	private int hotelCost;
	private String color;

	public StreetTitleDeedData(String title, int buyCost, int mortgageCost, List<Integer> rentPrice, int houseCost,
			int hotelCost, String color) {
		super(title, buyCost, mortgageCost, rentPrice);
		this.houseCost = houseCost;
		this.hotelCost = hotelCost;
		this.color = color;
	}

	@Override
	public StreetTitleDeedPacketData getAsPacket() {
		return new StreetTitleDeedPacketData(super.getTitle(), getBuyCost(), getMortgageCost(), getRentPrice(),
				houseCost, hotelCost, color);
	}
}
