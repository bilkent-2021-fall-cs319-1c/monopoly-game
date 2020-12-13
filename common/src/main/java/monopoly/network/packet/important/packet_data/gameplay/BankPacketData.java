package monopoly.network.packet.important.packet_data.gameplay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import monopoly.network.packet.important.packet_data.PacketData;

/**
 * Bank data holder that extends the generic packet data
 * 
 * @author Javid Baghirov
 * @version Dec 13, 2020
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankPacketData extends PacketData {
	private static final long serialVersionUID = -1224104020385919468L;
	
	private int houseCount;
	private int hotelCount;
}
