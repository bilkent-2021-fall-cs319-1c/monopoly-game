package monopoly.network.packet.important.packet_data.gameplay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import monopoly.network.packet.important.packet_data.PacketData;

/**
 * Bank data holder that implements the generic packet data
 * 
 * @author Javid Baghirov
 * @version Dec 13, 2020
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankPacketData implements PacketData {
	private static final long serialVersionUID = -1224104020385919468L;

	private int houseCount;
	private int hotelCount;
}
