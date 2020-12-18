package monopoly.network.packet.important.packet_data.gameplay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import monopoly.network.packet.important.packet_data.PacketData;
import monopoly.network.packet.important.packet_data.UserPacketData;
import monopoly.network.packet.important.packet_data.gameplay.property.TilePacketData;

/**
 * Player holder that implements the generic packet data
 * 
 * @author Javid Baghirov
 * @version Dec 18, 2020
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerPacketData implements PacketData {
	private static final long serialVersionUID = -4414030689393401443L;

	private UserPacketData userData;
	private int balance;
	/**
	 * The location of the tile that the player's token is on
	 */
	private TilePacketData tokenLocation;
	private boolean micOpen;
	private boolean camOpen;
}
