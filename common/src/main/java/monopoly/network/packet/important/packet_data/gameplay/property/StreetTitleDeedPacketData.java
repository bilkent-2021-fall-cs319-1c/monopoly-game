package monopoly.network.packet.important.packet_data.gameplay.property;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Street title deed data holder that extends the title deed packet data
 * 
 * @author Javid Baghirov
 * @version Dec 18, 2020
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class StreetTitleDeedPacketData extends TitleDeedPacketData {
	private static final long serialVersionUID = 2506393575254715525L;

	public StreetTitleDeedPacketData(String title, int buyPrice, int mortgagePrice, List<Integer> rentCost,
			int housePrice, int hotelPrice, String color) {
		super(title, buyPrice, mortgagePrice, rentCost);
		this.housePrice = housePrice;
		this.hotelPrice = hotelPrice;
		this.color = color;
	}

	private int housePrice;
	private int hotelPrice;
	private String color;
}