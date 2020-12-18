package monopoly.network.packet.important.packet_data.gameplay.property;

import lombok.Data;
import lombok.EqualsAndHashCode;
import monopoly.network.packet.important.packet_data.gameplay.PlayerPacketData;

/**
 * Street data holder that extends the property packet data
 * 
 * @author Javid Baghirov
 * @version Dec 13, 2020
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class StreetPacketData extends PropertyPacketData {
	private static final long serialVersionUID = -3484595840480253292L;

	private int houseCount;
	private boolean hasHotel;
	private String color;

	public StreetPacketData(TilePacketData tileData, PlayerPacketData owner, boolean mortgaged, int houseCount,
			boolean hasHotel, String color) {
		super(tileData, owner, mortgaged);
		this.houseCount = houseCount;
		this.hasHotel = hasHotel;
		this.color = color;
	}
}
