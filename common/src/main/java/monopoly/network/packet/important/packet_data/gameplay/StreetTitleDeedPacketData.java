package monopoly.network.packet.important.packet_data.gameplay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Street title deed data holder that extends the title deed packet data
 * 
 * @author Javid Baghirov
 * @version Dec 13, 2020
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StreetTitleDeedPacketData extends TitleDeedPacketData {
	private static final long serialVersionUID = 2506393575254715525L;
	
	private int housePrice;
	private int hotelPrice;
}
